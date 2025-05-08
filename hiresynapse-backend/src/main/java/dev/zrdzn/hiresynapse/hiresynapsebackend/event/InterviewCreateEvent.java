package dev.zrdzn.hiresynapse.hiresynapsebackend.event;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.InterviewType;

public record InterviewCreateEvent(
    long interviewReferenceId,
    long candidateId,
    InterviewType interviewType,
    boolean enableQuestions,
    int questionsAmount
) {
}
