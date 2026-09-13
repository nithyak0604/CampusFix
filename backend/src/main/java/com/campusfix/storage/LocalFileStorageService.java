package com.campusfix.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.nio.file.*;
import java.util.Set;
import java.util.UUID;

@Service
public class LocalFileStorageService implements FileStorageService {
    private static final Set<String> ALLOWED = Set.of("image/jpeg", "image/png", "image/webp");
    private final Path root;
    public LocalFileStorageService(@Value("${app.upload.directory:uploads}") String directory) { root = Paths.get(directory).toAbsolutePath().normalize(); try { Files.createDirectories(root); } catch (IOException e) { throw new IllegalStateException("Could not create upload directory", e); } }
    @Override public String store(MultipartFile file) throws IOException { if (file.isEmpty() || file.getSize() > 5 * 1024 * 1024 || !ALLOWED.contains(file.getContentType())) throw new IllegalArgumentException("Only non-empty JPEG, PNG, or WebP images up to 5 MB are accepted"); String extension = StringUtils.getFilenameExtension(file.getOriginalFilename()); String key = UUID.randomUUID() + "." + (extension == null ? "bin" : extension.toLowerCase()); Path target = root.resolve(key).normalize(); if (!target.getParent().equals(root)) throw new IllegalArgumentException("Invalid file name"); Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING); return key; }
}
