package com.campusfix.entity;

import jakarta.persistence.*;
import java.time.Instant;

@Entity @Table(name = "assignments")
public class Assignment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) private Issue issue;
    @ManyToOne(optional = false) private User maintenanceStaff;
    @ManyToOne(optional = false) private User assignedBy;
    @Column(nullable = false) private Instant assignedAt = Instant.now();
    private boolean active = true;
    public Long getId() { return id; }
    public Issue getIssue() { return issue; } public void setIssue(Issue v) { issue = v; }
    public User getMaintenanceStaff() { return maintenanceStaff; } public void setMaintenanceStaff(User v) { maintenanceStaff = v; }
    public User getAssignedBy() { return assignedBy; } public void setAssignedBy(User v) { assignedBy = v; }
    public Instant getAssignedAt() { return assignedAt; }
    public boolean isActive() { return active; } public void setActive(boolean v) { active = v; }
}
