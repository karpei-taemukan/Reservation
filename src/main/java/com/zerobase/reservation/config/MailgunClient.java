package com.zerobase.reservation.config;

import com.zerobase.reservation.domain.mailgun.SendMailForm;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

// 원격 api 호출(mailgun)
@FeignClient(name = "mailgun", url = "https://api.mailgun.net/v3/")
@Qualifier("mailgun")
public interface MailgunClient {

    @PostMapping("${spring.mailgun.domain}/messages")
    ResponseEntity<String> sendEmail(@SpringQueryMap SendMailForm sendMailForm);
}
