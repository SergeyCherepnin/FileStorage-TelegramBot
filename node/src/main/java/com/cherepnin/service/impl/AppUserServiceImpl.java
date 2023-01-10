package com.cherepnin.service.impl;

import com.cherepnin.dao.AppUserDAO;
import com.cherepnin.dto.MailParams;
import com.cherepnin.entity.AppUser;
import com.cherepnin.service.AppUserService;
import com.cherepnin.utils.CryptoTool;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

import static com.cherepnin.entity.enums.UserState.BASIC_STATE;
import static com.cherepnin.entity.enums.UserState.WAIT_FOR_EMAIL_STATE;

@Log4j
@Service
public class AppUserServiceImpl implements AppUserService {
    private final AppUserDAO appUserDAO;
    private final CryptoTool cryptoTool;

    @Value("${service.mail.uri}")
    String serviceMailUri;

    public AppUserServiceImpl(AppUserDAO appUserDAO, CryptoTool cryptoTool) {
        this.appUserDAO = appUserDAO;
        this.cryptoTool = cryptoTool;
    }

    @Override
    public String registerUser(AppUser appUser) {
        if (appUser.getIsActive()) {
            return "Вы уже зарегистрированы!";
        } else if (appUser.getEmail() != null) {
            return "На Вашу почту уже было отправлено письмо для подтверждения регистрации. "
                    + "Пожалуйста, перейдите по ссылке в этом письме";
        }
        appUser.setState(WAIT_FOR_EMAIL_STATE);
        appUserDAO.save(appUser);
        return "Пожалуйста, введите Ваш email:";
    }

    @Override
    public String setEmail(AppUser appUser, String email) {
        try {
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
        } catch (AddressException e) {
            return "Пожалуйста, введите корректный email. Для отмены команды введите /cancel";
        }
        var optional = appUserDAO.findByEmail(email);
        if (optional.isEmpty()) {
            appUser.setEmail(email);
            appUser.setState(BASIC_STATE);
            appUser = appUserDAO.save(appUser);

            var cryptoUserId = cryptoTool.hashOf(appUser.getId());
            var response = sendRequestToMailService(cryptoUserId, email);
            if (response.getStatusCode() != HttpStatus.OK) {
                var msg = String.format("Отправка письма на почту %s не удалась.", email);
                log.error(msg);
                appUser.setEmail(null);
                appUserDAO.save(appUser);
                return msg;
            }
            return "Вам на почту было отправлено письмо. "
                    + "Перейдите по ссылке в письме для подтверждения регистрации";
        } else {
            return "Этот email уже используется. Пожалуйста введите корректный email. "
                    + "Для отмены команды введите /cancel";
        }
    }

    private ResponseEntity<String> sendRequestToMailService(String cryptoUserId, String email) {
        var restTemplate = new RestTemplate();
        var headers = new HttpHeaders();

        headers.setContentType(MediaType.APPLICATION_JSON);
        var mailParams = MailParams.builder()
                .id(cryptoUserId)
                .emailTo(email)
                .build();
        var request = new HttpEntity<>(mailParams, headers);
        return  restTemplate.exchange(serviceMailUri,
                                    HttpMethod.POST,
                                    request,
                                    String.class);
    }
}
