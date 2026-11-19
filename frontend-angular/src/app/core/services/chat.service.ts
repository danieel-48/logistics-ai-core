import { Injectable, inject, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { AgentChatResponse, ChatMessage } from '../models/chat.model';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private http = inject(HttpClient);
  private apiUrl = `${environment.apiBaseUrl}/logistics/chat`;
  
  messages = signal<ChatMessage[]>([
    {
      sender: 'agent',
      text: '¡Hola! Soy tu Asistente de Comercio Exterior y Logística. Puedes preguntarme en qué contenedor viene cualquier producto, su estado de aduanas o fechas estimadas de llegada.',
      timestamp: new Date()
    }
  ]);
  loading = signal<boolean>(false);

  askAgent(prompt: string): void {
    if (!prompt.trim() || this.loading()) return;

    // 1. Agregamos el mensaje del usuario al chat
    this.messages.update(msgs => [
      ...msgs,
      { sender: 'user', text: prompt, timestamp: new Date() }
    ]);

    this.loading.set(true);

    // 2. Disparamos la petición al Gateway Java
    this.http.post<AgentChatResponse>(this.apiUrl, { prompt }).subscribe({
      next: (response) => {
        this.messages.update(msgs => [
          ...msgs,
          {
            sender: 'agent',
            text: response.answer,
            timestamp: new Date(),
            steps: response.stepsExecuted
          }
        ]);
        this.loading.set(false);
      },
      error: (err) => {
        this.messages.update(msgs => [
          ...msgs,
          {
            sender: 'agent',
            text: '❌ Ocurrió un error al consultar el agente de IA. Verifica que el backend esté arriba.',
            timestamp: new Date()
          }
        ]);
        this.loading.set(false);
      }
    });
  }
}