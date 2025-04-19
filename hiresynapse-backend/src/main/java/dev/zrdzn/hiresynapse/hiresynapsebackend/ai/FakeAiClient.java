package dev.zrdzn.hiresynapse.hiresynapsebackend.ai;

import java.util.concurrent.CompletableFuture;

public class FakeAiClient implements AiClient {

    @Override
    public CompletableFuture<String> sendPrompt(String prompt) {
        return CompletableFuture.completedFuture("This is a fake response.");
    }

}
