package com.campusfix.dto;
import jakarta.validation.constraints.*;
public record UserUpdateRequest(@NotBlank @Size(max=100) String name, @Size(max=30) String phone) {}
