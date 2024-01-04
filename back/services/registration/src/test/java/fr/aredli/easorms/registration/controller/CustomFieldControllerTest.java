package fr.aredli.easorms.registration.controller;

import fr.aredli.easorms.registration.IntegrationTest;
import fr.aredli.easorms.registration.dto.CustomFieldDTO.CustomFieldRequest.CustomFieldCreateRequest;
import fr.aredli.easorms.registration.dto.CustomFieldDTO.CustomFieldRequest.CustomFieldUpdateRequest;
import fr.aredli.easorms.registration.dto.CustomFieldDTO.CustomFieldResponse;
import fr.aredli.easorms.registration.entity.CustomField;
import fr.aredli.easorms.registration.entity.CustomField.Type;
import fr.aredli.easorms.registration.repository.CustomFieldRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import static fr.aredli.easorms.registration.util.RegistrationUtilTest.createCustomField;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomFieldControllerTest extends IntegrationTest {
	@Autowired
	private CustomFieldRepository customFieldRepository;
	
	@BeforeEach
	void setUp() {
		customFieldRepository.deleteAll();
	}
	
	@Test
	void shouldGetAllCustomField() {
		CustomField firstCustomField = createCustomField(customFieldRepository, "firstCustomField", Type.TEXT);
		CustomField secondCustomField = createCustomField(customFieldRepository, "secondCustomField", Type.DATE);
		CustomField thirdCustomField = createCustomField(customFieldRepository, "thirdCustomField", Type.NUMBER);
		
		ResponseEntity<CustomFieldResponse[]> response = getWithAdminAuth("/custom-field", CustomFieldResponse[].class);
		
		assertEquals(200, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals(3, response.getBody().length);
		assertEquals(firstCustomField.getId(), response.getBody()[0].getId());
		assertEquals(secondCustomField.getId(), response.getBody()[1].getId());
		assertEquals(thirdCustomField.getId(), response.getBody()[2].getId());
	}
	
	@Test
	void shouldGetCustomFieldById() {
		CustomField customField = createCustomField(customFieldRepository, "customField", Type.TEXT);
		createCustomField(customFieldRepository, "secondCustomField", Type.DATE);
		createCustomField(customFieldRepository, "thirdCustomField", Type.NUMBER);
		
		ResponseEntity<CustomFieldResponse> response = getWithAdminAuth("/custom-field/" + customField.getId(), CustomFieldResponse.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals(customField.getId(), response.getBody().getId());
	}
	
	@Test
	void shouldCreateCustomField() {
		CustomFieldCreateRequest request = new CustomFieldCreateRequest();
		request.setName("firstCustomField");
		request.setType(Type.TEXT);
		
		ResponseEntity<CustomFieldResponse> response = postWithAdminAuth("/custom-field", request, CustomFieldResponse.class);
		
		assertEquals(201, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("firstCustomField", response.getBody().getName());
		assertEquals(Type.TEXT, response.getBody().getType());
	}
	
	@Test
	void shouldUpdateCustomField() {
		CustomField customField = createCustomField(customFieldRepository, "customField", Type.TEXT);
		
		CustomFieldUpdateRequest request = new CustomFieldUpdateRequest();
		request.setName("updatedCustomField");
		request.setType(Type.DATE);
		
		ResponseEntity<CustomFieldResponse> response = putWithAdminAuth("/custom-field/" + customField.getId(), request, CustomFieldResponse.class);
		
		assertEquals(200, response.getStatusCode().value());
		assertNotNull(response.getBody());
		assertEquals("updatedCustomField", response.getBody().getName());
		assertEquals(Type.DATE, response.getBody().getType());
	}
	
	@Test
	void shouldDeleteCustomField() {
		CustomField customField = createCustomField(customFieldRepository, "customField", Type.TEXT);
		
		ResponseEntity<Void> response = deleteWithAdminAuth("/custom-field/" + customField.getId(), Void.class);
		
		assertEquals(204, response.getStatusCode().value());
	}
	
	@Test
	void shouldDeleteAllCustomField() {
		createCustomField(customFieldRepository, "firstCustomField", Type.TEXT);
		createCustomField(customFieldRepository, "secondCustomField", Type.DATE);
		createCustomField(customFieldRepository, "thirdCustomField", Type.NUMBER);
		
		ResponseEntity<Void> response = deleteWithAdminAuth("/custom-field", Void.class);
		
		assertEquals(204, response.getStatusCode().value());
		assertEquals(0, customFieldRepository.count());
	}
}
