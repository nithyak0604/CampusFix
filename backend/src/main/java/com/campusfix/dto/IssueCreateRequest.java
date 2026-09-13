package com.campusfix.dto;
import com.campusfix.entity.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
public record IssueCreateRequest(@NotBlank @Size(max=200) String title, @NotBlank @Size(max=4000) String description, @NotNull IssueCategory category, @NotNull Priority priority, @NotNull @Valid LocationRequest location) {}
