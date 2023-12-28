package fr.aredli.easorms.registration.mapper;

import fr.aredli.easorms.registration.dto.SchoolYearDTO.SchoolYearRequest.SchoolYearCreateRequest;
import fr.aredli.easorms.registration.dto.SchoolYearDTO.SchoolYearRequest.SchoolYearUpdateRequest;
import fr.aredli.easorms.registration.dto.SchoolYearDTO.SchoolYearResponse;
import fr.aredli.easorms.registration.entity.SchoolYear;
import org.modelmapper.ModelMapper;

public class SchoolYearMapper {
	private static final ModelMapper mapper = new ModelMapper();
	
	public static SchoolYearResponse mapEntityToDTO(SchoolYear entity) {
		return mapper.map(entity, SchoolYearResponse.class);
	}
	
	public static SchoolYear mapDTOToEntity(SchoolYearCreateRequest dto) {
		return mapper.map(dto, SchoolYear.class);
	}
	
	public static void mapDTOToEntity(SchoolYear schoolYear, SchoolYearUpdateRequest request) {
		mapper.map(request, schoolYear);
	}
	
	public static SchoolYear mapDTOToEntity(SchoolYearResponse dto) {
		return mapper.map(dto, SchoolYear.class);
	}
}
