package com.project.EhrRoute.Services;
import com.project.EhrRoute.Entities.Auth.User;
import com.project.EhrRoute.Entities.Auth.VerificationToken;
import com.project.EhrRoute.Repositories.VerificationTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class VerificationTokenService
{
    private VerificationTokenRepository verificationTokenRepository;

    @Autowired
    public VerificationTokenService(VerificationTokenRepository verificationTokenRepository) {
        this.verificationTokenRepository = verificationTokenRepository;
    }


    @Transactional
    public void createVerificationToken(User user, String UUID)
    {
        VerificationToken token = new VerificationToken(UUID, user);
        verificationTokenRepository.save(token);
    }


    @Transactional
    public VerificationToken getVerificationToken(String token)
    {
        return verificationTokenRepository.findByToken(token);
    }
}
