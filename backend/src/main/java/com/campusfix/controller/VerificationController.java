package com.campusfix.controller;
import com.campusfix.dto.IssueResponse;
import com.campusfix.entity.IssueStatus;
import com.campusfix.repository.IssueRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController @RequestMapping("/api/verification") @PreAuthorize("hasRole('VERIFIER')")
public class VerificationController {
    private final IssueRepository issues; public VerificationController(IssueRepository issues) { this.issues = issues; }
    @GetMapping("/pending") public List<IssueResponse> pending() { return issues.findByStatus(IssueStatus.VERIFICATION_PENDING).stream().map(IssueResponse::of).toList(); }
}
