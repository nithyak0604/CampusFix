package com.campusfix.security;

import com.campusfix.repository.UserRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository users;
    public CustomUserDetailsService(UserRepository users) { this.users = users; }
    @Override public UserDetails loadUserByUsername(String email) { return users.findByEmailIgnoreCase(email).map(u -> User.withUsername(u.getEmail()).password(u.getPassword()).roles(u.getRole().name()).build()).orElseThrow(() -> new UsernameNotFoundException("User not found")); }
}
