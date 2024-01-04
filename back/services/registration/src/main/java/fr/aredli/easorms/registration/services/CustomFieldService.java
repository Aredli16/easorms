package fr.aredli.easorms.registration.services;

import fr.aredli.easorms.registration.dto.CustomFieldDTO.CustomFieldRequest.CustomFieldCreateRequest;
import fr.aredli.easorms.registration.dto.CustomFieldDTO.CustomFieldRequest.CustomFieldUpdateRequest;
import fr.aredli.easorms.registration.dto.CustomFieldDTO.CustomFieldResponse;
import fr.aredli.easorms.registration.entity.CustomField;
import fr.aredli.easorms.registration.mapper.CustomFieldMapper;
import fr.aredli.easorms.registration.repository.CustomFieldRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomFieldService {
	private final CustomFieldRepository customFieldRepository;
	
	public List<CustomFieldResponse> findAll() {
		return customFieldRepository.findAll().stream().map(CustomFieldMapper::mapEntityToDTO).toList();
	}
	
	public CustomFieldResponse findById(String id) {
		return CustomFieldMapper.mapEntityToDTO(customFieldRepository.findById(id).orElseThrow());
	}
	
	public CustomFieldResponse save(CustomFieldCreateRequest customField) {
		return CustomFieldMapper.mapEntityToDTO(customFieldRepository.save(CustomFieldMapper.mapDTOToEntity(customField)));
	}
	
	public CustomFieldResponse update(String id, CustomFieldUpdateRequest customField) {
		CustomField entity = customFieldRepository.findById(id).orElseThrow();
		CustomFieldMapper.mapDTOToEntity(entity, customField);
		return CustomFieldMapper.mapEntityToDTO(customFieldRepository.save(entity));
	}
	
	public void delete(String id) {
		customFieldRepository.deleteById(id);
	}
	
	public void deleteAll() {
		customFieldRepository.deleteAll();
	}
	
	public CustomFieldResponse findByName(String name) {
		return CustomFieldMapper.mapEntityToDTO(customFieldRepository.findByName(name).orElseThrow());
	}
	
	public long count() {
		return customFieldRepository.count();
	}
}
