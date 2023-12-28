package fr.aredli.easorms.registration.mapper;

import fr.aredli.easorms.registration.dto.CustomFieldDTO.CustomFieldRequest.CustomFieldCreateRequest;
import fr.aredli.easorms.registration.dto.CustomFieldDTO.CustomFieldRequest.CustomFieldUpdateRequest;
import fr.aredli.easorms.registration.dto.CustomFieldDTO.CustomFieldResponse;
import fr.aredli.easorms.registration.entity.CustomField;
import org.modelmapper.ModelMapper;

public class CustomFieldMapper {
	private static final ModelMapper mapper = new ModelMapper();
	
	public static CustomFieldResponse mapEntityToDTO(CustomField entity) {
		return mapper.map(entity, CustomFieldResponse.class);
	}
	
	public static CustomField mapDTOToEntity(CustomFieldCreateRequest dto) {
		return mapper.map(dto, CustomField.class);
	}
	
	public static void mapDTOToEntity(CustomField customField, CustomFieldUpdateRequest dto) {
		mapper.map(dto, customField);
	}
	
	public static CustomField mapDTOToEntity(CustomFieldResponse customFieldResponse) {
		return mapper.map(customFieldResponse, CustomField.class);
	}
}
