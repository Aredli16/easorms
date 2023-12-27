package fr.aredli.easorms.registration.repository;

import fr.aredli.easorms.registration.entity.SchoolYear;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SchoolYearRepository extends JpaRepository<SchoolYear, String> {
	boolean existsByStartDateAndEndDate(LocalDate startDate, LocalDate endDate);
	
	Optional<SchoolYear> findFirstByCurrentTrue();
}
