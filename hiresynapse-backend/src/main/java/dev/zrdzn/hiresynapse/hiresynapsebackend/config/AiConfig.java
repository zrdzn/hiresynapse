package dev.zrdzn.hiresynapse.hiresynapsebackend.config;

import dev.zrdzn.hiresynapse.hiresynapsebackend.ai.AiClient;
import dev.zrdzn.hiresynapse.hiresynapsebackend.ai.FakeAiClient;
import dev.zrdzn.hiresynapse.hiresynapsebackend.ai.GptAiClient;
import io.github.sashirestela.openai.SimpleOpenAI;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    private final String openAiApiKey;

    public AiConfig(@Value("${ai.api-key}") String openAiApiKey) {
        this.openAiApiKey = openAiApiKey;
    }

    @Bean
    public AiClient aiClient() {
        if (openAiApiKey.isEmpty()) return new FakeAiClient();

        return new GptAiClient(SimpleOpenAI.builder()
            .apiKey(openAiApiKey)
            .build());
    }

}
