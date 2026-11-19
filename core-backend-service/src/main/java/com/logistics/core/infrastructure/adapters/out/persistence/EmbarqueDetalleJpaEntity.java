package com.logistics.core.infrastructure.adapters.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "embarques_detalle")
public class EmbarqueDetalleJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contenedor_id", nullable = false)
    private ContenedorJpaEntity contenedor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "producto_id", nullable = false)
    private ProductoJpaEntity producto;

    @Column(name = "orden_compra", nullable = false, length = 50)
    private String ordenCompra;

    @Column(name = "unidades_en_transito", nullable = false)
    private Integer unidadesEnTransito;

    @Column(name = "lote_fabricacion", length = 50)
    private String loteFabricacion;

    public EmbarqueDetalleJpaEntity() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public ContenedorJpaEntity getContenedor() { return contenedor; }
    public void setContenedor(ContenedorJpaEntity contenedor) { this.contenedor = contenedor; }

    public ProductoJpaEntity getProducto() { return producto; }
    public void setProducto(ProductoJpaEntity producto) { this.producto = producto; }

    public String getOrdenCompra() { return ordenCompra; }
    public void setOrdenCompra(String ordenCompra) { this.ordenCompra = ordenCompra; }

    public Integer getUnidadesEnTransito() { return unidadesEnTransito; }
    public void setUnidadesEnTransito(Integer unidadesEnTransito) { this.unidadesEnTransito = unidadesEnTransito; }

    public String getLoteFabricacion() { return loteFabricacion; }
    public void setLoteFabricacion(String loteFabricacion) { this.loteFabricacion = loteFabricacion; }
}