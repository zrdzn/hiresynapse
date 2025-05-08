package dev.zrdzn.hiresynapse.hiresynapsebackend.ai;

import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.chat.ChatMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;

public class GptAiClient implements AiClient {

    private final Logger logger = LoggerFactory.getLogger(GptAiClient.class);

    private final SimpleOpenAI openAi;

    public GptAiClient(SimpleOpenAI openAi) {
        this.openAi = openAi;
    }

    public CompletableFuture<String> sendPrompt(String prompt) {
        ChatRequest request = ChatRequest.builder()
            .model("gpt-4o")
            .message(ChatMessage.UserMessage.of(prompt))
            .temperature(1.0D)
            .presencePenalty(0.0D)
            .frequencyPenalty(0.0D)
            .topP(1.0D)
            .build();

        logger.debug("Sending prompt to OpenAI: {}", prompt);

        return openAi.chatCompletions()
            .create(request)
            .thenApplyAsync(response -> response.getChoices()
                .getFirst()
                .getMessage()
                .getContent());
    }

}
