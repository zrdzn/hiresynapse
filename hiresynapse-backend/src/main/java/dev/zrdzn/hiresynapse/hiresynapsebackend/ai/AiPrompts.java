package dev.zrdzn.hiresynapse.hiresynapsebackend.ai;

public class AiPrompts {

    public static final String CANDIDATE_RESUME_ANALYZE_PROMPT =
    """
    At the end of this prompt I will provide you a full content of resume.
    Your task is to analyze resume and extract all information as much as possible according to scheme I will provide you.
    After your analysis, you need to output me ONLY data in json that can be parsed to java pojo.
    Candidate is applying for job title: "{TITLE}".
    Make sure you calculate everything according to the current year which is "{YEAR}".
    If something is in other than English language, please translate it to English.
    In case of related experience, please don't be very strict about it - if job title is e.g. "PHP Developer"
    and candidate is applying for "Java Developer", then include it. It must be similar in field, but not exactly the same.
    That is the scheme you need to fulfill, if no data is provided just simply put null in that place:
    {
        String firstName;
        String lastName;
        String phone; // XXX XXX XXX
        String executiveSummary; // short summary of the candidate
        String analysedSummary; // short summary of the candidate analysed by you, include everything possible
        String careerTrajectory; // career trajectory of the candidate calculated from the experience
        Map<String, String> relatedExperience; // <job title> at <company>: startYear-endYear NOTE: here are only related job titles to the job title candidate is applying for
        Map<String, String> experience; // <job title> at <company>: startYear-endYear
        Map<String, String> education; // education: school name, degree, year of graduation
        Map<String, String> skills; // skill: good/basic/bad etc
        Map<String, String> projects; // project name: description (related technologies to the job title candidate is applying for)
        List<String> languages; // convert to ISO 639-1
        List<String> certificates;
        List<String> references;
        List<String> keyAchievements; // key achievements in the candidate's career
        List<String> keySoftSkills; // key soft skills in the candidate's career
    }
    Resume content: {CONTENT}
   \s""";

    public static final String INTERVIEW_QUESTIONS_PROMPT =
    """
    You are an AI assistant helping a recruiter to prepare questions for an interview.
    Make sure you are asking questions that are related to the job title candidate is applying for.
    Take into account the candidate's experience, skills and projects and mostly important type of an interview.
    Ask questions in a quite simple but professional way so the candidate can understand them.
    After your preparation, you need to output me ONLY data in specified format without any additional comments:
    ["question1", "question2", "question3", ...]
    Generate EXACT {AMOUNT} questions (maximum 10 questions).
    The type of the interview is: "{INTERVIEW_TYPE}".
    The candidate is applying for a job title: "{TITLE}".
    The candidate has the following experience: "{EXPERIENCE}".
    The candidate has the following skills: "{SKILLS}".
    The candidate has the following projects: "{PROJECTS}".
    Short summary of the candidate: "{SUMMARY}".
   \s""";

}