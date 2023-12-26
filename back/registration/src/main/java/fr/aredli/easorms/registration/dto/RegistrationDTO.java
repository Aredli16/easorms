package fr.aredli.easorms.registration.dto;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;
import java.util.List;

@Data
public abstract class RegistrationDTO {
	private String lastName;
	private String firstName;
	private String email;
	private String phone;
	private String streetAddress;
	private String zipCode;
	private String city;
	private String country;
	
	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class RegistrationResponse extends RegistrationDTO {
		private String id;
		private String createdBy;
		private Date createdAt;
		private Date updatedAt;
	}
	
	@Data
	@EqualsAndHashCode(callSuper = true)
	public static abstract class RegistrationRequest extends RegistrationDTO {
		@Data
		@EqualsAndHashCode(callSuper = true)
		public static class RegistrationCreateRequest extends RegistrationRequest {
		}
		
		@Data
		@EqualsAndHashCode(callSuper = true)
		public static class RegistrationUpdateRequest extends RegistrationRequest {
		}
	}
	
	@Data
	@Builder
	public static class RegistrationPageResponse {
		private int page;
		private int totalPages;
		private long totalElements;
		private List<RegistrationResponse> registrations;
	}
}
