package com.campusfix.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name = "verification_records")
public class VerificationRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) private Issue issue;
    @ManyToOne(optional = false) private User verifier;
    @Enumerated(EnumType.STRING) @Column(nullable = false) private VerificationDecision decision;
    @Column(nullable = false, length = 2000) private String remarks;
    @Column(nullable = false) private Instant verifiedAt = Instant.now();
    public Long getId() { return id; }
    public Issue getIssue() { return issue; } public void setIssue(Issue v) { issue = v; }
    public User getVerifier() { return verifier; } public void setVerifier(User v) { verifier = v; }
    public VerificationDecision getDecision() { return decision; } public void setDecision(VerificationDecision v) { decision = v; }
    public String getRemarks() { return remarks; } public void setRemarks(String v) { remarks = v; }
    public Instant getVerifiedAt() { return verifiedAt; }
}
