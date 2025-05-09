package dev.zrdzn.hiresynapse.hiresynapsebackend.event;

import dev.zrdzn.hiresynapse.hiresynapsebackend.model.interview.InterviewType;

public record InterviewCreateEvent(
    long recruiterId,
    long interviewReferenceId,
    long candidateId,
    InterviewType interviewType,
    boolean enableQuestions,
    int questionsAmount
) {
}
