package com.cherepnin.service;

import com.cherepnin.entity.AppDocument;
import com.cherepnin.entity.AppPhoto;
import com.cherepnin.service.enums.LinkType;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppDocument processDoc(Message telegramMessage);
    AppPhoto processPhoto(Message telegramMessage);
    String generateLink(Long docId, LinkType linkType);

}
