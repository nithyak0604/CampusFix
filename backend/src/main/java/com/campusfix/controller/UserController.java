package com.campusfix.controller;

import com.campusfix.dto.*;
import com.campusfix.entity.User;
import com.campusfix.exception.ResourceNotFoundException;
import com.campusfix.repository.UserRepository;
import com.campusfix.service.CurrentUserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController @RequestMapping("/api/users")
public class UserController {
    private final UserRepository users; private final CurrentUserService current;
    public UserController(UserRepository users, CurrentUserService current) { this.users = users; this.current = current; }
    @GetMapping("/{id}") public UserResponse get(@PathVariable Long id, Authentication authentication) { User actor = current.require(authentication); if (!actor.getId().equals(id) && actor.getRole() != com.campusfix.entity.Role.ADMIN) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN); return UserResponse.of(users.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found"))); }
    @PutMapping("/{id}") public UserResponse update(@PathVariable Long id, @Valid @RequestBody UserUpdateRequest request, Authentication authentication) { User actor = current.require(authentication); if (!actor.getId().equals(id) && actor.getRole() != com.campusfix.entity.Role.ADMIN) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.FORBIDDEN); User user = users.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found")); user.setName(request.name()); user.setPhone(request.phone()); return UserResponse.of(users.save(user)); }
}
