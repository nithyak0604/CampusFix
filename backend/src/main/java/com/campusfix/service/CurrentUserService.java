package com.campusfix.service;

import com.campusfix.entity.User;
import com.campusfix.exception.ResourceNotFoundException;
import com.campusfix.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class CurrentUserService {
    private final UserRepository users;
    public CurrentUserService(UserRepository users) { this.users = users; }
    public User require(Authentication authentication) { return users.findByEmailIgnoreCase(authentication.getName()).orElseThrow(() -> new ResourceNotFoundException("User not found")); }
}
