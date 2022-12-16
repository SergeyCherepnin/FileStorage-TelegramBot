package com.cherepnin.service;

import com.cherepnin.entity.AppDocument;
import com.cherepnin.entity.AppPhoto;
import com.cherepnin.entity.BinaryContent;
import org.springframework.core.io.FileSystemResource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileService {
    AppDocument getDocument(String id);
    AppPhoto getPhoto(String id);
    FileSystemResource getFileSystemResource(BinaryContent binaryContent);
}
