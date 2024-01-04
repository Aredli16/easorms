package fr.aredli.easorms.registration.security.schooyear;

import fr.aredli.easorms.registration.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SchoolYearControllerNoAuthTest extends IntegrationTest {
	
	@Test
	void getAllSchoolYears() {
		ResponseEntity<Void> response = getWithoutAuth("/school-year", Void.class);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void getSchoolYearById() {
		ResponseEntity<Void> response = getWithoutAuth("/school-year/1", Void.class);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void createSchoolYear() {
		ResponseEntity<Void> response = postWithoutAuth("/school-year", Void.class, null);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void updateSchoolYear() {
		ResponseEntity<Void> response = putWithoutAuth("/school-year/1", Void.class, null);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void deleteSchoolYear() {
		ResponseEntity<Void> response = deleteWithoutAuth("/school-year/1", Void.class);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void deleteAllSchoolYears() {
		ResponseEntity<Void> response = deleteWithoutAuth("/school-year", Void.class);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void getCurrentSchoolYear() {
		ResponseEntity<Void> response = getWithoutAuth("/school-year/current", Void.class);
		
		assertEquals(401, response.getStatusCode().value());
	}
	
	@Test
	void setCurrentSchoolYear() {
		ResponseEntity<Void> response = postWithoutAuth("/school-year/current/1", Void.class, null);
		
		assertEquals(401, response.getStatusCode().value());
	}
}
