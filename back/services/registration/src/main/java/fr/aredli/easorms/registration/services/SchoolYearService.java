package fr.aredli.easorms.registration.services;

import fr.aredli.easorms.registration.dto.SchoolYearDTO.SchoolYearRequest.SchoolYearCreateRequest;
import fr.aredli.easorms.registration.dto.SchoolYearDTO.SchoolYearRequest.SchoolYearUpdateRequest;
import fr.aredli.easorms.registration.dto.SchoolYearDTO.SchoolYearResponse;
import fr.aredli.easorms.registration.entity.SchoolYear;
import fr.aredli.easorms.registration.mapper.SchoolYearMapper;
import fr.aredli.easorms.registration.repository.SchoolYearRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolYearService {
	private final SchoolYearRepository schoolYearRepository;
	
	private void valideSchoolYear(LocalDate startDate, LocalDate endDate) {
		if (schoolYearRepository.existsByStartDateAndEndDate(startDate, endDate))
			throw new IllegalArgumentException("School year already exists");
		
		if (startDate.isAfter(endDate))
			throw new IllegalArgumentException("Start date must be before end date");
	}
	
	public List<SchoolYearResponse> findAllSchoolYears() {
		return schoolYearRepository
				.findAll()
				.stream()
				.map(SchoolYearMapper::mapEntityToDTO)
				.sorted((d1, d2) -> {
					if (d1.getStartDate().isBefore(d2.getStartDate()))
						return -1;
					else if (d1.getStartDate().isAfter(d2.getStartDate()))
						return 1;
					else
						return 0;
				})
				.toList();
	}
	
	public SchoolYearResponse findSchoolYearById(String id) {
		return SchoolYearMapper.mapEntityToDTO(schoolYearRepository.findById(id).orElseThrow());
	}
	
	public SchoolYearResponse createSchoolYear(SchoolYearCreateRequest request) {
		valideSchoolYear(request.getStartDate(), request.getEndDate());
		
		return SchoolYearMapper.mapEntityToDTO(schoolYearRepository.save(SchoolYearMapper.mapDTOToEntity(request)));
	}
	
	public SchoolYearResponse updateSchoolYear(String id, SchoolYearUpdateRequest request) {
		SchoolYear schoolYear = schoolYearRepository.findById(id).orElseThrow();
		SchoolYearMapper.mapDTOToEntity(schoolYear, request);
		
		valideSchoolYear(schoolYear.getStartDate(), schoolYear.getEndDate());
		
		return SchoolYearMapper.mapEntityToDTO(schoolYearRepository.save(schoolYear));
	}
	
	public void deleteSchoolYear(String id) {
		schoolYearRepository.deleteById(id);
	}
	
	public void deleteAllSchoolYears() {
		schoolYearRepository.deleteAll();
	}
	
	public SchoolYearResponse findCurrentSchoolYear() {
		return SchoolYearMapper.mapEntityToDTO(schoolYearRepository.findFirstByCurrentTrue().orElseThrow());
	}
	
	public void setCurrentSchoolYear(String id) {
		SchoolYear schoolYear = schoolYearRepository.findById(id).orElseThrow();
		SchoolYear currentSchoolYear = schoolYearRepository.findFirstByCurrentTrue().orElse(null);
		
		if (currentSchoolYear != null) {
			currentSchoolYear.setCurrent(false);
			schoolYearRepository.save(currentSchoolYear);
		}
		
		schoolYear.setCurrent(true);
		
		schoolYearRepository.save(schoolYear);
	}
}
