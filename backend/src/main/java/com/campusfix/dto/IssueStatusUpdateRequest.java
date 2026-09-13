package com.campusfix.dto;
import com.campusfix.entity.IssueStatus;
import jakarta.validation.constraints.*;
public record IssueStatusUpdateRequest(@NotNull IssueStatus status, @Size(max=2000) String remarks) {}
