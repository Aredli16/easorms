package fr.aredli.easorms.registration.controller;

import fr.aredli.easorms.registration.dto.SchoolYearDTO.SchoolYearRequest.SchoolYearCreateRequest;
import fr.aredli.easorms.registration.dto.SchoolYearDTO.SchoolYearRequest.SchoolYearUpdateRequest;
import fr.aredli.easorms.registration.dto.SchoolYearDTO.SchoolYearResponse;
import fr.aredli.easorms.registration.services.SchoolYearService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/school-year")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class SchoolYearController {
	private final SchoolYearService schoolYearService;
	
	@GetMapping
	public ResponseEntity<List<SchoolYearResponse>> getAllSchoolYears() {
		return ResponseEntity.ok(schoolYearService.findAllSchoolYears());
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<SchoolYearResponse> getSchoolYearById(@PathVariable String id) {
		return ResponseEntity.ok(schoolYearService.findSchoolYearById(id));
	}
	
	@PostMapping
	public ResponseEntity<SchoolYearResponse> createSchoolYear(@RequestBody SchoolYearCreateRequest request) {
		return new ResponseEntity<>(schoolYearService.createSchoolYear(request), HttpStatus.CREATED);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<SchoolYearResponse> updateSchoolYear(@PathVariable String id, @RequestBody SchoolYearUpdateRequest request) {
		return ResponseEntity.ok(schoolYearService.updateSchoolYear(id, request));
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteSchoolYear(@PathVariable String id) {
		schoolYearService.deleteSchoolYear(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping
	public ResponseEntity<Void> deleteAllSchoolYears() {
		schoolYearService.deleteAllSchoolYears();
		
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/current")
	public ResponseEntity<SchoolYearResponse> getCurrentSchoolYear() {
		return ResponseEntity.ok(schoolYearService.findCurrentSchoolYear());
	}
	
	@PostMapping("/current/{id}")
	public ResponseEntity<Void> setCurrentSchoolYear(@PathVariable String id) {
		schoolYearService.setCurrentSchoolYear(id);
		
		return ResponseEntity.noContent().build();
	}
}
