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
    Boolean existsByEmail(String email);

    Optional<User> findByAddress(String address);
    Optional<User> findByAddressOrEmail(String address, String email);

    List<User> findByIdIn(List<Long> userIds);

    @Query("SELECT u.address FROM User u WHERE u.address LIKE CONCAT('%', :keyword, '%')")
    List<String> searchAddressesByAddress(@Param("keyword") String keyword);

    @Query("SELECT u.address FROM User u INNER JOIN u.roles r WHERE r.name='ROLE_PROVIDER' AND u.address LIKE CONCAT('%', :keyword, '%')")
    List<String> searchProvidersAddressesByAddress(@Param("keyword") String keyword);

    @Query("SELECT u.isFirstLogin FROM User u WHERE u.id = :userID")
    Boolean getIsUserFirstLogin(@Param("userID") Long id);

    @Query("SELECT u FROM ProviderDetails p INNER JOIN p.user u WHERE p.providerUUID = :providerUUID")
    Optional<User> findUserByProviderUUID(@Param("providerUUID") String uuid);
}
