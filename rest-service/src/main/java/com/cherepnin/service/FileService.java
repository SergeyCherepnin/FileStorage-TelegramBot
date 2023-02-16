package com.cherepnin.service;

import com.cherepnin.entity.AppDocument;
import com.cherepnin.entity.AppPhoto;

public interface FileService {
    AppDocument getDocument(String id);
    AppPhoto getPhoto(String id);
}
