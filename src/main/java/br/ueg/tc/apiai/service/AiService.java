package br.ueg.tc.apiai.service;

import br.ueg.tc.apiai.contract.client.AbstractClient;
import br.ueg.tc.apiai.contract.client.ChatClientFactory;
import org.springframework.ai.chat.client.ChatClient;
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

    public AiService(ChatClientFactory<C> chatClientFactory) {
        OpenAiChatModel openAiChatModel = chatClientFactory.createChatModel();
        this.chatClient = ChatClient.create(openAiChatModel);
    }

    public ChatResponse sendPrompt(String promptText, double temperature, Integer maxTokens, ResponseFormat responseFormat) {
        Prompt prompt = new Prompt(List.of(new UserMessage(promptText)));

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

    public String sendPrompt(String prompt) {
        return this.chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
