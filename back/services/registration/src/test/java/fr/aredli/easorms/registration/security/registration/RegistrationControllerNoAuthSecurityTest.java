package fr.aredli.easorms.registration.security.registration;

import fr.aredli.easorms.registration.IntegrationTest;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationRequest.RegistrationCreateRequest;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationRequest.RegistrationUpdateRequest;
import fr.aredli.easorms.registration.entity.Registration.Status;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RegistrationControllerNoAuthSecurityTest extends IntegrationTest {
	
	@Test
	void getAll() {
		ResponseEntity<Void> response = getWithoutAuth("/registration", Void.class);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void getById() {
		ResponseEntity<Void> response = getWithoutAuth("/registration/1", Void.class);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void create() {
		ResponseEntity<Void> response = postWithoutAuth("/registration", new RegistrationCreateRequest(), Void.class);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void update() {
		ResponseEntity<Void> response = putWithoutAuth("/registration/1", new RegistrationUpdateRequest(), Void.class);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void delete() {
		ResponseEntity<Void> response = deleteWithoutAuth("/registration/1", Void.class);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void deleteAll() {
		ResponseEntity<Void> response = deleteWithoutAuth("/registration", Void.class);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void approve() {
		ResponseEntity<Void> response = postWithoutAuth("/registration/1/approve", null, Void.class);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void reject() {
		ResponseEntity<Void> response = postWithoutAuth("/registration/1/reject", null, Void.class);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void getByStatus() {
		ResponseEntity<Void> response = getWithoutAuth("/registration/status/" + Status.APPROVED, Void.class);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void getByCurrentSchoolYear() {
		ResponseEntity<Void> response = getWithoutAuth("/registration/school-year/current", Void.class);
		
		assertEquals(401, response.getStatusCode().value());
	}
}
