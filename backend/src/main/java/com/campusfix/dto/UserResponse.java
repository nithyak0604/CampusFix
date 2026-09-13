package com.campusfix.dto;
import com.campusfix.entity.*;
public record UserResponse(Long id, String name, String email, Role role, String phone) { public static UserResponse of(User u) { return new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole(), u.getPhone()); } }
