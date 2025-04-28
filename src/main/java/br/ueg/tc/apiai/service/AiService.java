package br.ueg.tc.apiai.service;

import br.ueg.tc.apiai.contract.client.AbstractClient;
import br.ueg.tc.apiai.contract.client.ChatClientFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.stereotype.Service;

@Service
public class AiService<C extends AbstractClient> {
    private final ChatClient chatClient;

    public AiService(ChatClientFactory<C> chatClientFactory) {
        OpenAiChatModel openAiChatModel = chatClientFactory.createChatModel();
        this.chatClient = ChatClient.create(openAiChatModel);
    }

    public String sendPrompt(String prompt) {
        return this.chatClient.prompt()
                .user(prompt)
                .call()
                .content();
    }
}
