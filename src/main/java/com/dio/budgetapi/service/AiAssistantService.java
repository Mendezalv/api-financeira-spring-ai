package com.dio.budgetapi.service;

import org.springframework.ai.audio.transcription.AudioTranscriptionPrompt;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiAudioSpeechModel;
import org.springframework.ai.openai.OpenAiAudioTranscriptionModel;
import org.springframework.ai.openai.audio.speech.SpeechPrompt;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Service
public class AiAssistantService {

    private final ChatClient chatClient;
    private final OpenAiAudioTranscriptionModel transcriptionModel;
    private final OpenAiAudioSpeechModel speechModel;

    public AiAssistantService(ChatClient.Builder chatClientBuilder,
                              OpenAiAudioTranscriptionModel transcriptionModel,
                              OpenAiAudioSpeechModel speechModel) {
        
        this.transcriptionModel = transcriptionModel;
        this.speechModel = speechModel;
        
        // Configuração do System Prompt e ativação das Tools
        this.chatClient = chatClientBuilder
                .defaultSystem("""
                    Você é uma assistente financeira rigorosa.
                    Seu objetivo é ajudar o usuário a controlar os gastos.
                    Sempre que o usuário relatar um gasto:
                    1. Categorize-o (ex: Alimentação, Transporte, Lazer, Educação).
                    2. Use a ferramenta 'saveTransaction' para registrar o gasto.
                    3. Em seguida, use a ferramenta 'checkBudget' para verificar como está o orçamento dessa categoria.
                    4. Responda informando que o gasto foi registrado e dê um aviso amigável, 
                       ou um alerta severo caso ele tenha ultrapassado o limite mensal.
                    """)
                .defaultFunctions("saveTransaction", "checkBudget")
                .build();
    }

    public String processTextCommand(String userMessage) {
        // Chamada real para a LLM (Tool Calling)
        return this.chatClient.prompt()
                .user(userMessage)
                .call()
                .content();
    }

    public byte[] processAudioCommand(Resource audioResource) {
        // 1. Transcreve o áudio (Speech-to-Text) usando Whisper
        String transcribedText = transcriptionModel.call(new AudioTranscriptionPrompt(audioResource))
                                                   .getResult().getOutput();

        // 2. Processa o texto via ChatClient com as Tools
        String aiResponseText = processTextCommand(transcribedText);

        // 3. Converte a resposta em áudio (Text-to-Speech) usando TTS-1
        return speechModel.call(new SpeechPrompt(aiResponseText))
                          .getResult().getOutput();
    }
}