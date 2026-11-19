package com.logistics.core.infrastructure.adapters.out.persistence;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.logistics.core.domain.model.ShipmentStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "contenedores")
public class ContenedorJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "numero_contenedor", unique = true, nullable = false, length = 50)
    private String numeroContenedor;

    @Column(name = "naviera", nullable = false, length = 50)
    private String naviera;

    @Column(name = "puerto_origen", nullable = false, length = 100)
    private String puertoOrigen;

    @Column(name = "puerto_destino", nullable = false, length = 100)
    private String puertoDestino;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado_aduanero", nullable = false, length = 50)
    private ShipmentStatus estadoAduanero;

    @Column(name = "fecha_embarque", nullable = false)
    private LocalDate fechaEmbarque;

    @Column(name = "fecha_llegada_estimada", nullable = false)
    private LocalDate fechaLlegadaEstimada;

    @Column(name = "notas_aduaneras", columnDefinition = "TEXT")
    private String notasAduaneras;

    @OneToMany(mappedBy = "contenedor", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<EmbarqueDetalleJpaEntity> detalles = new ArrayList<>();

    public ContenedorJpaEntity() {}

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getNumeroContenedor() { return numeroContenedor; }
    public void setNumeroContenedor(String numeroContenedor) { this.numeroContenedor = numeroContenedor; }

    public String getNaviera() { return naviera; }
    public void setNaviera(String naviera) { this.naviera = naviera; }

    public String getPuertoOrigen() { return puertoOrigen; }
    public void setPuertoOrigen(String puertoOrigen) { this.puertoOrigen = puertoOrigen; }

    public String getPuertoDestino() { return puertoDestino; }
    public void setPuertoDestino(String puertoDestino) { this.puertoDestino = puertoDestino; }

    public ShipmentStatus getEstadoAduanero() { return estadoAduanero; }
    public void setEstadoAduanero(ShipmentStatus estadoAduanero) { this.estadoAduanero = estadoAduanero; }

    public LocalDate getFechaEmbarque() { return fechaEmbarque; }
    public void setFechaEmbarque(LocalDate fechaEmbarque) { this.fechaEmbarque = fechaEmbarque; }

    public LocalDate getFechaLlegadaEstimada() { return fechaLlegadaEstimada; }
    public void setFechaLlegadaEstimada(LocalDate fechaLlegadaEstimada) { this.fechaLlegadaEstimada = fechaLlegadaEstimada; }

    public String getNotasAduaneras() { return notasAduaneras; }
    public void setNotasAduaneras(String notasAduaneras) { this.notasAduaneras = notasAduaneras; }

    public List<EmbarqueDetalleJpaEntity> getDetalles() { return detalles; }
    public void setDetalles(List<EmbarqueDetalleJpaEntity> detalles) { this.detalles = detalles; }
}