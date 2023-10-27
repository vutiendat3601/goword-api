package tech.cdnl.goword.mail.services.impl;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tech.cdnl.goword.mail.models.MailDetail;
import tech.cdnl.goword.mail.services.MailService;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendHtmlContentMail(MailDetail mailDetail) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper messageHelper = new MimeMessageHelper(message);
        try {
            messageHelper.setFrom(mailDetail.getFromAddress(), mailDetail.getSenderName());
            messageHelper.setTo(mailDetail.getToAddress());
            messageHelper.setSubject(mailDetail.getSubject());
            messageHelper.setText(mailDetail.getContent(), true);

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        mailSender.send(message);
    }
}
