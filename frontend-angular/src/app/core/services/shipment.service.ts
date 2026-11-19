import { Injectable, inject, signal, computed } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Shipment } from '../models/shipment.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ShipmentService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiBaseUrl}/shipments`;

  // Signals de estado
  shipments = signal<Shipment[]>([]);
  loading = signal<boolean>(false);
  error = signal<string | null>(null);

  // Signals computadas automáticamente
  totalContainers = computed(() => this.shipments().length);
  delayedContainers = computed(() => this.shipments().filter(s => s.delayedInCustoms).length);
  inTransitContainers = computed(() => this.shipments().filter(s => s.status === 'EN_TRANSITO_MARITIMO').length);
  clearedContainers = computed(() => this.shipments().filter(s => s.status === 'NACIONALIZADO_EN_BODEGA').length);

  loadShipments(): void {
    this.loading.set(true);
    this.error.set(null);
    this.http.get<Shipment[]>(this.apiUrl).subscribe({
      next: (data) => {
        this.shipments.set(data);
        this.loading.set(false);
      },
      error: (err) => {
        this.error.set('No se pudo conectar con el Core Backend (Java :8085).');
        this.loading.set(false);
      }
    });
  }
}