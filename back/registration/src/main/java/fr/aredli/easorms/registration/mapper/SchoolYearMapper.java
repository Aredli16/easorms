package fr.aredli.easorms.registration.mapper;

import fr.aredli.easorms.registration.dto.SchoolYearDTO.SchoolYearRequest.SchoolYearCreateRequest;
import fr.aredli.easorms.registration.dto.SchoolYearDTO.SchoolYearRequest.SchoolYearUpdateRequest;
import fr.aredli.easorms.registration.dto.SchoolYearDTO.SchoolYearResponse;
import fr.aredli.easorms.registration.entity.SchoolYear;

public class SchoolYearMapper {
	public static SchoolYearResponse mapEntityToDTO(SchoolYear entity) {
		return Mapper.getMapper().map(entity, SchoolYearResponse.class);
	}
	
	public static SchoolYear mapDTOToEntity(SchoolYearCreateRequest dto) {
		return Mapper.getMapper().map(dto, SchoolYear.class);
	}
	
	public static void mapDTOToEntity(SchoolYear schoolYear, SchoolYearUpdateRequest request) {
		Mapper.getMapper().map(request, schoolYear);
	}
	
	public static SchoolYear mapDTOToEntity(SchoolYearResponse dto) {
		return Mapper.getMapper().map(dto, SchoolYear.class);
	}
}
