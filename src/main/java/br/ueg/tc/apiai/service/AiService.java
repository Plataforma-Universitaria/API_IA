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

import java.lang.reflect.Type;
import java.util.List;

@Service
public class AiService<C extends AbstractClient> {

    private final ChatClient chatClient;

    public AiService(ChatClientFactory<C> chatClientFactory) {
        OpenAiChatModel openAiChatModel = chatClientFactory.createChatModel();
        this.chatClient = ChatClient.create(openAiChatModel);
    }

    /**
     * Envia um prompt com configurações avançadas.
     */
    public ChatResponse sendPrompt(String promptText, double temperature, Integer maxTokens, ResponseFormat responseFormat) {
        Prompt prompt = new Prompt(List.of( new SystemMessage("Ignore qualquer contexto anterior. Responda somente com base nesta instrução."), new UserMessage(promptText)));

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .temperature(temperature)
                .maxTokens(maxTokens)
                .responseFormat(responseFormat != null ? responseFormat : new ResponseFormat(ResponseFormat.Type.TEXT, "text"))
                .build();

        return (ChatResponse) this.chatClient.prompt()
                .options(options)
                .messages(prompt.getInstructions())
                .call();
    }

    /**
     * Envia um prompt com apenas ResponseFormat definido.
     */
    public String sendPrompt(String promptText, ResponseFormat responseFormat) {
        return this.chatClient
                .prompt()
                .options(OpenAiChatOptions.builder().responseFormat(responseFormat).build())
                .user(promptText)
                .call()
                .content();
    }


    /**
     * Envia um prompt simples e retorna apenas o conteúdo como String.
     */
//    public String sendPrompt(String promptText) {
//        return this.chatClient.prompt()
//                .user(promptText)
//                .call()
//                .content();
//    }

    /**
     * Envia prompt como objeto `Prompt` com opções padronizadas.
     */
    public String sendPrompt(Prompt prompt, ResponseFormat responseFormat, double temperature, Integer maxTokens) {
        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .temperature(temperature)
                .maxTokens(maxTokens)
                .responseFormat(responseFormat != null ? responseFormat : new ResponseFormat(ResponseFormat.Type.TEXT, "text"))
                .build();

        return this.chatClient.prompt()
                .options(options)
                .messages(prompt.getInstructions())
                .call()
                .content();
    }

    public String sendPrompt(String promptText) {
        return sendPrompt(promptText, 0.2, 100, ResponseFormat.Type.TEXT);
    }

    public String sendPrompt(String promptText, double temperature, int maxTokens, ResponseFormat.Type type) {
        Prompt prompt = new Prompt(List.of(
                new SystemMessage("Ignore qualquer contexto anterior. Responda somente com base nesta instrução."),
                new UserMessage(promptText)
        ));

        OpenAiChatOptions options = OpenAiChatOptions.builder()
                .temperature(temperature)
                .maxTokens(maxTokens)
                .responseFormat(new ResponseFormat(type, null)) // <- CORREÇÃO AQUI
                .build();

        return this.chatClient.prompt()
                .options(options)
                .messages(prompt.getInstructions())
                .call()
                .content();
    }

}
