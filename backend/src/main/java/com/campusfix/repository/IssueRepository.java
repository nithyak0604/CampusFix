package com.campusfix.repository;
import com.campusfix.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface IssueRepository extends JpaRepository<Issue, Long> { List<Issue> findByReportedById(Long userId); List<Issue> findByStatus(IssueStatus status); }
