# API_AI — Módulo de acesso à OpenAI com Spring AI

---
É um módulo JAR de comunicação com a OpenAI, com suporte aos formatos de resposta disponibilizados pelo Spring AI, na versão atual `0.0.1-SNAPSHOT`.
---

## Objetivo

Oferecer classes reutilizáveis para processar prompts com a OpenAI:

- Conexão direta com OpenAI (ChatGPT)
- `ChatClientFactory` para construir `OpenAiChatModel`
- `AiService` para envio de prompts e obtenção de `ChatResponse`
- Suporte a `ResponseFormat` nas sobrecargas do serviço
- Estrutura modular e moderna com Java 21
- Serialização eficiente com Jackson

O código atual não possui `@RestController` nem expõe endpoint HTTP. Apesar das dependências Web/WebFlux no `pom.xml`, o módulo é usado diretamente como dependência Maven pelos demais projetos Java.

---

## Tecnologias Utilizadas

- Java 21
- Spring Boot 3.5.0
- Spring AI 1.0.0-SNAPSHOT
- Dependências Spring WebFlux e Spring Web presentes no `pom.xml`
- Maven Project Build
- Lombok 1.18.30
- Jackson Databind JSON Parsing

---

### Pré-requisitos

- Java 21+
- Maven 3.9+
- Chave de API da OpenAI

---
