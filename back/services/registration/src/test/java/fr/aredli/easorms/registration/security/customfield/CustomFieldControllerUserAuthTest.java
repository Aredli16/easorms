package fr.aredli.easorms.registration.security.customfield;

import fr.aredli.easorms.registration.IntegrationTest;
import fr.aredli.easorms.registration.dto.CustomFieldDTO.CustomFieldRequest.CustomFieldCreateRequest;
import fr.aredli.easorms.registration.dto.CustomFieldDTO.CustomFieldRequest.CustomFieldUpdateRequest;
import fr.aredli.easorms.registration.exception.ErrorHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomFieldControllerUserAuthTest extends IntegrationTest {
	
	@Test
	void getAll() {
		ResponseEntity<ErrorHandler> response = getWithUserAuth("/custom-field", ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
	
	@Test
	void getById() {
		ResponseEntity<ErrorHandler> response = getWithUserAuth("/custom-field/1", ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
	
	@Test
	void create() {
		ResponseEntity<ErrorHandler> response = postWithUserAuth("/custom-field", new CustomFieldCreateRequest(), ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
	
	@Test
	void update() {
		ResponseEntity<ErrorHandler> response = putWithUserAuth("/custom-field/1", new CustomFieldUpdateRequest(), ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
	
	@Test
	void delete() {
		ResponseEntity<ErrorHandler> response = deleteWithUserAuth("/custom-field/1", ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
	
	@Test
	void deleteAll() {
		ResponseEntity<ErrorHandler> response = deleteWithUserAuth("/custom-field", ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
}
