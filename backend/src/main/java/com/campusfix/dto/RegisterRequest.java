package com.campusfix.dto;
import jakarta.validation.constraints.*;
public record RegisterRequest(@NotBlank @Size(max=100) String name, @NotBlank @Email String email, @NotBlank @Size(min=8, max=100) String password, @NotNull UserRole role, @Size(max=30) String phone) { public enum UserRole { STUDENT, FACULTY } }
