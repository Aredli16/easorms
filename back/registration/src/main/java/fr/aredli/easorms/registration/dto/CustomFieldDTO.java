package fr.aredli.easorms.registration.dto;

import fr.aredli.easorms.registration.entity.CustomField.Type;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public abstract class CustomFieldDTO {
	private String name;
	private Type type;
	
	@Data
	@EqualsAndHashCode(callSuper = true)
	public static class CustomFieldResponse extends CustomFieldDTO {
		private String id;
	}
	
	@Data
	@EqualsAndHashCode(callSuper = true)
	public static abstract class CustomFieldRequest extends CustomFieldDTO {
		@Data
		@EqualsAndHashCode(callSuper = true)
		public static class CustomFieldCreateRequest extends CustomFieldRequest {
		}
		
		@Data
		@EqualsAndHashCode(callSuper = true)
		public static class CustomFieldUpdateRequest extends CustomFieldRequest {
		}
	}
}
