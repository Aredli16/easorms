package fr.aredli.easorms.registration.controller;

import fr.aredli.easorms.registration.dto.CustomFieldDTO.CustomFieldRequest.CustomFieldCreateRequest;
import fr.aredli.easorms.registration.dto.CustomFieldDTO.CustomFieldRequest.CustomFieldUpdateRequest;
import fr.aredli.easorms.registration.dto.CustomFieldDTO.CustomFieldResponse;
import fr.aredli.easorms.registration.services.CustomFieldService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/custom-field")
@RequiredArgsConstructor
public class CustomFieldController {
	private final CustomFieldService customFieldService;
	
	@GetMapping
	public ResponseEntity<List<CustomFieldResponse>> getAll() {
		return ResponseEntity.ok(customFieldService.findAll());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CustomFieldResponse> getById(@PathVariable String id) {
		return ResponseEntity.ok(customFieldService.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<CustomFieldResponse> create(@RequestBody CustomFieldCreateRequest request) {
		return new ResponseEntity<>(customFieldService.save(request), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<CustomFieldResponse> update(@PathVariable String id, @RequestBody CustomFieldUpdateRequest request) {
		return ResponseEntity.ok(customFieldService.update(id, request));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		customFieldService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping
	public ResponseEntity<Void> deleteAll() {
		customFieldService.deleteAll();
		
		return ResponseEntity.noContent().build();
	}
}
