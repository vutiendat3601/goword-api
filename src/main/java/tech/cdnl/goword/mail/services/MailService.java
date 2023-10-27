package tech.cdnl.goword.mail.services;

import tech.cdnl.goword.mail.models.MailDetail;

public interface MailService {

    void sendHtmlContentMail(MailDetail mailDetail);
}
