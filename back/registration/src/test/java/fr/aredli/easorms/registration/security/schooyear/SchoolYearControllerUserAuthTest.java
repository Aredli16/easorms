package fr.aredli.easorms.registration.security.schooyear;

import fr.aredli.easorms.registration.IntegrationTest;
import fr.aredli.easorms.registration.dto.CustomFieldDTO.CustomFieldRequest.CustomFieldCreateRequest;
import fr.aredli.easorms.registration.dto.CustomFieldDTO.CustomFieldRequest.CustomFieldUpdateRequest;
import fr.aredli.easorms.registration.exception.ErrorHandler;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SchoolYearControllerUserAuthTest extends IntegrationTest {
	
	@Test
	void getAllSchoolYears() {
		ResponseEntity<ErrorHandler> response = getWithUserAuth("/school-year", ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
	
	@Test
	void getSchoolYearById() {
		ResponseEntity<ErrorHandler> response = getWithUserAuth("/school-year/1", ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
	
	@Test
	void createSchoolYear() {
		ResponseEntity<ErrorHandler> response = postWithUserAuth("/school-year", new CustomFieldCreateRequest(), ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
	
	@Test
	void updateSchoolYear() {
		ResponseEntity<ErrorHandler> response = putWithUserAuth("/school-year/1", new CustomFieldUpdateRequest(), ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
	
	@Test
	void deleteSchoolYear() {
		ResponseEntity<ErrorHandler> response = deleteWithUserAuth("/school-year/1", ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
	
	@Test
	void deleteAllSchoolYears() {
		ResponseEntity<ErrorHandler> response = deleteWithUserAuth("/school-year", ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
	
	@Test
	void getCurrentSchoolYear() {
		ResponseEntity<ErrorHandler> response = getWithUserAuth("/school-year/current", ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
	
	@Test
	void setCurrentSchoolYear() {
		ResponseEntity<ErrorHandler> response = postWithUserAuth("/school-year/current/1", null, ErrorHandler.class);
		
		assertEquals(403, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Access Denied", response.getBody().getMessage());
		assertEquals("Access denied.", response.getBody().getDetails());
		assertEquals(HttpStatus.FORBIDDEN, response.getBody().getStatus());
		assertEquals(403, response.getBody().getStatusCode());
	}
}
