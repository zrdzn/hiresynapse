package dev.zrdzn.hiresynapse.hiresynapsebackend.event;

public record CandidateCreateEvent(
    String candidateReferenceId,
    String jobId,
    String resumeFileName,
    String resumeFilePath,
    String resumeFileType
) {
}
