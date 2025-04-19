package dev.zrdzn.hiresynapse.hiresynapsebackend.ai;

public class AiPrompts {

    public static final String CANDIDATE_RESUME_ANALYZE_PROMPT =
    """
    At the end of this prompt I will provide you a full content of resume.
    Your task is to analyze resume and extract all information as much as possible according to scheme I will provide you.
    After your analysis, you need to output me ONLY data in json that can be parsed to java pojo.
    Candidate is applying for job title: "{TITLE}"
    That is the scheme you need to fulfill, if no data is provided just simply put null in that place:
    {
        String firstName;
        String lastName;
        String phone;
        Integer matchScore; // calculate in estimated 1-100% how candidate matches the given job title based off on other data in this resume
        Map<String, Integer> experience; // company with job title: years (if lower than year, put 1)
        Map<String, String> education;
        Map<String, String> skills; // skill: good/basic/bad etc
        List<String> languages; // convert to ISO 639-1
        List<String> certificates;
        List<String> references;
    }
    Resume content: {CONTENT}
   \s""";

}