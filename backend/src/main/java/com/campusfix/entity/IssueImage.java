package com.campusfix.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name = "issue_images")
public class IssueImage {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) private Issue issue;
    @Column(nullable = false) private String storageKey;
    @Column(nullable = false) private String contentType;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private ImageType type;
    private Instant createdAt = Instant.now();
    public enum ImageType { ORIGINAL, AFTER_REPAIR, VERIFICATION }
    public Long getId() { return id; }
    public Issue getIssue() { return issue; } public void setIssue(Issue v) { issue = v; }
    public String getStorageKey() { return storageKey; } public void setStorageKey(String v) { storageKey = v; }
    public String getContentType() { return contentType; } public void setContentType(String v) { contentType = v; }
    public ImageType getType() { return type; } public void setType(ImageType v) { type = v; }
    public Instant getCreatedAt() { return createdAt; }
}
