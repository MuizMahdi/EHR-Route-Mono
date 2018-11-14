package com.project.EMRChain.Events;
import com.project.EMRChain.Entities.Auth.User;
import com.project.EMRChain.Services.VerificationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;
import java.util.UUID;

@Component
public class VerificationTokenListener
{
    private JavaMailSender mailSender;
    private VerificationTokenService verificationTokenService;

    @Autowired
    public VerificationTokenListener(JavaMailSender mailSender, VerificationTokenService verificationTokenService) {
        this.mailSender = mailSender;
        this.verificationTokenService = verificationTokenService;
    }


    @EventListener
    public void onRegistrationCompleteEvent(RegistrationCompleteEvent event) {
        this.sendRegistrationToken(event);
    }

    @EventListener
    public void onRoleChangeEvent(RoleChangeEvent event) {
        this.sendRoleChangeToken(event);
    }

    private void sendRegistrationToken(RegistrationCompleteEvent event)
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

    private void sendRoleChangeToken(RoleChangeEvent event)
    {
        User user = event.getUser();
        String role = event.getRole();

        String verificationToken = UUID.randomUUID().toString();

        // Persist the token
        verificationTokenService.createVerificationToken(user, verificationToken);

        // Email with link containing the token
        String recipientAddress = user.getEmail();
        String subject = "Role Change Confirmation";
        String confirmationUrl = event.getAppUrl() + "/auth/role-change/" + role + "/" + verificationToken;
        String message = "Role Change Confirmation Link: " + confirmationUrl;

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(recipientAddress);
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        mailSender.send(mailMessage);
    }

}
