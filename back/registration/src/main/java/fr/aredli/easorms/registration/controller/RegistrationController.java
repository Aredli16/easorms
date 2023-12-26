package fr.aredli.easorms.registration.controller;

import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationPageResponse;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationRequest.RegistrationCreateRequest;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationRequest.RegistrationUpdateRequest;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationResponse;
import fr.aredli.easorms.registration.services.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {
	private final RegistrationService registrationService;
	
	@GetMapping
	public ResponseEntity<RegistrationPageResponse> getAll(
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "20") int size,
			@RequestParam(required = false, defaultValue = "updatedAt") String sortBy,
			@RequestParam(required = false, defaultValue = "desc") String sortDirection
	) {
		return ResponseEntity.ok(registrationService.findAll(page, size, sortBy, sortDirection));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<RegistrationResponse> getById(@PathVariable String id) {
		return ResponseEntity.ok(registrationService.findById(id));
	}
	
	@PostMapping
	public ResponseEntity<RegistrationResponse> create(@RequestBody RegistrationCreateRequest registration) {
		return new ResponseEntity<>(registrationService.create(registration), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<RegistrationResponse> update(@PathVariable String id, @RequestBody RegistrationUpdateRequest registration) {
		return ResponseEntity.ok(registrationService.update(id, registration));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable String id) {
		registrationService.delete(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping
	public ResponseEntity<Void> deleteAll() {
		registrationService.deleteAll();
		
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping("/{id}/approve")
	public ResponseEntity<RegistrationResponse> approve(@PathVariable String id) {
		return ResponseEntity.ok(registrationService.approve(id));
	}
	
	@PostMapping("/{id}/reject")
	public ResponseEntity<RegistrationResponse> reject(@PathVariable String id) {
		return ResponseEntity.ok(registrationService.reject(id));
	}
	
	@GetMapping("/status/{status}")
	public ResponseEntity<RegistrationPageResponse> getByStatus(
			@PathVariable String status,
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "20") int size,
			@RequestParam(required = false, defaultValue = "updatedAt") String sortBy,
			@RequestParam(required = false, defaultValue = "desc") String sortDirection
	) {
		return ResponseEntity.ok(registrationService.findByStatus(status, page, size, sortBy, sortDirection));
	}
}
