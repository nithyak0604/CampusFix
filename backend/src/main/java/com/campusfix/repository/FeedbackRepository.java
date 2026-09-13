package com.campusfix.repository;
import com.campusfix.entity.Feedback;
import org.springframework.data.jpa.repository.JpaRepository;
public interface FeedbackRepository extends JpaRepository<Feedback, Long> { boolean existsByIssueId(Long issueId); }
