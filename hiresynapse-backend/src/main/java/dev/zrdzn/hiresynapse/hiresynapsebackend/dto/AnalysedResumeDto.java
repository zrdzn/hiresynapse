package dev.zrdzn.hiresynapse.hiresynapsebackend.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public record AnalysedResumeDto(
    String firstName,
    String lastName,
    String phone,
    Integer matchScore,
    Map<String, Integer> experience,
    Map<String, String> education,
    Map<String, String> skills,
    List<String> languages,
    List<String> certificates,
    List<String> references
) implements Serializable {
}
