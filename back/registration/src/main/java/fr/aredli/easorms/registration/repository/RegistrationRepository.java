package fr.aredli.easorms.registration.repository;

import fr.aredli.easorms.registration.entity.Registration;
import fr.aredli.easorms.registration.entity.Registration.Status;
import fr.aredli.easorms.registration.entity.SchoolYear;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RegistrationRepository extends JpaRepository<Registration, String> {
	Page<Registration> findByStatus(Status status, Pageable pageable);
	
	Page<Registration> findBySchoolYear(SchoolYear schoolYear, Pageable pageable);
}
