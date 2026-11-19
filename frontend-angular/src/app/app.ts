import { Component, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ShipmentService } from './core/services/shipment.service';
import { ChatService } from './core/services/chat.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App implements OnInit {
  shipmentService = inject(ShipmentService);
  chatService = inject(ChatService);

  userPrompt = signal<string>('');
  selectedTab = signal<'all' | 'delayed' | 'in_transit'>('all');
  expandedAuditIndex = signal<number | null>(null);

  quickQueries = [
    '¿Qué contenedores están demorados en aduana y por qué?',
    '¿En qué contenedor viene la base liquida beige?',
    '¿Dónde está el labial rubí y cuántas unidades son?',
    '¿Cuál es el estado de las pestañinas waterproof?'
  ];

  ngOnInit(): void {
    this.shipmentService.loadShipments();
  }

  filteredShipments() {
    const list = this.shipmentService.shipments();
    const tab = this.selectedTab();
    if (tab === 'delayed') {
      return list.filter(s => s.delayedInCustoms);
    }
    if (tab === 'in_transit') {
      return list.filter(s => s.status === 'EN_TRANSITO_MARITIMO');
    }
    return list;
  }

  sendChat(): void {
    const text = this.userPrompt().trim();
    if (text) {
      this.chatService.askAgent(text);
      this.userPrompt.set('');
    }
  }

  useQuickQuery(query: string): void {
    this.userPrompt.set(query);
    this.sendChat();
  }

  toggleAudit(index: number): void {
    this.expandedAuditIndex.update(cur => cur === index ? null : index);
  }

  formatJson(data: any): string {
    if (typeof data === 'string') {
      try {
        return JSON.stringify(JSON.parse(data), null, 2);
      } catch {
        return data;
      }
    }
    return JSON.stringify(data, null, 2);
  }

  renderMarkdown(text: string): string {
    if (!text) return '';

    // 1. Reemplazamos encabezados ###
    let html = text.replace(/^### (.*$)/gim, '<h4 class="md-heading">$1</h4>');

    // 2. Reemplazamos negritas **texto**
    html = html.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');

    // 3. Convertimos viñetas * o - a <li> sin saltos extras
    html = html.replace(/^\s*[\*\-]\s+(.*$)/gim, '<li class="md-item">$1</li>');

    // 4. Agrupamos los <li> consecutivos dentro de un <ul> compacto
    html = html.replace(/((?:<li class="md-item">.*?<\/li>\s*)+)/gs, '<ul class="md-list">$1</ul>');

    // 5. Saltos de línea simples solo para párrafos de texto (no dentro de listas)
    html = html.replace(/\n\n/g, '<div class="md-spacing"></div>');
    html = html.replace(/(?<!<\/li>|<\/ul>|<\/h4>)\n/g, '<br/>');

    return html;
  }
}