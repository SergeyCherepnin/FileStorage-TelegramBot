package com.cherepnin.service;

import com.cherepnin.dto.MailParams;

public interface MailSenderService {
    void send(MailParams mailParams);
}
