package com.logistics.core.infrastructure.adapters.out.persistence;

import java.util.List;

import org.springframework.stereotype.Component;

import com.logistics.core.domain.model.Shipment;
import com.logistics.core.domain.model.ShipmentItem;

@Component
public class ShipmentPersistenceMapper {

    public Shipment toDomain(ContenedorJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        List<ShipmentItem> items = (entity.getDetalles() == null) ? List.of() : entity.getDetalles().stream()
                .map(d -> new ShipmentItem(
                         d.getId() != null ? d.getId().longValue() : null,
                        d.getOrdenCompra(),
                        d.getUnidadesEnTransito(),
                        d.getLoteFabricacion(),
                        d.getProducto() != null ? d.getProducto().getSku() : null,
                        d.getProducto() != null ? d.getProducto().getNombreProducto() : null
                ))
                .toList();

        return new Shipment(
                entity.getId() != null ? entity.getId().longValue() : null,
                entity.getNumeroContenedor(),
                entity.getNaviera(),
                entity.getPuertoOrigen(),
                entity.getPuertoDestino(),
                entity.getEstadoAduanero(),
                entity.getFechaEmbarque(),
                entity.getFechaLlegadaEstimada(),
                entity.getNotasAduaneras(),
                items
        );
    }
}