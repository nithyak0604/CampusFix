package com.campusfix.service;

import com.campusfix.entity.*;
import com.campusfix.repository.NotificationRepository;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {
    private final NotificationRepository notifications;
    public NotificationService(NotificationRepository notifications) { this.notifications = notifications; }
    public void notify(User recipient, Issue issue, NotificationType type, String message) { Notification n = new Notification(); n.setRecipient(recipient); n.setIssue(issue); n.setType(type); n.setMessage(message); notifications.save(n); }
}
