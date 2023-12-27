package fr.aredli.easorms.registration.mapper;

import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationRequest;
import fr.aredli.easorms.registration.dto.RegistrationDTO.RegistrationResponse;
import fr.aredli.easorms.registration.entity.Registration;

public class RegistrationMapper {
	
	public static RegistrationResponse mapEntityToDTO(Registration entity) {
		return Mapper.getMapper().map(entity, RegistrationResponse.class);
	}
	
	public static Registration mapDTOToEntity(RegistrationRequest registration) {
		return Mapper.getMapper().map(registration, Registration.class);
	}
	
	public static void mapDTOToEntity(Registration registration, RegistrationRequest registrationRequest) {
		Mapper.getMapper().map(registrationRequest, registration);
	}
}
