package com.project.EhrRoute.Repositories;
import com.project.EhrRoute.Entities.Auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>
{
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);

    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);

    List<User> findByIdIn(List<Long> userIds);

    @Query("SELECT u.username FROM User u WHERE u.username LIKE CONCAT('%', :keyword, '%')")
    List<String> searchUsernamesByUsername(@Param("keyword") String keyword);
}
