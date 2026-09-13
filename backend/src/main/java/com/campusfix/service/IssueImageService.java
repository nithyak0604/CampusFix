package com.campusfix.service;

import com.campusfix.entity.*;
import com.campusfix.exception.*;
import com.campusfix.repository.*;
import com.campusfix.storage.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
public class IssueImageService {
    private final IssueRepository issues; private final IssueImageRepository images; private final AssignmentRepository assignments; private final FileStorageService storage;
    public IssueImageService(IssueRepository issues, IssueImageRepository images, AssignmentRepository assignments, FileStorageService storage) { this.issues = issues; this.images = images; this.assignments = assignments; this.storage = storage; }
    public IssueImage add(Long issueId, MultipartFile file, IssueImage.ImageType type, User actor) { Issue issue = issues.findById(issueId).orElseThrow(() -> new ResourceNotFoundException("Issue not found")); if (actor.getRole() == Role.STUDENT || actor.getRole() == Role.FACULTY) { if (!issue.getReportedBy().getId().equals(actor.getId())) throw new BadRequestException("You do not own this issue"); } else if (actor.getRole() == Role.MAINTENANCE && assignments.findByIssueIdAndActiveTrue(issueId).map(a -> !a.getMaintenanceStaff().getId().equals(actor.getId())).orElse(true)) throw new BadRequestException("Issue is not assigned to you"); try { IssueImage image = new IssueImage(); image.setIssue(issue); image.setStorageKey(storage.store(file)); image.setContentType(file.getContentType()); image.setType(type); return images.save(image); } catch (IOException e) { throw new BadRequestException("Could not store image"); } }
}
