package com.campusfix.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name = "issue_status_history")
public class IssueStatusHistory {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) private Issue issue;
    @Enumerated(EnumType.STRING) private IssueStatus previousStatus;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private IssueStatus newStatus;
    @ManyToOne(optional = false) private User changedBy;
    @Column(nullable = false) private Instant changedAt = Instant.now();
    private String remarks;
    public Long getId() { return id; }
    public Issue getIssue() { return issue; } public void setIssue(Issue v) { issue = v; }
    public IssueStatus getPreviousStatus() { return previousStatus; } public void setPreviousStatus(IssueStatus v) { previousStatus = v; }
    public IssueStatus getNewStatus() { return newStatus; } public void setNewStatus(IssueStatus v) { newStatus = v; }
    public User getChangedBy() { return changedBy; } public void setChangedBy(User v) { changedBy = v; }
    public Instant getChangedAt() { return changedAt; } public String getRemarks() { return remarks; } public void setRemarks(String v) { remarks = v; }
}
