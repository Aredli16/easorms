package fr.aredli.easorms.registration.controller;

import fr.aredli.easorms.registration.entity.SchoolYear;
import fr.aredli.easorms.registration.exception.ErrorHandler;
import fr.aredli.easorms.registration.repository.SchoolYearRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class SchoolYearControllerTest extends DatabaseIntegrationTest {
	@Autowired
	private SchoolYearRepository schoolYearRepository;
	
	private SchoolYear createSchoolYear(int year) {
		SchoolYear schoolYear = new SchoolYear();
		
		schoolYear.setStartDate(LocalDate.of(year, 9, 1));
		schoolYear.setEndDate(LocalDate.of(year + 1, 6, 30));
		
		return schoolYearRepository.save(schoolYear);
	}
	
	@BeforeEach
	void setUp() {
		schoolYearRepository.deleteAll();
	}
	
	@Test
	void shouldGetAllSchoolYear() {
		SchoolYear firstSchoolYear = createSchoolYear(2000);
		SchoolYear secondSchoolYear = createSchoolYear(2002);
		SchoolYear thirdSchoolYear = createSchoolYear(2004);
		
		ResponseEntity<SchoolYear[]> response = restTemplate.getForEntity("/school-year", SchoolYear[].class);
		
		assertEquals(200, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals(3, response.getBody().length);
		assertEquals(firstSchoolYear.getId(), response.getBody()[0].getId());
		assertEquals(secondSchoolYear.getId(), response.getBody()[1].getId());
		assertEquals(thirdSchoolYear.getId(), response.getBody()[2].getId());
	}
	
	@Test
	void shouldGetTheCorrectRegistration() {
		SchoolYear schoolYear = createSchoolYear(2000);
		
		ResponseEntity<SchoolYear> response = restTemplate.getForEntity("/school-year/" + schoolYear.getId(), SchoolYear.class);
		
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
		
		ResponseEntity<SchoolYear> response = restTemplate.postForEntity("/school-year", schoolYear, SchoolYear.class);
		
		assertEquals(201, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals(schoolYear.getStartDate(), response.getBody().getStartDate());
		assertEquals(schoolYear.getEndDate(), response.getBody().getEndDate());
	}
	
	@Test
	void shouldThrowExceptionIfSchoolYearAlreadyExists() {
		SchoolYear schoolYear = createSchoolYear(2000);
		
		ResponseEntity<ErrorHandler> response = restTemplate.postForEntity("/school-year", schoolYear, ErrorHandler.class);
		
		assertEquals(400, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("School year already exists", response.getBody().getMessage());
	}
	
	@Test
	void shouldThrowExceptionIfEndDateIsBeforeStartDate() {
		SchoolYear schoolYear = new SchoolYear();
		
		schoolYear.setStartDate(LocalDate.of(2000, 9, 1));
		schoolYear.setEndDate(LocalDate.of(1999, 6, 30));
		
		ResponseEntity<ErrorHandler> response = restTemplate.postForEntity("/school-year", schoolYear, ErrorHandler.class);
		
		assertEquals(400, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Start date must be before end date", response.getBody().getMessage());
	}
	
	@Test
	void shouldUpdateSchoolYear() {
		SchoolYear schoolYear = createSchoolYear(2000);
		
		schoolYear.setStartDate(LocalDate.of(2001, 9, 1));
		schoolYear.setEndDate(LocalDate.of(2002, 6, 30));
		
		ResponseEntity<SchoolYear> response = restTemplate.exchange("/school-year/" + schoolYear.getId(), HttpMethod.PUT, new HttpEntity<>(schoolYear), SchoolYear.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals(schoolYear.getStartDate(), response.getBody().getStartDate());
		assertEquals(schoolYear.getEndDate(), response.getBody().getEndDate());
	}
	
	@Test
	void shouldThrowExceptionIfSchoolYearAlreadyExistsWhenUpdating() {
		SchoolYear firstSchoolYear = createSchoolYear(2000);
		
		firstSchoolYear.setStartDate(LocalDate.of(2000, 9, 1));
		firstSchoolYear.setEndDate(LocalDate.of(2001, 6, 30));
		
		ResponseEntity<ErrorHandler> response = restTemplate.exchange("/school-year/" + firstSchoolYear.getId(), HttpMethod.PUT, new HttpEntity<>(firstSchoolYear), ErrorHandler.class);
		
		assertEquals(400, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("School year already exists", response.getBody().getMessage());
	}
	
	@Test
	void shouldThrowExceptionIfEndDateIsBeforeStartDateWhenUpdating() {
		SchoolYear schoolYear = createSchoolYear(2000);
		
		schoolYear.setStartDate(LocalDate.of(2001, 9, 1));
		schoolYear.setEndDate(LocalDate.of(2000, 6, 30));
		
		ResponseEntity<ErrorHandler> response = restTemplate.exchange("/school-year/" + schoolYear.getId(), HttpMethod.PUT, new HttpEntity<>(schoolYear), ErrorHandler.class);
		
		assertEquals(400, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("Start date must be before end date", response.getBody().getMessage());
	}
	
	@Test
	void shouldDeleteSchoolYear() {
		SchoolYear schoolYear = createSchoolYear(2000);
		
		ResponseEntity<Void> response = restTemplate.exchange("/school-year/" + schoolYear.getId(), HttpMethod.DELETE, null, Void.class);
		
		assertEquals(204, response.getStatusCode().value());
	}
	
	@Test
	void shouldDeleteAllSchoolYears() {
		createSchoolYear(2000);
		createSchoolYear(2001);
		createSchoolYear(2002);
		
		ResponseEntity<Void> response = restTemplate.exchange("/school-year", HttpMethod.DELETE, null, Void.class);
		
		assertEquals(204, response.getStatusCode().value());
	}
	
	@Test
	void shouldGetCurrentSchoolYear() {
		SchoolYear schoolYear = createSchoolYear(2000);
		schoolYear.setCurrent(true);
		schoolYearRepository.save(schoolYear);
		
		ResponseEntity<SchoolYear> response = restTemplate.getForEntity("/school-year/current", SchoolYear.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals(schoolYear.getId(), response.getBody().getId());
		assertEquals(schoolYear.getStartDate(), response.getBody().getStartDate());
		assertEquals(schoolYear.getEndDate(), response.getBody().getEndDate());
	}
	
	@Test
	void shouldSetCurrentSchoolYear() {
		SchoolYear schoolYear = createSchoolYear(2000);
		
		ResponseEntity<Void> response = restTemplate.postForEntity("/school-year/current/" + schoolYear.getId(), null, Void.class);
		
		assertEquals(204, response.getStatusCode().value());
	}
	
	@Test
	void shouldSwitchCorrectlyCurrentSchoolYear() {
		SchoolYear firstSchoolYear = createSchoolYear(2000);
		SchoolYear secondSchoolYear = createSchoolYear(2001);
		
		firstSchoolYear.setCurrent(true);
		secondSchoolYear.setCurrent(false);
		schoolYearRepository.save(firstSchoolYear);
		schoolYearRepository.save(secondSchoolYear);
		
		ResponseEntity<Void> response = restTemplate.postForEntity("/school-year/current/" + secondSchoolYear.getId(), null, Void.class);
		
		assertEquals(204, response.getStatusCode().value());
		
		firstSchoolYear = schoolYearRepository.findById(firstSchoolYear.getId()).orElseThrow();
		secondSchoolYear = schoolYearRepository.findById(secondSchoolYear.getId()).orElseThrow();
		
		assertFalse(firstSchoolYear.isCurrent());
		assertTrue(secondSchoolYear.isCurrent());
	}
}
