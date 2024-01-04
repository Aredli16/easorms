package fr.aredli.easorms.registration.security.customfield;

import fr.aredli.easorms.registration.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomFieldControllerNoAuthTest extends IntegrationTest {
	
	@Test
	void getAll() {
		ResponseEntity<Void> response = getWithoutAuth("/custom-field", Void.class);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void getById() {
		ResponseEntity<Void> response = getWithoutAuth("/custom-field/1", Void.class);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void create() {
		ResponseEntity<Void> response = postWithoutAuth("/custom-field", Void.class, null);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void update() {
		ResponseEntity<Void> response = putWithoutAuth("/custom-field/1", Void.class, null);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void delete() {
		ResponseEntity<Void> response = deleteWithoutAuth("/custom-field/1", Void.class);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void deleteAll() {
		ResponseEntity<Void> response = deleteWithoutAuth("/custom-field", Void.class);
		
		assertEquals(401, response.getStatusCode().value());
	}
}
