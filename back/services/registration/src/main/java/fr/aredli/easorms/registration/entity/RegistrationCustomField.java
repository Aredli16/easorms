package fr.aredli.easorms.registration.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table
@Data
public class RegistrationCustomField {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	@ManyToOne(fetch = FetchType.LAZY)
	private Registration registration;
	@ManyToOne(fetch = FetchType.LAZY)
	private CustomField customField;
	private String value;
}
