package dev.zrdzn.hiresynapse.hiresynapsebackend.event;

public record CandidateCreateEvent(
    long candidateReferenceId,
    long jobId,
    String resumeFileName,
    String resumeFilePath,
    String resumeFileType
) {
}
