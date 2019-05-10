package com.project.EhrRoute.Repositories;
import com.project.EhrRoute.Entities.Auth.Role;
import com.project.EhrRoute.Models.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>
{
    Optional<Role> findByName(RoleName roleName);
}
