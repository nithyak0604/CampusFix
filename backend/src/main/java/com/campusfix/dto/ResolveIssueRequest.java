package com.campusfix.dto;
import jakarta.validation.constraints.*;
public record ResolveIssueRequest(@NotBlank @Size(max=2000) String remarks) {}
