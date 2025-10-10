package br.ueg.tc.apiai.service;

import br.ueg.tc.apiai.client.AbstractClient;
import br.ueg.tc.apiai.client.ChatClientFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.ResponseFormat;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiService<C extends AbstractClient> {

    private final ChatClient chatClient;

    private static final String SYSTEM_PROMPT = """
            Você é um assistente de IA especialista e pragmático.
            Sua função é fornecer respostas diretas, factuais e concisas.
            - NÃO use saudações, despedidas ou frases de preenchimento.
            - NÃO dê opiniões, especulações ou conselhos.
            - NÃO explique o que você faz.
            - Foque exclusivamente nos dados e na tarefa solicitada.
            - Responda de forma objetiva e sem rodeios.
            """;

    public AiService(ChatClientFactory<C> chatClientFactory) {
        OpenAiChatModel openAiChatModel = chatClientFactory.createChatModel();
        this.chatClient = ChatClient.create(openAiChatModel);
    }

    /**
     * Envia um prompt com configurações avançadas.
     */
    public ChatResponse sendPrompt(String promptText, double temperature, Integer maxTokens, ResponseFormat responseFormat) {
        Prompt prompt = createCleanPrompt(promptText, SYSTEM_PROMPT);

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .temperature(temperature)
                .topP(1.0)
                .maxTokens(maxTokens)
                .responseFormat(responseFormat != null
                        ? responseFormat
                        : new ResponseFormat(ResponseFormat.Type.TEXT, "text"))
                .build();

        return (ChatResponse) this.chatClient.prompt()
                .options(options)
                .messages(prompt.getInstructions())
                .call();
    }

    /**
     * Método simples com ResponseFormat e SystemMessage.
     */
    public String sendPromptWithSystemMessage(String promptText, String systemText, ResponseFormat format) {
        Prompt prompt = createCleanPrompt(promptText, systemText);

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .temperature(0.0)
                .topP(1.0)
                .maxTokens(500)
                .responseFormat(format != null ? format : new ResponseFormat(ResponseFormat.Type.TEXT, "text"))
                .build();

        return this.chatClient.prompt()
                .options(options)
                .messages(prompt.getInstructions())
                .call()
                .content();
    }

    /**
     * Envia prompt como objeto `Prompt` com opções padronizadas.
     */
    public String sendPrompt(Prompt prompt, ResponseFormat responseFormat, double temperature, Integer maxTokens) {
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .temperature(temperature)
                .maxTokens(maxTokens)
                .responseFormat(responseFormat != null
                        ? responseFormat
                        : new ResponseFormat(ResponseFormat.Type.TEXT, "text"))
                .build();

        return this.chatClient.prompt()
                .options(options)
                .messages(prompt.getInstructions())
                .call()
                .content();
    }

    /**
     * Versão simplificada: apenas texto, defaults fixos.
     */
    public String sendPrompt(String promptText) {
        return sendPrompt(promptText, 0.0, 500, ResponseFormat.Type.TEXT, SYSTEM_PROMPT);
    }

    /**
     * Com systemMessage customizado e opções avançadas.
     */
    public String sendPrompt(String promptText, double temperature, int maxTokens, ResponseFormat.Type type, String systemText) {
        Prompt prompt = createCleanPrompt(promptText, systemText);

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .temperature(temperature)
                .maxTokens(maxTokens)
                .topP(1.0)
                .responseFormat(new ResponseFormat(type, null))
                .build();

        return this.chatClient.prompt()
                .options(options)
                .messages(prompt.getInstructions())
                .call()
                .content();
    }

    /**
     * Cria prompt unindo systemMessage e userMessage.
     */
    private Prompt createCleanPrompt(String userText, String systemText) {
        return new Prompt(List.of(
                new SystemMessage(systemText),
                new UserMessage(userText)
        ));
    }
}
