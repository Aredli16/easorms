package fr.aredli.easorms.registration.util;

import com.github.javafaker.Faker;
import fr.aredli.easorms.registration.entity.CustomField;
import fr.aredli.easorms.registration.entity.CustomField.Type;
import fr.aredli.easorms.registration.entity.Registration;
import fr.aredli.easorms.registration.entity.SchoolYear;
import fr.aredli.easorms.registration.repository.CustomFieldRepository;
import fr.aredli.easorms.registration.repository.RegistrationRepository;
import fr.aredli.easorms.registration.repository.SchoolYearRepository;

import java.time.LocalDate;

public abstract class RegistrationUtilTest {
	public static Registration createRegistration(RegistrationRepository registrationRepository, String userId) {
		Faker faker = new Faker();
		Registration registration = new Registration();
		
		registration.setLastName(faker.name().lastName());
		registration.setFirstName(faker.name().firstName());
		registration.setEmail(faker.internet().emailAddress());
		registration.setPhone(faker.phoneNumber().phoneNumber());
		registration.setStreetAddress(faker.address().streetAddress());
		registration.setZipCode(faker.address().zipCode());
		registration.setCity(faker.address().city());
		registration.setCountry(faker.address().country());
		registration.setCreatedBy(userId);
		
		return registrationRepository.save(registration);
	}
	
	public static Registration createRegistration(RegistrationRepository registrationRepository, String userId, SchoolYear schoolYear) {
		Registration registration = createRegistration(registrationRepository, userId);
		registration.setSchoolYear(schoolYear);
		
		return registrationRepository.save(registration);
	}
	
	public static SchoolYear createSchoolYear(SchoolYearRepository schoolYearRepository, int year) {
		SchoolYear schoolYear = new SchoolYear();
		
		schoolYear.setStartDate(LocalDate.of(year, 9, 1));
		schoolYear.setEndDate(LocalDate.of(year + 1, 6, 30));
		
		return schoolYearRepository.save(schoolYear);
	}
	
	public static SchoolYear createCurrentSchoolYear(SchoolYearRepository schoolYearRepository, int year) {
		SchoolYear schoolYear = createSchoolYear(schoolYearRepository, year);
		schoolYear.setCurrent(true);
		
		return schoolYearRepository.save(schoolYear);
	}
	
	public static CustomField createCustomField(CustomFieldRepository customFieldRepository, String name, Type type) {
		CustomField customField = new CustomField();
		customField.setName(name);
		customField.setType(type);
		
		return customFieldRepository.save(customField);
	}
}
