package fr.aredli.easorms.registration.repository;

import fr.aredli.easorms.registration.entity.CustomField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomFieldRepository extends JpaRepository<CustomField, String> {
	Optional<CustomField> findByName(String name);
}
