package com.project.EMRChain.Events;
import com.project.EMRChain.Entities.Auth.User;
import com.project.EMRChain.Services.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class RegistrationListener implements ApplicationListener<OnRegistrationCompleteEvent>
{
    private JavaMailSender mailSender;
    private VerificationTokenService verificationTokenService;

    @Autowired
    public RegistrationListener(JavaMailSender mailSender, VerificationTokenService verificationTokenService) {
        this.mailSender = mailSender;
        this.verificationTokenService = verificationTokenService;
    }



    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event)
    {
        this.confirmRegistration(event);
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event)
    {
        User user = event.getUser();

        String verificationToken = UUID.randomUUID().toString();

        verificationTokenService.createVerificationToken(user, verificationToken); // Persist the token

        // ### Email with account verification link containing the token ### //
        String recipientAddress = user.getEmail();
        String subject = "Registration Confirmation";
        String confirmationUrl = event.getAppUrl() + "/auth/registration-confirm/" + verificationToken;
        String message = "Account Confirmation Link: " + confirmationUrl;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientAddress);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }


}
