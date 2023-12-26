package fr.aredli.easorms.registration.services;

import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationPageResponse;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationRequest.RegistrationCreateRequest;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationRequest.RegistrationUpdateRequest;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationResponse;
import fr.aredli.easorms.registration.entity.Registration;
import fr.aredli.easorms.registration.entity.Registration.RegistrationStatus;
import fr.aredli.easorms.registration.mapper.RegistrationMapper;
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
	
	public RegistrationResponse create(RegistrationCreateRequest registration) {
		return RegistrationMapper.mapEntityToDTO(repository.save(RegistrationMapper.mapDTOToEntity(registration)));
	}
	
	public RegistrationResponse update(String id, RegistrationUpdateRequest registration) {
		Registration entity = repository.findById(id).orElseThrow();
		RegistrationMapper.mapDTOToEntity(entity, registration);
		
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
		entity.setStatus(RegistrationStatus.APPROVED);
		
		return RegistrationMapper.mapEntityToDTO(repository.save(entity));
	}
	
	public RegistrationResponse reject(String id) {
		Registration entity = repository.findById(id).orElseThrow();
		entity.setStatus(RegistrationStatus.REJECTED);
		
		return RegistrationMapper.mapEntityToDTO(repository.save(entity));
	}
	
	public RegistrationPageResponse findByStatus(String status, int page, int size, String sortBy, String sortDirection) {
		Page<Registration> registrations = repository.findByStatus(RegistrationStatus.valueOf(status), PageRequest.of(page, size).withSort(Sort.by(Direction.fromString(sortDirection), sortBy)));
		
		return RegistrationPageResponse
				.builder()
				.page(registrations.getNumber())
				.totalPages(registrations.getTotalPages())
				.totalElements(registrations.getTotalElements())
				.registrations(repository.findByStatus(RegistrationStatus.valueOf(status), PageRequest.of(page, size).withSort(Sort.by(Direction.fromString(sortDirection), sortBy))).stream().map(RegistrationMapper::mapEntityToDTO).toList())
				.build();
	}
}
