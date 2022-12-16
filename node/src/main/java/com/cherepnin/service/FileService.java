package com.cherepnin.service;

import com.cherepnin.entity.AppDocument;
import com.cherepnin.entity.AppPhoto;
import org.telegram.telegrambots.meta.api.objects.Message;

public interface FileService {
    AppDocument processDoc(Message telegramMessage);
    AppPhoto processPhoto(Message telegramMessage);

}
