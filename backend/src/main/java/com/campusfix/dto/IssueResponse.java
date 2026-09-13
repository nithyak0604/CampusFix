package com.campusfix.dto;
import com.campusfix.entity.*;
import java.time.Instant;
public record IssueResponse(Long id, String title, String description, IssueCategory category, Priority priority, IssueStatus status, UserResponse reportedBy, LocationResponse location, String resolutionRemarks, Instant createdAt, Instant updatedAt, Instant resolvedAt, Instant closedAt) { public static IssueResponse of(Issue i) { return new IssueResponse(i.getId(), i.getTitle(), i.getDescription(), i.getCategory(), i.getPriority(), i.getStatus(), UserResponse.of(i.getReportedBy()), LocationResponse.of(i.getLocation()), i.getResolutionRemarks(), i.getCreatedAt(), i.getUpdatedAt(), i.getResolvedAt(), i.getClosedAt()); } }
