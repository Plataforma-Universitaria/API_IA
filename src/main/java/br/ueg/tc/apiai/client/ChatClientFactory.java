package br.ueg.tc.apiai.client;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.ai.model.ApiKey;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.retry.support.RetryTemplate;

public class ChatClientFactory<C extends AbstractClient> {
    private final C client;

    public ChatClientFactory(C client) {
        this.client = client;
    }


    public OpenAiChatModel createChatModel() {
        ApiKey customApiKey = client::getApiKey;

        OpenAiApi openAiApi = OpenAiApi.builder()
                .apiKey(customApiKey)
                .build();

        OpenAiChatOptions chatOptions = OpenAiChatOptions.builder()
                .model(client.getModel())
                .build();

        RetryTemplate retryTemplate = new RetryTemplate();

        return new OpenAiChatModel(openAiApi, chatOptions,
                ToolCallingManager.builder().build(), retryTemplate, ObservationRegistry.create());
    }
}
