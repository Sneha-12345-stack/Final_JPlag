package com.plagiarism.plagiarismchecker.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "submissions")
public class Submission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;

    private String filePath;

    private String fileName;

    private LocalDateTime submittedAt;

    // ✅ Relationship with Job
    @ManyToOne
    @JoinColumn(name = "job_id")
    private Job job;

    public Submission() {}

    // Getters
    public Long getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public Job getJob() {
        return job;
    }

    // Setters
    public void setId(Long id) {
        this.id = id;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public void setJob(Job job) {
        this.job = job;
    }
}