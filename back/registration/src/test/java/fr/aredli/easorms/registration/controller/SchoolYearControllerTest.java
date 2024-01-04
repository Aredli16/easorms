package fr.aredli.easorms.registration.controller;

import fr.aredli.easorms.registration.IntegrationTest;
import fr.aredli.easorms.registration.entity.SchoolYear;
import fr.aredli.easorms.registration.exception.ErrorHandler;
import fr.aredli.easorms.registration.repository.SchoolYearRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static fr.aredli.easorms.registration.util.RegistrationUtilTest.createSchoolYear;
import static org.junit.jupiter.api.Assertions.*;

class SchoolYearControllerTest extends IntegrationTest {
	@Autowired
	private SchoolYearRepository schoolYearRepository;
	
	@BeforeEach
	void setUp() {
		schoolYearRepository.deleteAll();
	}
	
	@Test
	void shouldGetAllSchoolYear() {
		SchoolYear firstSchoolYear = createSchoolYear(schoolYearRepository, 2000);
		SchoolYear secondSchoolYear = createSchoolYear(schoolYearRepository, 2002);
		SchoolYear thirdSchoolYear = createSchoolYear(schoolYearRepository, 2004);
		
		ResponseEntity<SchoolYear[]> response = getWithAdminAuth("/school-year", SchoolYear[].class);
		
		assertEquals(200, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals(3, response.getBody().length);
		assertEquals(firstSchoolYear.getId(), response.getBody()[0].getId());
		assertEquals(secondSchoolYear.getId(), response.getBody()[1].getId());
		assertEquals(thirdSchoolYear.getId(), response.getBody()[2].getId());
	}
	
	@Test
	void shouldGetTheCorrectRegistration() {
		SchoolYear schoolYear = createSchoolYear(schoolYearRepository, 2000);
		
		ResponseEntity<SchoolYear> response = getWithAdminAuth("/school-year/" + schoolYear.getId(), SchoolYear.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals(schoolYear.getId(), response.getBody().getId());
		assertEquals(schoolYear.getStartDate(), response.getBody().getStartDate());
		assertEquals(schoolYear.getEndDate(), response.getBody().getEndDate());
	}
	
	@Test
	void shouldCreateRegistration() {
		SchoolYear schoolYear = new SchoolYear();
		
		schoolYear.setStartDate(LocalDate.of(2000, 9, 1));
		schoolYear.setEndDate(LocalDate.of(2001, 6, 30));
		
		ResponseEntity<SchoolYear> response = postWithAdminAuth("/school-year", schoolYear, SchoolYear.class);
		
		assertEquals(201, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals(schoolYear.getStartDate(), response.getBody().getStartDate());
		assertEquals(schoolYear.getEndDate(), response.getBody().getEndDate());
	}
	
	@Test
	void shouldThrowExceptionIfSchoolYearAlreadyExists() {
		SchoolYear schoolYear = createSchoolYear(schoolYearRepository, 2000);
		
		ResponseEntity<ErrorHandler> response = postWithAdminAuth("/school-year", schoolYear, ErrorHandler.class);
		
		assertEquals(400, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("School year already exists", response.getBody().getMessage());
	}
	
	@Test
	void shouldThrowExceptionIfEndDateIsBeforeStartDate() {
		SchoolYear schoolYear = new SchoolYear();
		
		schoolYear.setStartDate(LocalDate.of(2000, 9, 1));
		schoolYear.setEndDate(LocalDate.of(1999, 6, 30));
		
		ResponseEntity<ErrorHandler> response = postWithAdminAuth("/school-year", schoolYear, ErrorHandler.class);
		
		assertEquals(400, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Start date must be before end date", response.getBody().getMessage());
	}
	
	@Test
	void shouldUpdateSchoolYear() {
		SchoolYear schoolYear = createSchoolYear(schoolYearRepository, 2000);
		
		schoolYear.setStartDate(LocalDate.of(2001, 9, 1));
		schoolYear.setEndDate(LocalDate.of(2002, 6, 30));
		
		ResponseEntity<SchoolYear> response = putWithAdminAuth("/school-year/" + schoolYear.getId(), schoolYear, SchoolYear.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals(schoolYear.getStartDate(), response.getBody().getStartDate());
		assertEquals(schoolYear.getEndDate(), response.getBody().getEndDate());
	}
	
	@Test
	void shouldThrowExceptionIfSchoolYearAlreadyExistsWhenUpdating() {
		SchoolYear firstSchoolYear = createSchoolYear(schoolYearRepository, 2000);
		
		firstSchoolYear.setStartDate(LocalDate.of(2000, 9, 1));
		firstSchoolYear.setEndDate(LocalDate.of(2001, 6, 30));
		
		ResponseEntity<ErrorHandler> response = putWithAdminAuth("/school-year/" + firstSchoolYear.getId(), firstSchoolYear, ErrorHandler.class);
		
		assertEquals(400, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("School year already exists", response.getBody().getMessage());
	}
	
	@Test
	void shouldThrowExceptionIfEndDateIsBeforeStartDateWhenUpdating() {
		SchoolYear schoolYear = createSchoolYear(schoolYearRepository, 2000);
		
		schoolYear.setStartDate(LocalDate.of(2001, 9, 1));
		schoolYear.setEndDate(LocalDate.of(2000, 6, 30));
		
		ResponseEntity<ErrorHandler> response = putWithAdminAuth("/school-year/" + schoolYear.getId(), schoolYear, ErrorHandler.class);
		
		assertEquals(400, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Start date must be before end date", response.getBody().getMessage());
	}
	
	@Test
	void shouldDeleteSchoolYear() {
		SchoolYear schoolYear = createSchoolYear(schoolYearRepository, 2000);
		
		ResponseEntity<Void> response = deleteWithAdminAuth("/school-year/" + schoolYear.getId(), Void.class);
		
		assertEquals(204, response.getStatusCode().value());
	}
	
	@Test
	void shouldDeleteAllSchoolYears() {
		createSchoolYear(schoolYearRepository, 2000);
		createSchoolYear(schoolYearRepository, 2001);
		createSchoolYear(schoolYearRepository, 2002);
		
		ResponseEntity<Void> response = deleteWithAdminAuth("/school-year", Void.class);
		
		assertEquals(204, response.getStatusCode().value());
	}
	
	@Test
	void shouldGetCurrentSchoolYear() {
		SchoolYear schoolYear = createSchoolYear(schoolYearRepository, 2000);
		schoolYear.setCurrent(true);
		schoolYearRepository.save(schoolYear);
		
		ResponseEntity<SchoolYear> response = getWithAdminAuth("/school-year/current", SchoolYear.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals(schoolYear.getId(), response.getBody().getId());
		assertEquals(schoolYear.getStartDate(), response.getBody().getStartDate());
		assertEquals(schoolYear.getEndDate(), response.getBody().getEndDate());
	}
	
	@Test
	void shouldSetCurrentSchoolYear() {
		SchoolYear schoolYear = createSchoolYear(schoolYearRepository, 2000);
		
		ResponseEntity<Void> response = postWithAdminAuth("/school-year/current/" + schoolYear.getId(), null, Void.class);
		
		assertEquals(204, response.getStatusCode().value());
	}
	
	@Test
	void shouldSwitchCorrectlyCurrentSchoolYear() {
		SchoolYear firstSchoolYear = createSchoolYear(schoolYearRepository, 2000);
		SchoolYear secondSchoolYear = createSchoolYear(schoolYearRepository, 2001);
		
		firstSchoolYear.setCurrent(true);
		secondSchoolYear.setCurrent(false);
		schoolYearRepository.save(firstSchoolYear);
		schoolYearRepository.save(secondSchoolYear);
		
		ResponseEntity<Void> response = postWithAdminAuth("/school-year/current/" + secondSchoolYear.getId(), null, Void.class);
		
		assertEquals(204, response.getStatusCode().value());
		
		firstSchoolYear = schoolYearRepository.findById(firstSchoolYear.getId()).orElseThrow();
		secondSchoolYear = schoolYearRepository.findById(secondSchoolYear.getId()).orElseThrow();
		
		assertFalse(firstSchoolYear.isCurrent());
		assertTrue(secondSchoolYear.isCurrent());
	}
}
