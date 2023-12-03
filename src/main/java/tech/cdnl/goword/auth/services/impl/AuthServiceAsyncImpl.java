package tech.cdnl.goword.auth.services.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.ZonedDateTime;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import tech.cdnl.goword.auth.services.AuthServiceAsync;
import tech.cdnl.goword.exceptions.AppErrorMessage;
import tech.cdnl.goword.exceptions.ResourceNotFoundException;
import tech.cdnl.goword.mail.models.MailDetail;
import tech.cdnl.goword.mail.services.MailService;
import tech.cdnl.goword.shared.AppConstant;
import tech.cdnl.goword.user.models.entity.User;
import tech.cdnl.goword.user.repos.UserRepo;

@RequiredArgsConstructor
@Service
public class AuthServiceAsyncImpl implements AuthServiceAsync {
    @Value("${app.refresh_token.valid_time_sec}")
    private long refTokenValidTimeSec;

    @Value("${app.name}")
    private String appName;

    @Value("${app.website}")
    private String appWebsite;

    @Value("${spring.mail.username}")
    private String serverEmailAddress;

    @Value("${app.auth.verification_code.valid_time_sec}")
    private long verifCodeValidTimeSec;

    private final UserRepo userRepo;
    private final MailService mailService;
    private final Base64.Encoder base64Encoder;
    private final ResourceLoader resourceLoader;

    @Override
    public void sendEmailVerificationCode(String email) {
        String verificationCode = RandomString.make(AppConstant.VERIFICATION_CODE_RANDOM_LENGTH);
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException(AppErrorMessage.USER_NOT_FOUND));
        user.setVerificationCode(base64Encoder.encodeToString(verificationCode.getBytes()));
        user.setVerificationCodeExpireAt(ZonedDateTime.now().toEpochSecond() + verifCodeValidTimeSec);
        user = userRepo.save(user);

        StringBuilder contentBuilder = new StringBuilder("");
        try {
            Resource verifCodeMailTemplate = resourceLoader
                    .getResource("classpath:templates/verification-code-mail.html");
            Reader contentTemplate = new InputStreamReader(verifCodeMailTemplate.getInputStream());
            BufferedReader br = new BufferedReader(contentTemplate);
            String line = "";
            while ((line = br.readLine()) != null) {
                contentBuilder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String confirmLink = appWebsite + "/auth/email-verification?code=" + verificationCode;
        String newConfirmLink = appWebsite + "/re-verify?email=" + email;
        String content = contentBuilder.toString();
        content = content.replace("${app-name}", appName);
        content = content.replace("${app-website}", appWebsite);
        content = content.replace("${confirm-link}", confirmLink);
        content = content.replace("${new-confirm-link}", newConfirmLink);
        MailDetail mailDetail = new MailDetail(serverEmailAddress, email, appName,
                "Xác thực địa chỉ email!",
                content);
        mailService.sendHtmlContentMail(mailDetail);
    }
}
