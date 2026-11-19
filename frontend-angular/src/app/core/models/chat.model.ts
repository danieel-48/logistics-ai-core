export interface ActionStep {
  toolName: string;
  arguments: Record<string, any>;
  output: any;
}

export interface AgentChatResponse {
  prompt: string;
  answer: string;
  stepsExecuted: ActionStep[];
}

export interface ChatMessage {
  sender: 'user' | 'agent';
  text: string;
  timestamp: Date;
  steps?: ActionStep[];
}