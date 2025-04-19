package dev.zrdzn.hiresynapse.hiresynapsebackend.ai;

import java.util.concurrent.CompletableFuture;

public interface AiClient {

    CompletableFuture<String> sendPrompt(String prompt);

}
