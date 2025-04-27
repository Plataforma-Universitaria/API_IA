package br.ueg.tc.aiapi;

import br.ueg.tc.aiapi.contract.client.AbstractClient;
import br.ueg.tc.aiapi.contract.client.ChatClientFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApiAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiAiApplication.class, args);
    }
    @Bean
    public <C extends AbstractClient> ChatClientFactory<C> chatClientFactory(C client) {
        return new ChatClientFactory<>(client);
    }
}
