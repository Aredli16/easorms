package fr.aredli.easorms.registration.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class CustomField {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	@Column(nullable = false, unique = true)
	private String name;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Type type;
	
	public enum Type {
		TEXT, NUMBER, DATE, BOOLEAN;
		
		public boolean validate(String value) {
			return switch (this) {
				case TEXT -> true;
				case NUMBER -> value.matches("^\\d+$");
				case DATE -> value.matches("^\\d{4}-\\d{2}-\\d{2}$");
				case BOOLEAN -> value.matches("^(true|false)$");
			};
		}
	}
}
