package com.campusfix.repository;
import com.campusfix.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface NotificationRepository extends JpaRepository<Notification, Long> { List<Notification> findByRecipientIdOrderByCreatedAtDesc(Long recipientId); }
