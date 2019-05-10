package com.project.EhrRoute.Repositories;
import com.project.EhrRoute.Entities.App.Institution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;


@Repository
public interface InstitutionRepository extends JpaRepository<Institution, Long>
{
    Optional<Institution> findByName(String name);
}
