package com.campusfix.service;

import com.campusfix.dto.*;
import com.campusfix.entity.*;
import com.campusfix.exception.BadRequestException;
import com.campusfix.repository.UserRepository;
import com.campusfix.security.JwtService;
import org.springframework.security.authentication.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository users; private final PasswordEncoder encoder; private final AuthenticationManager auth; private final JwtService jwt;
    public AuthService(UserRepository users, PasswordEncoder encoder, AuthenticationManager auth, JwtService jwt) { this.users = users; this.encoder = encoder; this.auth = auth; this.jwt = jwt; }
    public UserResponse register(RegisterRequest request) { if (users.existsByEmailIgnoreCase(request.email())) throw new BadRequestException("Email is already registered"); User u = new User(); u.setName(request.name()); u.setEmail(request.email().toLowerCase()); u.setPassword(encoder.encode(request.password())); u.setRole(request.role() == RegisterRequest.UserRole.FACULTY ? Role.FACULTY : Role.STUDENT); u.setPhone(request.phone()); return UserResponse.of(users.save(u)); }
    public LoginResponse login(LoginRequest request) { auth.authenticate(new UsernamePasswordAuthenticationToken(request.email(), request.password())); User u = users.findByEmailIgnoreCase(request.email()).orElseThrow(); return new LoginResponse(jwt.generateToken(org.springframework.security.core.userdetails.User.withUsername(u.getEmail()).password(u.getPassword()).roles(u.getRole().name()).build()), UserResponse.of(u)); }
}
