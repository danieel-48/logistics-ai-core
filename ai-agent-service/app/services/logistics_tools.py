from sqlalchemy import and_, or_ 
from app.database import SessionLocal
from app.models.logistics import ContenedorModel, ProductoModel, EmbarqueDetalleModel
from langchain_core.tools import tool
import re


def _generar_regex_acento(palabra: str) -> str:
    """Convierte una palabra en una expresión regular tolerante a tildes y plurales."""
    p = palabra.lower()
    
    # Manejo de plurales: si termina en 's', la 's' final es opcional (s)?
    tiene_plural = False
    if p.endswith("s") and len(p) > 3:
        p = p[:-1]
        tiene_plural = True
    # Reemplazo de cada vocal por su grupo regex [vocal con y sin tilde]
    p = re.sub(r"[aá]", "[aá]", p)
    p = re.sub(r"[eé]", "[eé]", p)
    p = re.sub(r"[ií]", "[ií]", p)
    p = re.sub(r"[oó]", "[oó]", p)
    p = re.sub(r"[uú]", "[uú]", p)
    if tiene_plural:
        p += "s?"
    return p

@tool
def buscar_producto_en_transito(termino_busqueda: str) -> dict:
    """
    Busca productos cosméticos por nombre aproximado o por SKU exacto y devuelve
    en qué contenedores marítimos vienen, cuántas unidades y la fecha estimada de llegada (ETA).
    Args:
        termino_busqueda: Nombre del producto (ej. 'labial', 'rubi', 'base', 'serum') o SKU (ej. 'MK-LIP-04').
    """
    db = SessionLocal()
    try:
        palabras = termino_busqueda.strip().split()
        
        # Filtros: cada palabra del usuario debe coincidir vía Regex (~*) en el nombre del producto o en el SKU
        filtros_palabras = []
        for p in palabras:
            regex_patron = _generar_regex_acento(p)
            filtros_palabras.append(
                or_(
                    ProductoModel.nombre_producto.op("~*")(regex_patron),
                    ProductoModel.sku.ilike(f"%{p}%")
                )
            )
        resultados = (
            db.query(ProductoModel, EmbarqueDetalleModel, ContenedorModel)
            .join(EmbarqueDetalleModel, ProductoModel.id == EmbarqueDetalleModel.producto_id)
            .join(ContenedorModel, EmbarqueDetalleModel.contenedor_id == ContenedorModel.id)
            .filter(and_(*filtros_palabras))
            .all()
        )
        if not resultados:
            return {
                "encontrado": False,
                "mensaje": f"No se encontraron órdenes ni contenedores en tránsito para '{termino_busqueda}'."
            }
        embarques = []
        for prod, detalle, cont in resultados:
            embarques.append({
                "sku": prod.sku,
                "producto": prod.nombre_producto,
                "categoria": prod.categoria,
                "orden_compra": detalle.orden_compra,
                "unidades_en_camino": detalle.unidades_en_transito,
                "numero_contenedor": cont.numero_contenedor,
                "naviera": cont.naviera,
                "puerto_destino": cont.puerto_destino,
                "estado_actual": cont.estado_aduanero,
                "fecha_estimada_llegada": str(cont.fecha_llegada_estimada),
                "novedades_aduaneras": cont.notas_aduaneras
            })

        for e in embarques:
            print(f"   -> SKU: {e['sku']} | Producto: {e['producto']} | Contenedor: {e['numero_contenedor']}")
        return {
            "encontrado": True,
            "total_coincidencias": len(embarques),
            "embarques": embarques
        }
    finally:
        db.close()

@tool
def consultar_estado_contenedor(numero_contenedor: str) -> dict:
    """
    Consulta la información técnica, trazabilidad y estado de nacionalización aduanera (DIAN/INVIMA)
    de un contenedor marítimo específico a partir de su número de serie.
    Args:
        numero_contenedor: Código alfanumérico del contenedor (ej. 'MSCU9823145', 'MAEU4581290').
    """
    db = SessionLocal()
    try:
        contenedor = (
            db.query(ContenedorModel)
            .filter(ContenedorModel.numero_contenedor.ilike(numero_contenedor.strip()))
            .first()
        )
        if not contenedor:
            return {
                "encontrado": False,
                "mensaje": f"El contenedor '{numero_contenedor}' no existe en el sistema de comercio exterior."
            }
        # Obtenemos la lista de productos que vienen dentro de ese contenedor
        manifiesto_carga = []
        for detalle in contenedor.detalles:
            manifiesto_carga.append({
                "sku": detalle.producto.sku,
                "producto": detalle.producto.nombre_producto,
                "unidades": detalle.unidades_en_transito,
                "orden_compra": detalle.orden_compra,
                "lote": detalle.lote_fabricacion
            })
        return {
            "encontrado": True,
            "numero_contenedor": contenedor.numero_contenedor,
            "naviera": contenedor.naviera,
            "ruta": f"{contenedor.puerto_origen} -> {contenedor.puerto_destino}",
            "estado_aduanero": contenedor.estado_aduanero,
            "fecha_embarque": str(contenedor.fecha_embarque),
            "fecha_llegada_estimada": str(contenedor.fecha_llegada_estimada),
            "notas_aduaneras": contenedor.notas_aduaneras,
            "manifiesto_carga": manifiesto_carga
        }
    finally:
        db.close()

@tool
def listar_contenedores_en_transito() -> dict:
    """
    Devuelve un resumen de todos los contenedores marítimos que están actualmente navegando
    o en inspección aduanera y que aún no han sido nacionalizados en bodega.
    """
    db = SessionLocal()
    try:
        contenedores = (
            db.query(ContenedorModel)
            .filter(ContenedorModel.estado_aduanero != "NACIONALIZADO_EN_BODEGA")
            .all()
        )
        resumen = []
        for c in contenedores:
            resumen.append({
                "numero_contenedor": c.numero_contenedor,
                "naviera": c.naviera,
                "destino": c.puerto_destino,
                "estado": c.estado_aduanero,
                "fecha_llegada_estimada": str(c.fecha_llegada_estimada)
            })
        return {
            "total_contenedores_pendientes": len(resumen),
            "contenedores": resumen
        }
    finally:
        db.close()