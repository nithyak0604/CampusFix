package com.campusfix.dto;
import jakarta.validation.constraints.*;
public record LocationRequest(@NotBlank String building, String block, String floor, String room, String details) {}
