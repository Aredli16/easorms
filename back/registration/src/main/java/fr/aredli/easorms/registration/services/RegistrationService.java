package fr.aredli.easorms.registration.services;

import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationPageResponse;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationRequest.RegistrationCreateRequest;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationRequest.RegistrationUpdateRequest;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationResponse;
import fr.aredli.easorms.registration.entity.Registration;
import fr.aredli.easorms.registration.entity.Registration.Status;
import fr.aredli.easorms.registration.entity.SchoolYear;
import fr.aredli.easorms.registration.mapper.RegistrationMapper;
import fr.aredli.easorms.registration.mapper.SchoolYearMapper;
import fr.aredli.easorms.registration.repository.RegistrationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrationService {
	private final RegistrationRepository repository;
	private final SchoolYearService schoolYearService;
	
	public RegistrationPageResponse findAll(int page, int size, String sortBy, String sortDirection) {
		Page<Registration> registrations = repository.findAll(PageRequest.of(page, size).withSort(Sort.by(Direction.fromString(sortDirection), sortBy)));
		
		return RegistrationPageResponse
				.builder()
				.page(registrations.getNumber())
				.totalPages(registrations.getTotalPages())
				.totalElements(registrations.getTotalElements())
				.registrations(registrations.getContent().stream().map(RegistrationMapper::mapEntityToDTO).toList())
				.build();
	}
	
	public RegistrationResponse findById(String id) {
		return RegistrationMapper.mapEntityToDTO(repository.findById(id).orElseThrow());
	}
	
	public RegistrationResponse create(RegistrationCreateRequest request) {
		SchoolYear schoolYear;
		
		try {
			schoolYear = SchoolYearMapper.mapDTOToEntity(schoolYearService.findCurrentSchoolYear());
		} catch (Exception e) {
			throw new IllegalArgumentException("No current school year found");
		}
		
		Registration registration = RegistrationMapper.mapDTOToEntity(request);
		registration.setSchoolYear(schoolYear);
		
		return RegistrationMapper.mapEntityToDTO(repository.save(registration));
	}
	
	public RegistrationResponse update(String id, RegistrationUpdateRequest request) {
		Registration entity = repository.findById(id).orElseThrow();
		RegistrationMapper.mapDTOToEntity(entity, request);
		
		return RegistrationMapper.mapEntityToDTO(repository.save(entity));
	}
	
	public void delete(String id) {
		repository.deleteById(id);
	}
	
	public void deleteAll() {
		repository.deleteAll();
	}
	
	public RegistrationResponse approve(String id) {
		Registration entity = repository.findById(id).orElseThrow();
		entity.setStatus(Status.APPROVED);
		
		return RegistrationMapper.mapEntityToDTO(repository.save(entity));
	}
	
	public RegistrationResponse reject(String id) {
		Registration entity = repository.findById(id).orElseThrow();
		entity.setStatus(Status.REJECTED);
		
		return RegistrationMapper.mapEntityToDTO(repository.save(entity));
	}
	
	public RegistrationPageResponse findByStatus(String status, int page, int size, String sortBy, String sortDirection) {
		Page<Registration> registrations = repository.findByStatus(Status.valueOf(status), PageRequest.of(page, size).withSort(Sort.by(Direction.fromString(sortDirection), sortBy)));
		
		return RegistrationPageResponse
				.builder()
				.page(registrations.getNumber())
				.totalPages(registrations.getTotalPages())
				.totalElements(registrations.getTotalElements())
				.registrations(repository.findByStatus(Status.valueOf(status), PageRequest.of(page, size).withSort(Sort.by(Direction.fromString(sortDirection), sortBy))).stream().map(RegistrationMapper::mapEntityToDTO).toList())
				.build();
	}
	
	public RegistrationPageResponse findByCurrentSchoolYear(int page, int size, String sortBy, String sortDirection) {
		SchoolYear schoolYear = SchoolYearMapper.mapDTOToEntity(schoolYearService.findCurrentSchoolYear());
		Page<Registration> registrations = repository.findBySchoolYear(schoolYear, PageRequest.of(page, size).withSort(Sort.by(Direction.fromString(sortDirection), sortBy)));
		
		return RegistrationPageResponse
				.builder()
				.page(registrations.getNumber())
				.totalPages(registrations.getTotalPages())
				.totalElements(registrations.getTotalElements())
				.registrations(repository.findBySchoolYear(schoolYear, PageRequest.of(page, size).withSort(Sort.by(Direction.fromString(sortDirection), sortBy))).stream().map(RegistrationMapper::mapEntityToDTO).toList())
				.build();
	}
}
