package fr.aredli.easorms.registration.security.registration;

import fr.aredli.easorms.registration.IntegrationTest;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationRequest.RegistrationCreateRequest;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationRequest.RegistrationUpdateRequest;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationResponse;
import fr.aredli.easorms.registration.entity.Registration;
import fr.aredli.easorms.registration.entity.Registration.Status;
import fr.aredli.easorms.registration.exception.ErrorHandler;
import fr.aredli.easorms.registration.repository.RegistrationRepository;
import fr.aredli.easorms.registration.repository.SchoolYearRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static fr.aredli.easorms.registration.util.RegistrationUtilTest.createCurrentSchoolYear;
import static fr.aredli.easorms.registration.util.RegistrationUtilTest.createRegistration;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RegistrationControllerUserAuthSecurityTest extends IntegrationTest {
	@Autowired
	private RegistrationRepository registrationRepository;
	@Autowired
	private SchoolYearRepository schoolYearRepository;
	
	
	@Test
	void getAll() {
		ResponseEntity<ErrorHandler> response = getWithUserAuth("/registration", ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
	
	@Test
	void getByIdNoOwnerRegistration() {
		Registration registration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID);
		
		ResponseEntity<ErrorHandler> response = getWithUserAuth("/registration/" + registration.getId(), ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
	
	@Test
	void getByIdOwnerRegistration() {
		Registration registration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_USER_USERID);
		
		ResponseEntity<RegistrationResponse> response = getWithUserAuth("/registration/" + registration.getId(), RegistrationResponse.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertNotNull(response.getBody());
	}
	
	@Test
	void create() {
		createCurrentSchoolYear(schoolYearRepository, 2024);
		
		ResponseEntity<RegistrationResponse> response = postWithUserAuth("/registration", new RegistrationCreateRequest(), RegistrationResponse.class);
		
		assertEquals(201, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals(KEYCLOAK_CONTAINER_USER_USERID, response.getBody().getCreatedBy());
	}
	
	@Test
	void updateNoOwnerRegistration() {
		Registration registration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID);
		
		ResponseEntity<ErrorHandler> response = putWithUserAuth("/registration/" + registration.getId(), new RegistrationUpdateRequest(), ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
	
	@Test
	void updateOwnerRegistration() {
		Registration registration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_USER_USERID);
		
		ResponseEntity<RegistrationResponse> response = putWithUserAuth("/registration/" + registration.getId(), new RegistrationUpdateRequest(), RegistrationResponse.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertNotNull(response.getBody());
	}
	
	@Test
	void deleteNoOwnerRegistration() {
		Registration registration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_ADMIN_USERID);
		
		ResponseEntity<ErrorHandler> response = deleteWithUserAuth("/registration/" + registration.getId(), ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
	
	@Test
	void deleteOwnerRegistration() {
		Registration registration = createRegistration(registrationRepository, KEYCLOAK_CONTAINER_USER_USERID);
		
		ResponseEntity<Void> response = deleteWithUserAuth("/registration/" + registration.getId(), Void.class);
		
		assertEquals(204, response.getStatusCode().value());
	}
	
	@Test
	void deleteAll() {
		ResponseEntity<ErrorHandler> response = deleteWithUserAuth("/registration", ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
	
	@Test
	void approve() {
		ResponseEntity<ErrorHandler> response = postWithUserAuth("/registration/1/approve", null, ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
	
	@Test
	void reject() {
		ResponseEntity<ErrorHandler> response = postWithUserAuth("/registration/1/reject", null, ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
	
	@Test
	void getByStatus() {
		ResponseEntity<ErrorHandler> response = getWithUserAuth("/registration/status/" + Status.APPROVED, ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
	
	@Test
	void getByCurrentSchoolYear() {
		ResponseEntity<ErrorHandler> response = getWithUserAuth("/registration/school-year/current", ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
}
