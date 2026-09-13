package com.campusfix.controller;
import com.campusfix.dto.IssueResponse;
import com.campusfix.entity.*;
import com.campusfix.repository.AssignmentRepository;
import com.campusfix.service.CurrentUserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/maintenance") @PreAuthorize("hasRole('MAINTENANCE')")
public class MaintenanceController {
    private final AssignmentRepository assignments; private final CurrentUserService current;
    public MaintenanceController(AssignmentRepository assignments, CurrentUserService current) { this.assignments = assignments; this.current = current; }
    @GetMapping("/issues") public List<IssueResponse> assigned(Authentication a) { return assignments.findByMaintenanceStaffIdAndActiveTrue(current.require(a).getId()).stream().map(Assignment::getIssue).map(IssueResponse::of).toList(); }
}
