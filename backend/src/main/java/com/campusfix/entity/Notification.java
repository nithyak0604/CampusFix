package com.campusfix.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name = "notifications")
public class Notification {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) private User recipient;
    @ManyToOne private Issue issue;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private NotificationType type;
    @Column(nullable = false, length = 500) private String message;
    @Column(nullable = false) private Instant createdAt = Instant.now();
    private boolean readFlag;
    public Long getId() { return id; }
    public User getRecipient() { return recipient; } public void setRecipient(User v) { recipient = v; }
    public Issue getIssue() { return issue; } public void setIssue(Issue v) { issue = v; }
    public NotificationType getType() { return type; } public void setType(NotificationType v) { type = v; }
    public String getMessage() { return message; } public void setMessage(String v) { message = v; }
    public Instant getCreatedAt() { return createdAt; } public boolean isReadFlag() { return readFlag; } public void setReadFlag(boolean v) { readFlag = v; }
}
