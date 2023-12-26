package fr.aredli.easorms.registration.mapper;

import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationRequest;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationResponse;
import fr.aredli.easorms.registration.entity.Registration;
import org.modelmapper.ModelMapper;

public class RegistrationMapper {
	private static final ModelMapper mapper = new ModelMapper();
	
	public static RegistrationResponse mapEntityToDTO(Registration entity) {
		return mapper.map(entity, RegistrationResponse.class);
	}
	
	public static Registration mapDTOToEntity(RegistrationRequest registration) {
		return mapper.map(registration, Registration.class);
	}
	
	public static void mapDTOToEntity(Registration registration, RegistrationRequest registrationRequest) {
		mapper.map(registrationRequest, registration);
	}
}
