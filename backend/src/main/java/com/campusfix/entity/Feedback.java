package com.campusfix.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name = "feedback", uniqueConstraints = @UniqueConstraint(columnNames = {"issue_id"}))
public class Feedback {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @OneToOne(optional = false) private Issue issue;
    @ManyToOne(optional = false) private User submittedBy;
    @Column(nullable = false) private int rating;
    @Column(length = 2000) private String comment;
    @Column(nullable = false) private Instant createdAt = Instant.now();
    public Long getId() { return id; }
    public Issue getIssue() { return issue; } public void setIssue(Issue v) { issue = v; }
    public User getSubmittedBy() { return submittedBy; } public void setSubmittedBy(User v) { submittedBy = v; }
    public int getRating() { return rating; } public void setRating(int v) { rating = v; }
    public String getComment() { return comment; } public void setComment(String v) { comment = v; }
    public Instant getCreatedAt() { return createdAt; }
}
