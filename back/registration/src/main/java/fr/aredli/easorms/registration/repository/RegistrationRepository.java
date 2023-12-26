package fr.aredli.easorms.registration.repository;

import fr.aredli.easorms.registration.entity.Registration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, String> {
}
