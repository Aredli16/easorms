package fr.aredli.easorms.registration.controller;

import com.github.javafaker.Faker;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationPageResponse;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationRequest.RegistrationCreateRequest;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationRequest.RegistrationUpdateRequest;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationResponse;
import fr.aredli.easorms.registration.entity.Registration;
import fr.aredli.easorms.registration.entity.Registration.Status;
import fr.aredli.easorms.registration.entity.SchoolYear;
import fr.aredli.easorms.registration.exception.ErrorHandler;
import fr.aredli.easorms.registration.repository.RegistrationRepository;
import fr.aredli.easorms.registration.repository.SchoolYearRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegistrationControllerTest extends DatabaseIntegrationTest {
	@Autowired
	private RegistrationRepository registrationRepository;
	@Autowired
	private SchoolYearRepository schoolYearRepository;
	
	private Registration createRegistration() {
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
		registration.setCreatedBy("test");
		
		return registrationRepository.save(registration);
	}
	
	private Registration createRegistration(SchoolYear schoolYear) {
		Registration registration = createRegistration();
		registration.setSchoolYear(schoolYear);
		
		return registrationRepository.save(registration);
	}
	
	private SchoolYear createCurrentSchoolYear(int year) {
		SchoolYear schoolYear = new SchoolYear();
		schoolYear.setStartDate(LocalDate.of(year, 9, 1));
		schoolYear.setEndDate(LocalDate.of(year + 1, 7, 6));
		schoolYear.setCurrent(true);
		
		return schoolYearRepository.save(schoolYear);
	}
	
	@AfterEach
	void tearDown() {
		registrationRepository.deleteAll();
		schoolYearRepository.deleteAll();
	}
	
	@Test
	void shouldGetPageOfAllRegistration() {
		Registration firstRegistration = createRegistration();
		Registration secondRegistration = createRegistration();
		Registration thirdRegistration = createRegistration();
		
		ResponseEntity<RegistrationPageResponse> pageResponse = restTemplate.getForEntity("/registration?page=0&size=10&sortBy=createdAt&sortDirection=asc", RegistrationPageResponse.class);
		
		assertEquals(200, pageResponse.getStatusCode().value());
		assertNotNull(pageResponse.getBody());
		assertEquals(3, pageResponse.getBody().getTotalElements());
		assertEquals(1, pageResponse.getBody().getTotalPages());
		assertNotNull(pageResponse.getBody().getRegistrations());
		assertEquals(3, pageResponse.getBody().getRegistrations().size());
		assertEquals(firstRegistration.getId(), pageResponse.getBody().getRegistrations().getFirst().getId());
		assertEquals(secondRegistration.getId(), pageResponse.getBody().getRegistrations().get(1).getId());
		assertEquals(thirdRegistration.getId(), pageResponse.getBody().getRegistrations().getLast().getId());
	}
	
	@Test
	void shouldGetCorrectRegistration() {
		Registration registration = createRegistration();
		
		ResponseEntity<RegistrationResponse> registrationResponse = restTemplate.getForEntity("/registration/" + registration.getId(), RegistrationResponse.class);
		
		assertEquals(200, registrationResponse.getStatusCode().value());
		assertNotNull(registrationResponse.getBody());
		assertEquals(registration.getId(), registrationResponse.getBody().getId());
		assertEquals(registration.getLastName(), registrationResponse.getBody().getLastName());
		assertEquals(registration.getFirstName(), registrationResponse.getBody().getFirstName());
		assertEquals(registration.getEmail(), registrationResponse.getBody().getEmail());
		assertEquals(registration.getPhone(), registrationResponse.getBody().getPhone());
		assertEquals(registration.getStreetAddress(), registrationResponse.getBody().getStreetAddress());
		assertEquals(registration.getZipCode(), registrationResponse.getBody().getZipCode());
		assertEquals(registration.getCity(), registrationResponse.getBody().getCity());
		assertEquals(registration.getCountry(), registrationResponse.getBody().getCountry());
		assertEquals(registration.getCreatedBy(), registrationResponse.getBody().getCreatedBy());
		assertEquals(registration.getCreatedAt(), registrationResponse.getBody().getCreatedAt());
		assertEquals(registration.getUpdatedAt(), registrationResponse.getBody().getUpdatedAt());
	}
	
	@Test
	void shouldThrowExceptionIfTheValueIsNotPresent() {
		ResponseEntity<ErrorHandler> response = restTemplate.getForEntity("/registration/1", ErrorHandler.class);
		
		assertEquals(404, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getTimestamp());
		assertEquals("No value present", response.getBody().getMessage());
		assertEquals("The requested resource was not found.", response.getBody().getDetails());
		assertEquals(404, response.getBody().getStatusCode());
		assertEquals("NOT_FOUND", response.getBody().getStatus().name());
	}
	
	@Test
	void shouldCreateRegistration() {
		createCurrentSchoolYear(2000);
		
		RegistrationCreateRequest registrationCreateRequest = new RegistrationCreateRequest();
		Faker faker = new Faker();
		
		registrationCreateRequest.setLastName(faker.name().lastName());
		registrationCreateRequest.setFirstName(faker.name().firstName());
		registrationCreateRequest.setEmail(faker.internet().emailAddress());
		registrationCreateRequest.setPhone(faker.phoneNumber().phoneNumber());
		registrationCreateRequest.setStreetAddress(faker.address().streetAddress());
		registrationCreateRequest.setZipCode(faker.address().zipCode());
		registrationCreateRequest.setCity(faker.address().city());
		registrationCreateRequest.setCountry(faker.address().country());
		
		ResponseEntity<RegistrationResponse> registrationResponse = restTemplate.postForEntity("/registration", registrationCreateRequest, RegistrationResponse.class);
		
		assertEquals(201, registrationResponse.getStatusCode().value());
		assertNotNull(registrationResponse.getBody());
		assertNotNull(registrationResponse.getBody().getId());
		assertEquals(registrationCreateRequest.getLastName(), registrationResponse.getBody().getLastName());
		assertEquals(registrationCreateRequest.getFirstName(), registrationResponse.getBody().getFirstName());
		assertEquals(registrationCreateRequest.getEmail(), registrationResponse.getBody().getEmail());
		assertEquals(registrationCreateRequest.getPhone(), registrationResponse.getBody().getPhone());
		assertEquals(registrationCreateRequest.getStreetAddress(), registrationResponse.getBody().getStreetAddress());
		assertEquals(registrationCreateRequest.getZipCode(), registrationResponse.getBody().getZipCode());
		assertEquals(registrationCreateRequest.getCity(), registrationResponse.getBody().getCity());
		assertEquals(registrationCreateRequest.getCountry(), registrationResponse.getBody().getCountry());
		// TODO with auth: assertNotNull(registrationResponse.getBody().getCreatedBy());
		assertNotNull(registrationResponse.getBody().getCreatedAt());
		assertNotNull(registrationResponse.getBody().getUpdatedAt());
		
		Registration registration = registrationRepository.findById(registrationResponse.getBody().getId()).orElseThrow();
		
		assertEquals(registrationCreateRequest.getLastName(), registration.getLastName());
		assertEquals(registrationCreateRequest.getFirstName(), registration.getFirstName());
		assertEquals(registrationCreateRequest.getEmail(), registration.getEmail());
		assertEquals(registrationCreateRequest.getPhone(), registration.getPhone());
		assertEquals(registrationCreateRequest.getStreetAddress(), registration.getStreetAddress());
		assertEquals(registrationCreateRequest.getZipCode(), registration.getZipCode());
		assertEquals(registrationCreateRequest.getCity(), registration.getCity());
		assertEquals(registrationCreateRequest.getCountry(), registration.getCountry());
		assertEquals(registrationResponse.getBody().getCreatedBy(), registration.getCreatedBy());
		assertEquals(registrationResponse.getBody().getCreatedAt(), registration.getCreatedAt());
		assertEquals(registrationResponse.getBody().getUpdatedAt(), registration.getUpdatedAt());
	}
	
	@Test
	void shouldUpdateAnExistingRegistration() {
		Registration registration = createRegistration();
		RegistrationUpdateRequest registrationUpdateRequest = new RegistrationUpdateRequest();
		Faker faker = new Faker();
		
		registrationUpdateRequest.setLastName(faker.name().lastName());
		registrationUpdateRequest.setFirstName(faker.name().firstName());
		registrationUpdateRequest.setEmail(faker.internet().emailAddress());
		registrationUpdateRequest.setPhone(faker.phoneNumber().phoneNumber());
		registrationUpdateRequest.setStreetAddress(faker.address().streetAddress());
		registrationUpdateRequest.setZipCode(faker.address().zipCode());
		registrationUpdateRequest.setCity(faker.address().city());
		registrationUpdateRequest.setCountry(faker.address().country());
		
		ResponseEntity<RegistrationResponse> registrationResponse = restTemplate.exchange("/registration/" + registration.getId(), HttpMethod.PUT, new HttpEntity<>(registrationUpdateRequest), RegistrationResponse.class);
		
		assertEquals(200, registrationResponse.getStatusCode().value());
		assertNotNull(registrationResponse.getBody());
		assertEquals(registration.getId(), registrationResponse.getBody().getId());
		assertEquals(registrationUpdateRequest.getLastName(), registrationResponse.getBody().getLastName());
		assertEquals(registrationUpdateRequest.getFirstName(), registrationResponse.getBody().getFirstName());
		assertEquals(registrationUpdateRequest.getEmail(), registrationResponse.getBody().getEmail());
		assertEquals(registrationUpdateRequest.getPhone(), registrationResponse.getBody().getPhone());
		assertEquals(registrationUpdateRequest.getStreetAddress(), registrationResponse.getBody().getStreetAddress());
		assertEquals(registrationUpdateRequest.getZipCode(), registrationResponse.getBody().getZipCode());
		assertEquals(registrationUpdateRequest.getCity(), registrationResponse.getBody().getCity());
		assertEquals(registrationUpdateRequest.getCountry(), registrationResponse.getBody().getCountry());
		assertEquals(registration.getCreatedBy(), registrationResponse.getBody().getCreatedBy());
		assertEquals(registration.getCreatedAt(), registrationResponse.getBody().getCreatedAt());
		assertNotNull(registrationResponse.getBody().getUpdatedAt());
		
		Registration updatedRegistration = registrationRepository.findById(registrationResponse.getBody().getId()).orElseThrow();
		
		assertEquals(registrationUpdateRequest.getLastName(), updatedRegistration.getLastName());
		assertEquals(registrationUpdateRequest.getFirstName(), updatedRegistration.getFirstName());
		assertEquals(registrationUpdateRequest.getEmail(), updatedRegistration.getEmail());
		assertEquals(registrationUpdateRequest.getPhone(), updatedRegistration.getPhone());
		assertEquals(registrationUpdateRequest.getStreetAddress(), updatedRegistration.getStreetAddress());
		assertEquals(registrationUpdateRequest.getZipCode(), updatedRegistration.getZipCode());
		assertEquals(registrationUpdateRequest.getCity(), updatedRegistration.getCity());
		assertEquals(registrationUpdateRequest.getCountry(), updatedRegistration.getCountry());
		assertEquals(registration.getCreatedBy(), updatedRegistration.getCreatedBy());
		assertEquals(registration.getCreatedAt(), updatedRegistration.getCreatedAt());
		assertEquals(registrationResponse.getBody().getUpdatedAt(), updatedRegistration.getUpdatedAt());
	}
	
	@Test
	void shouldDeleteCorrectRegistration() {
		Registration registration = createRegistration();
		
		ResponseEntity<Void> registrationResponse = restTemplate.exchange("/registration/" + registration.getId(), HttpMethod.DELETE, null, Void.class);
		
		assertEquals(204, registrationResponse.getStatusCode().value());
		assertEquals(0, registrationRepository.count());
	}
	
	@Test
	void shouldDeleteAllRegistration() {
		for (int i = 0; i < 10; i++) {
			createRegistration();
		}
		
		ResponseEntity<Void> registrationResponse = restTemplate.exchange("/registration", HttpMethod.DELETE, null, Void.class);
		
		assertEquals(204, registrationResponse.getStatusCode().value());
		assertEquals(0, registrationRepository.count());
	}
	
	@Test
	void shouldCreateAPendingRegistrationWhenCreateANewRegistration() {
		createCurrentSchoolYear(2000);
		
		RegistrationCreateRequest registrationCreateRequest = new RegistrationCreateRequest();
		
		ResponseEntity<RegistrationResponse> registrationResponse = restTemplate.postForEntity("/registration", registrationCreateRequest, RegistrationResponse.class);
		
		assertEquals(201, registrationResponse.getStatusCode().value());
		assertNotNull(registrationResponse.getBody());
		assertEquals(Status.PENDING, registrationResponse.getBody().getStatus());
	}
	
	@Test
	void shouldNotUpdateRegistrationStatusWhenUpdate() {
		Registration registration = createRegistration();
		registration.setStatus(Status.REJECTED);
		registrationRepository.save(registration);
		
		RegistrationUpdateRequest registrationUpdateRequest = new RegistrationUpdateRequest();
		
		ResponseEntity<RegistrationResponse> registrationResponse = restTemplate.exchange("/registration/" + registration.getId(), HttpMethod.PUT, new HttpEntity<>(registrationUpdateRequest), RegistrationResponse.class);
		
		assertEquals(200, registrationResponse.getStatusCode().value());
		assertNotNull(registrationResponse.getBody());
		assertEquals(Status.REJECTED, registrationResponse.getBody().getStatus());
		
		Registration updatedRegistration = registrationRepository.findById(registrationResponse.getBody().getId()).orElseThrow();
		
		assertEquals(Status.REJECTED, updatedRegistration.getStatus());
	}
	
	@Test
	void shouldApproveExistingRegistration() {
		Registration registration = createRegistration();
		
		ResponseEntity<RegistrationResponse> registrationResponse = restTemplate.postForEntity("/registration/" + registration.getId() + "/approve", null, RegistrationResponse.class);
		
		assertEquals(200, registrationResponse.getStatusCode().value());
		assertNotNull(registrationResponse.getBody());
		assertEquals(Status.APPROVED, registrationResponse.getBody().getStatus());
		
		Registration updatedRegistration = registrationRepository.findById(registrationResponse.getBody().getId()).orElseThrow();
		
		assertEquals(Status.APPROVED, updatedRegistration.getStatus());
	}
	
	@Test
	void shouldRejectExistingRegistration() {
		Registration registration = createRegistration();
		
		ResponseEntity<RegistrationResponse> registrationResponse = restTemplate.postForEntity("/registration/" + registration.getId() + "/reject", null, RegistrationResponse.class);
		
		assertEquals(200, registrationResponse.getStatusCode().value());
		assertNotNull(registrationResponse.getBody());
		assertEquals(Status.REJECTED, registrationResponse.getBody().getStatus());
		
		Registration updatedRegistration = registrationRepository.findById(registrationResponse.getBody().getId()).orElseThrow();
		
		assertEquals(Status.REJECTED, updatedRegistration.getStatus());
	}
	
	@Test
	void shouldGetPageOfAllRegistrationByStatus() {
		Registration firstRegistration = createRegistration();
		Registration secondRegistration = createRegistration();
		Registration thirdRegistration = createRegistration();
		
		ResponseEntity<RegistrationPageResponse> pageResponse = restTemplate.getForEntity("/registration/status/" + Status.PENDING, RegistrationPageResponse.class);
		
		assertEquals(200, pageResponse.getStatusCode().value());
		assertNotNull(pageResponse.getBody());
		assertEquals(3, pageResponse.getBody().getTotalElements());
		assertEquals(1, pageResponse.getBody().getTotalPages());
		
		restTemplate.postForEntity("/registration/" + firstRegistration.getId() + "/approve", null, RegistrationResponse.class);
		restTemplate.postForEntity("/registration/" + secondRegistration.getId() + "/reject", null, RegistrationResponse.class);
		
		pageResponse = restTemplate.getForEntity("/registration/status/" + Status.PENDING, RegistrationPageResponse.class);
		
		assertEquals(200, pageResponse.getStatusCode().value());
		assertNotNull(pageResponse.getBody());
		assertEquals(1, pageResponse.getBody().getTotalElements());
		assertEquals(1, pageResponse.getBody().getTotalPages());
		assertNotNull(pageResponse.getBody().getRegistrations());
		assertEquals(1, pageResponse.getBody().getRegistrations().size());
		assertEquals(thirdRegistration.getId(), pageResponse.getBody().getRegistrations().getFirst().getId());
		
		pageResponse = restTemplate.getForEntity("/registration/status/" + Status.APPROVED, RegistrationPageResponse.class);
		
		assertEquals(200, pageResponse.getStatusCode().value());
		assertNotNull(pageResponse.getBody());
		assertEquals(1, pageResponse.getBody().getTotalElements());
		assertEquals(1, pageResponse.getBody().getTotalPages());
		assertNotNull(pageResponse.getBody().getRegistrations());
		assertEquals(1, pageResponse.getBody().getRegistrations().size());
		assertEquals(firstRegistration.getId(), pageResponse.getBody().getRegistrations().getFirst().getId());
		
		pageResponse = restTemplate.getForEntity("/registration/status/" + Status.REJECTED, RegistrationPageResponse.class);
		
		assertEquals(200, pageResponse.getStatusCode().value());
		assertNotNull(pageResponse.getBody());
		assertEquals(1, pageResponse.getBody().getTotalElements());
		assertEquals(1, pageResponse.getBody().getTotalPages());
		assertNotNull(pageResponse.getBody().getRegistrations());
		assertEquals(1, pageResponse.getBody().getRegistrations().size());
		assertEquals(secondRegistration.getId(), pageResponse.getBody().getRegistrations().getFirst().getId());
	}
	
	@Test
	void shouldThrowAndExceptionIfNoAValidStatus() {
		ResponseEntity<ErrorHandler> response = restTemplate.getForEntity("/registration/status/INVALID", ErrorHandler.class);
		
		assertEquals(400, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getTimestamp());
		assertEquals("No enum constant fr.aredli.easorms.registration.entity.Registration.Status.INVALID", response.getBody().getMessage());
		assertEquals("The request is invalid.", response.getBody().getDetails());
		assertEquals(400, response.getBody().getStatusCode());
		assertEquals("BAD_REQUEST", response.getBody().getStatus().name());
	}
	
	@Test
	void shouldGetRegistrationPageOfCurrentSchoolYear() {
		SchoolYear schoolYear = createCurrentSchoolYear(2000);
		Registration firstRegistration = createRegistration(schoolYear);
		Registration secondRegistration = createRegistration(schoolYear);
		
		ResponseEntity<RegistrationPageResponse> pageResponse = restTemplate.getForEntity("/registration/school-year/current", RegistrationPageResponse.class);
		
		assertEquals(200, pageResponse.getStatusCode().value());
		assertNotNull(pageResponse.getBody());
		assertEquals(2, pageResponse.getBody().getTotalElements());
		assertEquals(1, pageResponse.getBody().getTotalPages());
		assertNotNull(pageResponse.getBody().getRegistrations());
		assertEquals(2, pageResponse.getBody().getRegistrations().size());
		assertEquals(secondRegistration.getId(), pageResponse.getBody().getRegistrations().getFirst().getId());
		assertEquals(firstRegistration.getId(), pageResponse.getBody().getRegistrations().getLast().getId());
		
		schoolYear.setCurrent(false);
		schoolYearRepository.save(schoolYear);
		schoolYear = createCurrentSchoolYear(2002);
		Registration thirdRegistration = createRegistration(schoolYear);
		
		pageResponse = restTemplate.getForEntity("/registration/school-year/current", RegistrationPageResponse.class);
		
		assertEquals(200, pageResponse.getStatusCode().value());
		assertNotNull(pageResponse.getBody());
		assertEquals(1, pageResponse.getBody().getTotalElements());
		assertEquals(1, pageResponse.getBody().getTotalPages());
		assertNotNull(pageResponse.getBody().getRegistrations());
		assertEquals(1, pageResponse.getBody().getRegistrations().size());
		assertEquals(thirdRegistration.getId(), pageResponse.getBody().getRegistrations().getFirst().getId());
	}
	
	@Test
	void shouldThrowExceptionWhenCreatingRegistrationWithoutCurrentSchoolYear() {
		RegistrationCreateRequest registrationCreateRequest = new RegistrationCreateRequest();
		
		ResponseEntity<ErrorHandler> response = restTemplate.postForEntity("/registration", registrationCreateRequest, ErrorHandler.class);
		
		assertEquals(400, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getTimestamp());
		assertEquals("No current school year found", response.getBody().getMessage());
		assertEquals("The request is invalid.", response.getBody().getDetails());
		assertEquals(400, response.getBody().getStatusCode());
		assertEquals("BAD_REQUEST", response.getBody().getStatus().name());
	}
}
