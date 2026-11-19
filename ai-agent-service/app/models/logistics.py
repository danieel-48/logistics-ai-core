from sqlalchemy import Column, Integer, String, Date, Numeric, Text, ForeignKey
from sqlalchemy.orm import relationship
from app.database import Base

class ContenedorModel(Base):
    __tablename__ = "contenedores"
    id = Column(Integer, primary_key=True, index=True)
    numero_contenedor = Column(String(50), unique=True, nullable=False, index=True)
    naviera = Column(String(50), nullable=False)
    puerto_origen = Column(String(100), nullable=False)
    puerto_destino = Column(String(100), nullable=False)
    estado_aduanero = Column(String(50), nullable=False)
    fecha_embarque = Column(Date, nullable=False)
    fecha_llegada_estimada = Column(Date, nullable=False)
    notas_aduaneras = Column(Text, nullable=True)
    detalles = relationship("EmbarqueDetalleModel", back_populates="contenedor")

class ProductoModel(Base):
    __tablename__ = "productos"
    id = Column(Integer, primary_key=True, index=True)
    sku = Column(String(50), unique=True, nullable=False, index=True)
    nombre_producto = Column(String(150), nullable=False)
    categoria = Column(String(50), nullable=False)
    precio_unitario = Column(Numeric(10, 2), nullable=False)
    detalles = relationship("EmbarqueDetalleModel", back_populates="producto")

class EmbarqueDetalleModel(Base):
    __tablename__ = "embarques_detalle"
    id = Column(Integer, primary_key=True, index=True)
    contenedor_id = Column(Integer, ForeignKey("contenedores.id", ondelete="CASCADE"), nullable=False)
    producto_id = Column(Integer, ForeignKey("productos.id", ondelete="CASCADE"), nullable=False)
    orden_compra = Column(String(50), nullable=False, index=True)
    unidades_en_transito = Column(Integer, nullable=False)
    lote_fabricacion = Column(String(50), nullable=True)
    contenedor = relationship("ContenedorModel", back_populates="detalles")
    producto = relationship("ProductoModel", back_populates="detalles")