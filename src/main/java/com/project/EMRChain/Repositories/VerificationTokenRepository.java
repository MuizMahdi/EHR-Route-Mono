package com.project.EMRChain.Repositories;
import com.project.EMRChain.Entities.Auth.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long>
{
    VerificationToken findByToken(String token);
}
