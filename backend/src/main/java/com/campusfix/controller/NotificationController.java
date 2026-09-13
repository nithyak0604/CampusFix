package com.campusfix.controller;
import com.campusfix.dto.NotificationResponse;
import com.campusfix.repository.NotificationRepository;
import com.campusfix.service.CurrentUserService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationRepository notifications; private final CurrentUserService current;
    public NotificationController(NotificationRepository notifications, CurrentUserService current) { this.notifications = notifications; this.current = current; }
    @GetMapping public List<NotificationResponse> list(Authentication a) { return notifications.findByRecipientIdOrderByCreatedAtDesc(current.require(a).getId()).stream().map(NotificationResponse::of).toList(); }
}
