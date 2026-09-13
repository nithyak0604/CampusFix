package com.campusfix.dto;
import jakarta.validation.constraints.NotNull;
public record AssignmentRequest(@NotNull Long maintenanceStaffId) {}
