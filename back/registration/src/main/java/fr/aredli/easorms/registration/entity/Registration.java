package fr.aredli.easorms.registration.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Table
@Data
public class Registration {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private String lastName;
	private String firstName;
	private String email;
	private String phone;
	private String streetAddress;
	private String zipCode;
	private String city;
	private String country;
	private String createdBy;
	@CreationTimestamp
	private Date createdAt;
	@UpdateTimestamp
	private Date updatedAt;
	@Enumerated(EnumType.STRING)
	private RegistrationStatus status;
	
	@PrePersist
	public void prePersist() {
		this.status = RegistrationStatus.PENDING;
	}
	
	public enum RegistrationStatus {
		PENDING,
		APPROVED,
		REJECTED
	}
}
