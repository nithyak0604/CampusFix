package com.campusfix.dto;
import com.campusfix.entity.VerificationDecision;
import jakarta.validation.constraints.*;
public record VerificationRequest(@NotNull VerificationDecision decision, @NotBlank @Size(max=2000) String remarks) {}
