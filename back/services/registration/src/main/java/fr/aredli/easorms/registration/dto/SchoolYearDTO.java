package fr.aredli.easorms.registration.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;

@Data
public abstract class SchoolYearDTO {
	private LocalDate startDate;
	private LocalDate endDate;
	
	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class SchoolYearResponse extends SchoolYearDTO {
		private String id;
		private boolean current;
	}
	
	@Data
	@EqualsAndHashCode(callSuper = true)
	public static abstract class SchoolYearRequest extends SchoolYearDTO {
		@Data
		@EqualsAndHashCode(callSuper = true)
		public static class SchoolYearCreateRequest extends SchoolYearRequest {
		}
		
		@Data
		@EqualsAndHashCode(callSuper = true)
		public static class SchoolYearUpdateRequest extends SchoolYearRequest {
		}
	}
}
