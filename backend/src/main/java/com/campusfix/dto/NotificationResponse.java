package com.campusfix.dto;
import com.campusfix.entity.*;
import java.time.Instant;
public record NotificationResponse(Long id, Long issueId, NotificationType type, String message, Instant createdAt, boolean read) { public static NotificationResponse of(Notification n) { return new NotificationResponse(n.getId(), n.getIssue() == null ? null : n.getIssue().getId(), n.getType(), n.getMessage(), n.getCreatedAt(), n.isReadFlag()); } }
