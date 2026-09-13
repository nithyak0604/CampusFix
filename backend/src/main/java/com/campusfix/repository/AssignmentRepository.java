package com.campusfix.repository;
import com.campusfix.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;
public interface AssignmentRepository extends JpaRepository<Assignment, Long> { Optional<Assignment> findByIssueIdAndActiveTrue(Long issueId); List<Assignment> findByMaintenanceStaffIdAndActiveTrue(Long userId); }
