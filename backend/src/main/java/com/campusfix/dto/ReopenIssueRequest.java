package com.campusfix.dto;
import jakarta.validation.constraints.*;
public record ReopenIssueRequest(@NotBlank @Size(max=2000) String reason) {}
