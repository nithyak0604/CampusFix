package com.campusfix.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name = "issues")
public class Issue {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false) private String title;
    @Column(nullable = false, length = 4000) private String description;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private IssueCategory category;
    @ManyToOne(optional = false) private Location location;
    @ManyToOne(optional = false) private User reportedBy;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private Priority priority = Priority.MEDIUM;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private IssueStatus status = IssueStatus.REPORTED;
    private String resolutionRemarks; private Instant resolvedAt; private Instant closedAt;
    @Column(nullable = false, updatable = false) private Instant createdAt; @Column(nullable = false) private Instant updatedAt;
    @PrePersist void prePersist() { createdAt = Instant.now(); updatedAt = createdAt; }
    @PreUpdate void preUpdate() { updatedAt = Instant.now(); }
    public Long getId() { return id; }
    public String getTitle() { return title; } public void setTitle(String v) { title = v; }
    public String getDescription() { return description; } public void setDescription(String v) { description = v; }
    public IssueCategory getCategory() { return category; } public void setCategory(IssueCategory v) { category = v; }
    public Location getLocation() { return location; } public void setLocation(Location v) { location = v; }
    public User getReportedBy() { return reportedBy; } public void setReportedBy(User v) { reportedBy = v; }
    public Priority getPriority() { return priority; } public void setPriority(Priority v) { priority = v; }
    public IssueStatus getStatus() { return status; } public void setStatus(IssueStatus v) { status = v; }
    public String getResolutionRemarks() { return resolutionRemarks; } public void setResolutionRemarks(String v) { resolutionRemarks = v; }
    public Instant getResolvedAt() { return resolvedAt; } public void setResolvedAt(Instant v) { resolvedAt = v; }
    public Instant getClosedAt() { return closedAt; } public void setClosedAt(Instant v) { closedAt = v; }
    public Instant getCreatedAt() { return createdAt; } public Instant getUpdatedAt() { return updatedAt; }
}
