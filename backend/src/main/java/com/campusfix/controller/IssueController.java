package com.campusfix.controller;

import com.campusfix.dto.*;
import com.campusfix.entity.*;
import com.campusfix.service.*;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController @RequestMapping("/api/issues")
public class IssueController {
    private final IssueService issues; private final CurrentUserService current;
    public IssueController(IssueService issues, CurrentUserService current) { this.issues = issues; this.current = current; }
    @PostMapping @PreAuthorize("hasAnyRole('STUDENT','FACULTY')") public ResponseEntity<IssueResponse> create(@Valid @RequestBody IssueCreateRequest r, Authentication a) { return ResponseEntity.status(HttpStatus.CREATED).body(issues.create(r, current.require(a))); }
    @GetMapping public List<IssueResponse> list(Authentication a) { return issues.list(current.require(a)); }
    @GetMapping("/{id}") public IssueResponse get(@PathVariable Long id, Authentication a) { return issues.get(id, current.require(a)); }
    @PutMapping("/{id}/status") public IssueResponse status(@PathVariable Long id, @Valid @RequestBody IssueStatusUpdateRequest r, Authentication a) { return issues.status(id, r, current.require(a)); }
    @PutMapping("/{id}/assign") @PreAuthorize("hasRole('ADMIN')") public IssueResponse assign(@PathVariable Long id, @Valid @RequestBody AssignmentRequest r, Authentication a) { return issues.assign(id, r.maintenanceStaffId(), current.require(a)); }
    @PutMapping("/{id}/resolve") @PreAuthorize("hasRole('MAINTENANCE')") public IssueResponse resolve(@PathVariable Long id, @Valid @RequestBody ResolveIssueRequest r, Authentication a) { return issues.resolve(id, r, current.require(a)); }
    @PutMapping("/{id}/verify") @PreAuthorize("hasRole('VERIFIER')") public IssueResponse verify(@PathVariable Long id, @Valid @RequestBody VerificationRequest r, Authentication a) { return issues.verify(id, r, current.require(a)); }
    @PutMapping("/{id}/confirm") @PreAuthorize("hasAnyRole('STUDENT','FACULTY')") public IssueResponse confirm(@PathVariable Long id, Authentication a) { return issues.confirm(id, true, null, current.require(a)); }
    @PutMapping("/{id}/reopen") @PreAuthorize("hasAnyRole('STUDENT','FACULTY')") public IssueResponse reopen(@PathVariable Long id, @Valid @RequestBody ReopenIssueRequest r, Authentication a) { return issues.confirm(id, false, r.reason(), current.require(a)); }
    @PostMapping("/{id}/feedback") @PreAuthorize("hasAnyRole('STUDENT','FACULTY')") @ResponseStatus(HttpStatus.CREATED) public void feedback(@PathVariable Long id, @Valid @RequestBody FeedbackRequest r, Authentication a) { issues.feedback(id, r, current.require(a)); }
}
