package dev.zrdzn.hiresynapse.hiresynapsebackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document(collection = "candidates")
public class Candidate {

    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    @DBRef
    private Job job;

    private int matchScore;
    private String status;
    private Map<String, Integer> experience;
    private Map<String, String> education;
    private Map<String, String> skills;
    private List<String> languages;
    private List<String> certificates;
    private List<String> references;
    private double salaryExpectation;

}
