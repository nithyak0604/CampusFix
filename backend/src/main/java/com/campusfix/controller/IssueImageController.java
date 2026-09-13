package com.campusfix.controller;

import com.campusfix.entity.IssueImage;
import com.campusfix.service.IssueImageService;
import com.campusfix.service.CurrentUserService;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.core.Authentication;

@RestController @RequestMapping("/api/issues/{issueId}/images")
public class IssueImageController {
    private final IssueImageService images; private final CurrentUserService current;
    public IssueImageController(IssueImageService images, CurrentUserService current) { this.images = images; this.current = current; }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) @PreAuthorize("hasAnyRole('STUDENT','FACULTY','MAINTENANCE','VERIFIER')") public ResponseEntity<Void> upload(@PathVariable Long issueId, @RequestParam MultipartFile file, @RequestParam(defaultValue = "ORIGINAL") IssueImage.ImageType type, Authentication authentication) { images.add(issueId, file, type, current.require(authentication)); return ResponseEntity.status(HttpStatus.CREATED).build(); }
}
