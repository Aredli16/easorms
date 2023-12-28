package fr.aredli.easorms.registration.controller;

import fr.aredli.easorms.registration.dto.CustomFieldDTO.CustomFieldRequest.CustomFieldCreateRequest;
import fr.aredli.easorms.registration.dto.CustomFieldDTO.CustomFieldRequest.CustomFieldUpdateRequest;
import fr.aredli.easorms.registration.dto.CustomFieldDTO.CustomFieldResponse;
import fr.aredli.easorms.registration.entity.CustomField;
import fr.aredli.easorms.registration.entity.CustomField.Type;
import fr.aredli.easorms.registration.repository.CustomFieldRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomFieldControllerTest extends DatabaseIntegrationTest {
	@Autowired
	private CustomFieldRepository customFieldRepository;
	
	private CustomField createCustomField(String name, Type type) {
		CustomField customField = new CustomField();
		customField.setName(name);
		customField.setType(type);
		
		return customFieldRepository.save(customField);
	}
	
	@BeforeEach
	void setUp() {
		customFieldRepository.deleteAll();
	}
	
	@Test
	void shouldGetAllCustomField() {
		CustomField firstCustomField = createCustomField("firstCustomField", Type.TEXT);
		CustomField secondCustomField = createCustomField("secondCustomField", Type.DATE);
		CustomField thirdCustomField = createCustomField("thirdCustomField", Type.NUMBER);
		
		ResponseEntity<CustomFieldResponse[]> response = restTemplate.getForEntity("/custom-field", CustomFieldResponse[].class);
		
		assertEquals(200, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals(3, response.getBody().length);
		assertEquals(firstCustomField.getId(), response.getBody()[0].getId());
		assertEquals(secondCustomField.getId(), response.getBody()[1].getId());
		assertEquals(thirdCustomField.getId(), response.getBody()[2].getId());
	}
	
	@Test
	void shouldGetCustomFieldById() {
		CustomField customField = createCustomField("customField", Type.TEXT);
		createCustomField("secondCustomField", Type.DATE);
		createCustomField("thirdCustomField", Type.NUMBER);
		
		ResponseEntity<CustomFieldResponse> response = restTemplate.getForEntity("/custom-field/" + customField.getId(), CustomFieldResponse.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals(customField.getId(), response.getBody().getId());
	}
	
	@Test
	void shouldCreateCustomField() {
		CustomFieldCreateRequest request = new CustomFieldCreateRequest();
		request.setName("firstCustomField");
		request.setType(Type.TEXT);
		
		ResponseEntity<CustomFieldResponse> response = restTemplate.postForEntity("/custom-field", request, CustomFieldResponse.class);
		
		assertEquals(201, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("firstCustomField", response.getBody().getName());
		assertEquals(Type.TEXT, response.getBody().getType());
	}
	
	@Test
	void shouldUpdateCustomField() {
		CustomField customField = createCustomField("customField", Type.TEXT);
		
		CustomFieldUpdateRequest request = new CustomFieldUpdateRequest();
		request.setName("updatedCustomField");
		request.setType(Type.DATE);
		
		ResponseEntity<CustomFieldResponse> response = restTemplate.exchange("/custom-field/" + customField.getId(), HttpMethod.PUT, new HttpEntity<>(request), CustomFieldResponse.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("updatedCustomField", response.getBody().getName());
		assertEquals(Type.DATE, response.getBody().getType());
	}
	
	@Test
	void shouldDeleteCustomField() {
		CustomField customField = createCustomField("customField", Type.TEXT);
		
		ResponseEntity<Void> response = restTemplate.exchange("/custom-field/" + customField.getId(), HttpMethod.DELETE, null, Void.class);
		
		assertEquals(204, response.getStatusCode().value());
	}
	
	@Test
	void shouldDeleteAllCustomField() {
		createCustomField("firstCustomField", Type.TEXT);
		createCustomField("secondCustomField", Type.DATE);
		createCustomField("thirdCustomField", Type.NUMBER);
		
		ResponseEntity<Void> response = restTemplate.exchange("/custom-field", HttpMethod.DELETE, null, Void.class);
		
		assertEquals(204, response.getStatusCode().value());
		assertEquals(0, customFieldRepository.count());
	}
}
