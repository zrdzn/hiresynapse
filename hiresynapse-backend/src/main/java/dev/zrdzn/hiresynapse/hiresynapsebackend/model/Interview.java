package dev.zrdzn.hiresynapse.hiresynapsebackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "interviews")
public class Interview implements Serializable {

    @Id
    @Nullable
    private String id;

    @DBRef
    private User recruiter;

    @DBRef
    private Candidate candidate;

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private InterviewStatus status;
    private TaskStatus taskStatus;

    private Instant interviewAt;
    private Instant completedAt;
    private Instant cancelledAt;
    private Instant confirmedAt;
    private InterviewType interviewType;
    private String notes;
    private String feedback;
    private boolean enableQuestions;
    private int questionsAmount;
    private List<String> questions;

}
