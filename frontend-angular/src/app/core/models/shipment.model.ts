export type ShipmentStatus = 'EN_TRANSITO_MARITIMO' | 'EN_INSPECCION_DIAN' | 'NACIONALIZADO_EN_BODEGA';

export interface ShipmentItem {
  purchaseOrder: string;
  quantity: number;
  lotNumber: string;
  productSku: string;
  productName: string;
}

export interface Shipment {
  id: number;
  containerNumber: string;
  shippingLine: string;
  originPort: string;
  destinationPort: string;
  status: ShipmentStatus;
  statusDescription: string;
  departureDate: string;
  estimatedArrivalDate: string;
  customsNotes: string;
  delayedInCustoms: boolean;
  items: ShipmentItem[];
}