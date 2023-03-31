package com.example.junittestpractice.util;

import org.springframework.stereotype.Component;

// 추후에 Mail 클래스가 완성되면 코드를 완성하면 됨.
// MailSenderImpl로 해도 되겠다...
@Component // IoC 컨테이너에 등록
public class MailSenderAdapter implements MailSender {

//    private Mail mail;

//    public MailSenderAdapter() {
//        this.mail = new Mail();
//    }

    @Override
    public boolean send() {
//        return mail.sendMail();
        return true;
    }
}
