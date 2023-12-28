package fr.aredli.easorms.registration.mapper;

import fr.aredli.easorms.registration.dto.RegistrationCustomFieldDTO;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationRequest.RegistrationCreateRequest;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationRequest.RegistrationUpdateRequest;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationResponse;
import fr.aredli.easorms.registration.entity.Registration;
import fr.aredli.easorms.registration.entity.RegistrationCustomField;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;

public class RegistrationMapper {
	private static final ModelMapper mapper = new ModelMapper();
	
	static {
		Converter<List<RegistrationCustomField>, List<RegistrationCustomFieldDTO>> customFieldConverterToDTO = context -> {
			List<RegistrationCustomFieldDTO> customFields = new ArrayList<>();
			
			for (RegistrationCustomField customField : context.getSource()) {
				RegistrationCustomFieldDTO customFieldDTO = new RegistrationCustomFieldDTO();
				
				customFieldDTO.setName(customField.getCustomField().getName());
				customFieldDTO.setValue(customField.getValue());
				
				customFields.add(customFieldDTO);
			}
			
			return customFields;
		};
		
		mapper.createTypeMap(Registration.class, RegistrationResponse.class)
				.addMappings(map -> map.using(customFieldConverterToDTO).map(Registration::getCustomFields, RegistrationResponse::setCustomFields));
	}
	
	public static RegistrationResponse mapEntityToDTO(Registration entity) {
		return mapper.map(entity, RegistrationResponse.class);
	}
	
	public static Registration mapDTOToEntity(RegistrationCreateRequest registration) {
		return mapper.map(registration, Registration.class);
	}
	
	public static void mapDTOToEntity(Registration registration, RegistrationUpdateRequest registrationRequest) {
		mapper.map(registrationRequest, registration);
	}
}
