package fr.aredli.easorms.registration.controller;

import com.github.javafaker.Faker;
import fr.aredli.easorms.registration.IntegrationTest;
import fr.aredli.easorms.registration.dto.RegistrationCustomFieldDTO;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationPageResponse;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationRequest.RegistrationCreateRequest;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationRequest.RegistrationUpdateRequest;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationResponse;
import fr.aredli.easorms.registration.entity.CustomField;
import fr.aredli.easorms.registration.entity.CustomField.Type;
import fr.aredli.easorms.registration.entity.Registration;
import fr.aredli.easorms.registration.entity.Registration.Status;
import fr.aredli.easorms.registration.entity.RegistrationCustomField;
import fr.aredli.easorms.registration.entity.SchoolYear;
import fr.aredli.easorms.registration.exception.ErrorHandler;
import fr.aredli.easorms.registration.repository.CustomFieldRepository;
import fr.aredli.easorms.registration.repository.RegistrationRepository;
import fr.aredli.easorms.registration.repository.SchoolYearRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static fr.aredli.easorms.registration.util.RegistrationUtilTest.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class RegistrationControllerTest extends IntegrationTest {
	@Autowired
	private RegistrationRepository registrationRepository;
	@Autowired
	private SchoolYearRepository schoolYearRepository;
	@Autowired
	private CustomFieldRepository customFieldRepository;
	
	@AfterEach
	void tearDown() {
		registrationRepository.deleteAll();
		schoolYearRepository.deleteAll();
		customFieldRepository.deleteAll();
	}
	
	@Test
	void shouldGetPageOfAllRegistration() {
		Registration firstRegistration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID);
		Registration secondRegistration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID);
		Registration thirdRegistration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID);
		
		ResponseEntity<RegistrationPageResponse> pageResponse = getWithAdminAuth("/registration?page=0&size=10&sortBy=createdAt&sortDirection=asc", RegistrationPageResponse.class);
		
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
		Registration registration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID);
		
		ResponseEntity<RegistrationResponse> registrationResponse = getWithAdminAuth("/registration/" + registration.getId(), RegistrationResponse.class);
		
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
		ResponseEntity<ErrorHandler> response = getWithAdminAuth("/registration/1", ErrorHandler.class);
		
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
		createCurrentSchoolYear(schoolYearRepository, 2000);
		
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
		
		ResponseEntity<RegistrationResponse> registrationResponse = postWithAdminAuth("/registration", registrationCreateRequest, RegistrationResponse.class);
		
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
		assertEquals(KEYCLOAK_CONTAINER_ADMIN_USERID, registrationResponse.getBody().getCreatedBy());
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
		Registration registration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID);
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
		
		ResponseEntity<RegistrationResponse> registrationResponse = putWithAdminAuth("/registration/" + registration.getId(), registrationUpdateRequest, RegistrationResponse.class);
		
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
		Registration registration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID);
		
		ResponseEntity<Void> registrationResponse = deleteWithAdminAuth("/registration/" + registration.getId(), Void.class);
		
		assertEquals(204, registrationResponse.getStatusCode().value());
		assertEquals(0, registrationRepository.count());
	}
	
	@Test
	void shouldDeleteAllRegistration() {
		for (int i = 0; i < 10; i++) {
			createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID);
		}
		
		ResponseEntity<Void> registrationResponse = deleteWithAdminAuth("/registration", Void.class);
		
		assertEquals(204, registrationResponse.getStatusCode().value());
		assertEquals(0, registrationRepository.count());
	}
	
	@Test
	void shouldCreateAPendingRegistrationWhenCreateANewRegistration() {
		createCurrentSchoolYear(schoolYearRepository, 2000);
		
		RegistrationCreateRequest registrationCreateRequest = new RegistrationCreateRequest();
		
		ResponseEntity<RegistrationResponse> registrationResponse = postWithAdminAuth("/registration", registrationCreateRequest, RegistrationResponse.class);
		
		assertEquals(201, registrationResponse.getStatusCode().value());
		assertNotNull(registrationResponse.getBody());
		assertEquals(Status.PENDING, registrationResponse.getBody().getStatus());
	}
	
	@Test
	void shouldNotUpdateRegistrationStatusWhenUpdate() {
		Registration registration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID);
		registration.setStatus(Status.REJECTED);
		registrationRepository.save(registration);
		
		RegistrationUpdateRequest registrationUpdateRequest = new RegistrationUpdateRequest();
		
		ResponseEntity<RegistrationResponse> registrationResponse = putWithAdminAuth("/registration/" + registration.getId(), registrationUpdateRequest, RegistrationResponse.class);
		
		assertEquals(200, registrationResponse.getStatusCode().value());
		assertNotNull(registrationResponse.getBody());
		assertEquals(Status.REJECTED, registrationResponse.getBody().getStatus());
		
		Registration updatedRegistration = registrationRepository.findById(registrationResponse.getBody().getId()).orElseThrow();
		
		assertEquals(Status.REJECTED, updatedRegistration.getStatus());
	}
	
	@Test
	void shouldApproveExistingRegistration() {
		Registration registration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID);
		
		ResponseEntity<RegistrationResponse> registrationResponse = postWithAdminAuth("/registration/" + registration.getId() + "/approve", null, RegistrationResponse.class);
		
		assertEquals(200, registrationResponse.getStatusCode().value());
		assertNotNull(registrationResponse.getBody());
		assertEquals(Status.APPROVED, registrationResponse.getBody().getStatus());
		
		Registration updatedRegistration = registrationRepository.findById(registrationResponse.getBody().getId()).orElseThrow();
		
		assertEquals(Status.APPROVED, updatedRegistration.getStatus());
	}
	
	@Test
	void shouldRejectExistingRegistration() {
		Registration registration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID);
		
		ResponseEntity<RegistrationResponse> registrationResponse = postWithAdminAuth("/registration/" + registration.getId() + "/reject", null, RegistrationResponse.class);
		
		assertEquals(200, registrationResponse.getStatusCode().value());
		assertNotNull(registrationResponse.getBody());
		assertEquals(Status.REJECTED, registrationResponse.getBody().getStatus());
		
		Registration updatedRegistration = registrationRepository.findById(registrationResponse.getBody().getId()).orElseThrow();
		
		assertEquals(Status.REJECTED, updatedRegistration.getStatus());
	}
	
	@Test
	void shouldGetPageOfAllRegistrationByStatus() {
		Registration firstRegistration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID);
		Registration secondRegistration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID);
		Registration thirdRegistration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID);
		
		ResponseEntity<RegistrationPageResponse> pageResponse = getWithAdminAuth("/registration/status/" + Status.PENDING, RegistrationPageResponse.class);
		
		assertEquals(200, pageResponse.getStatusCode().value());
		assertNotNull(pageResponse.getBody());
		assertEquals(3, pageResponse.getBody().getTotalElements());
		assertEquals(1, pageResponse.getBody().getTotalPages());
		
		postWithAdminAuth("/registration/" + firstRegistration.getId() + "/approve", null, RegistrationResponse.class);
		postWithAdminAuth("/registration/" + secondRegistration.getId() + "/reject", null, RegistrationResponse.class);
		
		pageResponse = getWithAdminAuth("/registration/status/" + Status.PENDING, RegistrationPageResponse.class);
		
		assertEquals(200, pageResponse.getStatusCode().value());
		assertNotNull(pageResponse.getBody());
		assertEquals(1, pageResponse.getBody().getTotalElements());
		assertEquals(1, pageResponse.getBody().getTotalPages());
		assertNotNull(pageResponse.getBody().getRegistrations());
		assertEquals(1, pageResponse.getBody().getRegistrations().size());
		assertEquals(thirdRegistration.getId(), pageResponse.getBody().getRegistrations().getFirst().getId());
		
		pageResponse = getWithAdminAuth("/registration/status/" + Status.APPROVED, RegistrationPageResponse.class);
		
		assertEquals(200, pageResponse.getStatusCode().value());
		assertNotNull(pageResponse.getBody());
		assertEquals(1, pageResponse.getBody().getTotalElements());
		assertEquals(1, pageResponse.getBody().getTotalPages());
		assertNotNull(pageResponse.getBody().getRegistrations());
		assertEquals(1, pageResponse.getBody().getRegistrations().size());
		assertEquals(firstRegistration.getId(), pageResponse.getBody().getRegistrations().getFirst().getId());
		
		pageResponse = getWithAdminAuth("/registration/status/" + Status.REJECTED, RegistrationPageResponse.class);
		
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
		ResponseEntity<ErrorHandler> response = getWithAdminAuth("/registration/status/INVALID", ErrorHandler.class);
		
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
		SchoolYear schoolYear = createCurrentSchoolYear(schoolYearRepository, 2000);
		Registration firstRegistration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID, schoolYear);
		Registration secondRegistration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID, schoolYear);
		
		ResponseEntity<RegistrationPageResponse> pageResponse = getWithAdminAuth("/registration/school-year/current", RegistrationPageResponse.class);
		
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
		schoolYear = createCurrentSchoolYear(schoolYearRepository, 2002);
		Registration thirdRegistration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID, schoolYear);
		
		pageResponse = getWithAdminAuth("/registration/school-year/current", RegistrationPageResponse.class);
		
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
		
		ResponseEntity<ErrorHandler> response = postWithAdminAuth("/registration", registrationCreateRequest, ErrorHandler.class);
		
		assertEquals(400, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getTimestamp());
		assertEquals("No current school year found", response.getBody().getMessage());
		assertEquals("The request is invalid.", response.getBody().getDetails());
		assertEquals(400, response.getBody().getStatusCode());
		assertEquals("BAD_REQUEST", response.getBody().getStatus().name());
	}
	
	@Test
	void shouldCreateRegistrationWithCustomField() {
		createCurrentSchoolYear(schoolYearRepository, 2000);
		createCustomField(customFieldRepository, "customField", Type.TEXT);
		createCustomField(customFieldRepository, "customField2", Type.BOOLEAN);
		
		
		List<RegistrationCustomFieldDTO> customFieldDTOS = new ArrayList<>();
		RegistrationCustomFieldDTO customFieldDTO = new RegistrationCustomFieldDTO();
		customFieldDTO.setName("customField");
		customFieldDTO.setValue("customFieldValue");
		customFieldDTOS.add(customFieldDTO);
		customFieldDTO = new RegistrationCustomFieldDTO();
		customFieldDTO.setName("customField2");
		customFieldDTO.setValue("true");
		customFieldDTOS.add(customFieldDTO);
		
		RegistrationCreateRequest registrationCreateRequest = new RegistrationCreateRequest();
		registrationCreateRequest.setCustomFields(customFieldDTOS);
		
		ResponseEntity<RegistrationResponse> registrationResponse = postWithAdminAuth("/registration", registrationCreateRequest, RegistrationResponse.class);
		
		assertEquals(201, registrationResponse.getStatusCode().value());
		assertNotNull(registrationResponse.getBody());
		assertNotNull(registrationResponse.getBody().getId());
		assertEquals(registrationCreateRequest.getCustomFields().getFirst().getName(), registrationResponse.getBody().getCustomFields().getFirst().getName());
		assertEquals(registrationCreateRequest.getCustomFields().getFirst().getValue(), registrationResponse.getBody().getCustomFields().getFirst().getValue());
		assertEquals(registrationCreateRequest.getCustomFields().getLast().getName(), registrationResponse.getBody().getCustomFields().getLast().getName());
		assertEquals(registrationCreateRequest.getCustomFields().getLast().getValue(), registrationResponse.getBody().getCustomFields().getLast().getValue());
	}
	
	@Test
	void shouldThrowErrorIfAllCustomFieldAreNotFill() {
		createCurrentSchoolYear(schoolYearRepository, 2000);
		createCustomField(customFieldRepository, "customField", Type.TEXT);
		createCustomField(customFieldRepository, "customField2", Type.BOOLEAN);
		
		List<RegistrationCustomFieldDTO> customFieldDTOS = new ArrayList<>();
		RegistrationCustomFieldDTO customFieldDTO = new RegistrationCustomFieldDTO();
		customFieldDTO.setName("customField");
		customFieldDTO.setValue("customFieldValue");
		customFieldDTOS.add(customFieldDTO);
		
		RegistrationCreateRequest registrationCreateRequest = new RegistrationCreateRequest();
		registrationCreateRequest.setCustomFields(customFieldDTOS);
		
		ResponseEntity<ErrorHandler> response = postWithAdminAuth("/registration", registrationCreateRequest, ErrorHandler.class);
		
		assertEquals(400, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getTimestamp());
		assertEquals("Not all custom fields are present", response.getBody().getMessage());
		assertEquals("The request is invalid.", response.getBody().getDetails());
		assertEquals(400, response.getBody().getStatusCode());
		assertEquals("BAD_REQUEST", response.getBody().getStatus().name());
	}
	
	@Test
	void shouldThrowErrorIfCustomFieldValueDontMatchWithType() {
		createCurrentSchoolYear(schoolYearRepository, 2000);
		createCustomField(customFieldRepository, "customField", Type.TEXT);
		createCustomField(customFieldRepository, "customField2", Type.BOOLEAN);
		
		List<RegistrationCustomFieldDTO> customFieldDTOS = new ArrayList<>();
		RegistrationCustomFieldDTO customFieldDTO = new RegistrationCustomFieldDTO();
		customFieldDTO.setName("customField");
		customFieldDTO.setValue("customFieldValue");
		customFieldDTOS.add(customFieldDTO);
		customFieldDTO = new RegistrationCustomFieldDTO();
		customFieldDTO.setName("customField2");
		customFieldDTO.setValue("customFieldValue");
		customFieldDTOS.add(customFieldDTO);
		
		RegistrationCreateRequest registrationCreateRequest = new RegistrationCreateRequest();
		registrationCreateRequest.setCustomFields(customFieldDTOS);
		
		ResponseEntity<ErrorHandler> response = postWithAdminAuth("/registration", registrationCreateRequest, ErrorHandler.class);
		
		assertEquals(400, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getTimestamp());
		assertEquals("Custom field customField2 value customFieldValue is not valid", response.getBody().getMessage());
		assertEquals("The request is invalid.", response.getBody().getDetails());
		assertEquals(400, response.getBody().getStatusCode());
		assertEquals("BAD_REQUEST", response.getBody().getStatus().name());
	}
	
	@Test
	void shouldThrowErrorIfCustomFieldIsNotFound() {
		createCurrentSchoolYear(schoolYearRepository, 2000);
		createCustomField(customFieldRepository, "customField", Type.TEXT);
		createCustomField(customFieldRepository, "customField2", Type.BOOLEAN);
		
		List<RegistrationCustomFieldDTO> customFieldDTOS = new ArrayList<>();
		RegistrationCustomFieldDTO customFieldDTO = new RegistrationCustomFieldDTO();
		customFieldDTO.setName("customField");
		customFieldDTO.setValue("customFieldValue");
		customFieldDTOS.add(customFieldDTO);
		customFieldDTO = new RegistrationCustomFieldDTO();
		customFieldDTO.setName("customField3");
		customFieldDTO.setValue("customFieldValue");
		customFieldDTOS.add(customFieldDTO);
		
		RegistrationCreateRequest registrationCreateRequest = new RegistrationCreateRequest();
		registrationCreateRequest.setCustomFields(customFieldDTOS);
		
		ResponseEntity<ErrorHandler> response = postWithAdminAuth("/registration", registrationCreateRequest, ErrorHandler.class);
		
		assertEquals(400, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getTimestamp());
		assertEquals("Custom field customField3 not found", response.getBody().getMessage());
		assertEquals("The request is invalid.", response.getBody().getDetails());
		assertEquals(400, response.getBody().getStatusCode());
		assertEquals("BAD_REQUEST", response.getBody().getStatus().name());
	}
	
	@Test
	void shouldUpdateRegistrationCustomField() {
		Registration registration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID);
		CustomField firstCustomField = createCustomField(customFieldRepository, "customField", Type.TEXT);
		CustomField secondCustomField = createCustomField(customFieldRepository, "customField2", Type.BOOLEAN);
		
		registration.setCustomFields(new ArrayList<>());
		RegistrationCustomField registrationCustomField = new RegistrationCustomField();
		registrationCustomField.setCustomField(firstCustomField);
		registrationCustomField.setRegistration(registration);
		registrationCustomField.setValue("customFieldValue");
		registration.getCustomFields().add(registrationCustomField);
		registrationCustomField = new RegistrationCustomField();
		registrationCustomField.setCustomField(secondCustomField);
		registrationCustomField.setRegistration(registration);
		registrationCustomField.setValue("true");
		registration.getCustomFields().add(registrationCustomField);
		registration = registrationRepository.save(registration);
		
		List<RegistrationCustomFieldDTO> customFieldDTOS = new ArrayList<>();
		RegistrationCustomFieldDTO customFieldDTO = new RegistrationCustomFieldDTO();
		customFieldDTO.setName("customField");
		customFieldDTO.setValue("updatedCustomFieldValue");
		customFieldDTOS.add(customFieldDTO);
		customFieldDTO = new RegistrationCustomFieldDTO();
		customFieldDTO.setName("customField2");
		customFieldDTO.setValue("false");
		customFieldDTOS.add(customFieldDTO);
		
		RegistrationUpdateRequest registrationUpdateRequest = new RegistrationUpdateRequest();
		registrationUpdateRequest.setCustomFields(customFieldDTOS);
		
		ResponseEntity<RegistrationResponse> registrationResponse = putWithAdminAuth("/registration/" + registration.getId(), registrationUpdateRequest, RegistrationResponse.class);
		
		assertEquals(200, registrationResponse.getStatusCode().value());
		assertNotNull(registrationResponse.getBody());
		assertEquals(registration.getId(), registrationResponse.getBody().getId());
		assertEquals(registrationUpdateRequest.getCustomFields().getFirst().getName(), registrationResponse.getBody().getCustomFields().getFirst().getName());
		assertEquals(registrationUpdateRequest.getCustomFields().getFirst().getValue(), registrationResponse.getBody().getCustomFields().getFirst().getValue());
		assertEquals(registrationUpdateRequest.getCustomFields().getLast().getName(), registrationResponse.getBody().getCustomFields().getLast().getName());
		assertEquals(registrationUpdateRequest.getCustomFields().getLast().getValue(), registrationResponse.getBody().getCustomFields().getLast().getValue());
	}
	
	@Test
	void shouldThrowExceptionWhenUpdateHaveNoAllCustomField() {
		Registration registration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID);
		CustomField firstCustomField = createCustomField(customFieldRepository, "customField", Type.TEXT);
		CustomField secondCustomField = createCustomField(customFieldRepository, "customField2", Type.BOOLEAN);
		
		registration.setCustomFields(new ArrayList<>());
		RegistrationCustomField registrationCustomField = new RegistrationCustomField();
		registrationCustomField.setCustomField(firstCustomField);
		registrationCustomField.setRegistration(registration);
		registrationCustomField.setValue("customFieldValue");
		registration.getCustomFields().add(registrationCustomField);
		registrationCustomField = new RegistrationCustomField();
		registrationCustomField.setCustomField(secondCustomField);
		registrationCustomField.setRegistration(registration);
		registrationCustomField.setValue("true");
		registration.getCustomFields().add(registrationCustomField);
		registration = registrationRepository.save(registration);
		
		List<RegistrationCustomFieldDTO> customFieldDTOS = new ArrayList<>();
		RegistrationCustomFieldDTO customFieldDTO = new RegistrationCustomFieldDTO();
		customFieldDTO.setName("customField");
		customFieldDTO.setValue("updatedCustomFieldValue");
		customFieldDTOS.add(customFieldDTO);
		
		RegistrationUpdateRequest registrationUpdateRequest = new RegistrationUpdateRequest();
		registrationUpdateRequest.setCustomFields(customFieldDTOS);
		
		ResponseEntity<ErrorHandler> response = putWithAdminAuth("/registration/" + registration.getId(), registrationUpdateRequest, ErrorHandler.class);
		
		assertEquals(400, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getTimestamp());
		assertEquals("Not all custom fields are present", response.getBody().getMessage());
		assertEquals("The request is invalid.", response.getBody().getDetails());
		assertEquals(400, response.getBody().getStatusCode());
		assertEquals("BAD_REQUEST", response.getBody().getStatus().name());
	}
	
	@Test
	void shouldThrowErrorWhenUpdatedCustomFieldDontMatchWithCustomFieldType() {
		Registration registration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID);
		CustomField firstCustomField = createCustomField(customFieldRepository, "customField", Type.TEXT);
		CustomField secondCustomField = createCustomField(customFieldRepository, "customField2", Type.BOOLEAN);
		
		registration.setCustomFields(new ArrayList<>());
		RegistrationCustomField registrationCustomField = new RegistrationCustomField();
		registrationCustomField.setCustomField(firstCustomField);
		registrationCustomField.setRegistration(registration);
		registrationCustomField.setValue("customFieldValue");
		registration.getCustomFields().add(registrationCustomField);
		registrationCustomField = new RegistrationCustomField();
		registrationCustomField.setCustomField(secondCustomField);
		registrationCustomField.setRegistration(registration);
		registrationCustomField.setValue("true");
		registration.getCustomFields().add(registrationCustomField);
		registration = registrationRepository.save(registration);
		
		List<RegistrationCustomFieldDTO> customFieldDTOS = new ArrayList<>();
		RegistrationCustomFieldDTO customFieldDTO = new RegistrationCustomFieldDTO();
		customFieldDTO.setName("customField");
		customFieldDTO.setValue("updatedCustomFieldValue");
		customFieldDTOS.add(customFieldDTO);
		customFieldDTO = new RegistrationCustomFieldDTO();
		customFieldDTO.setName("customField2");
		customFieldDTO.setValue("updatedCustomFieldValue");
		customFieldDTOS.add(customFieldDTO);
		
		RegistrationUpdateRequest registrationUpdateRequest = new RegistrationUpdateRequest();
		registrationUpdateRequest.setCustomFields(customFieldDTOS);
		
		ResponseEntity<ErrorHandler> response = putWithAdminAuth("/registration/" + registration.getId(), registrationUpdateRequest, ErrorHandler.class);
		
		assertEquals(400, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertNotNull(response.getBody().getTimestamp());
		assertEquals("Custom field customField2 value updatedCustomFieldValue is not valid", response.getBody().getMessage());
		assertEquals("The request is invalid.", response.getBody().getDetails());
		assertEquals(400, response.getBody().getStatusCode());
		assertEquals("BAD_REQUEST", response.getBody().getStatus().name());
	}
}
