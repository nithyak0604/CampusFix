package com.campusfix.repository;
import com.campusfix.entity.IssueStatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface IssueStatusHistoryRepository extends JpaRepository<IssueStatusHistory, Long> { List<IssueStatusHistory> findByIssueIdOrderByChangedAtAsc(Long issueId); }
