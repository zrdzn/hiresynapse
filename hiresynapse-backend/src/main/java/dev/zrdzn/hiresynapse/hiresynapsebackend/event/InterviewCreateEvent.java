package dev.zrdzn.hiresynapse.hiresynapsebackend.event;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.InterviewType;

public record InterviewCreateEvent(
    String interviewReferenceId,
    String candidateId,
    InterviewType interviewType,
    boolean enableQuestions,
    int questionsAmount
) {
}
