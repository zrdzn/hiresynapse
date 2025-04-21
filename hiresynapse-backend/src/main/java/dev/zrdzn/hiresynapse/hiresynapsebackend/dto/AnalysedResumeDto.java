package dev.zrdzn.hiresynapse.hiresynapsebackend.dto;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public record AnalysedResumeDto(
    String firstName,
    String lastName,
    String phone,
    String executiveSummary,
    String careerTrajectory,
    String analysedSummary,
    Map<String, String> relatedExperience,
    Map<String, String> experience,
    Map<String, String> education,
    Map<String, String> skills,
    Map<String, String> projects,
    List<String> languages,
    List<String> certificates,
    List<String> references,
    List<String> keyAchievements,
    List<String> keySoftSkills
) implements Serializable {
}
