package fr.aredli.easorms.registration.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;
import java.util.List;

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
	private Status status;
	@ManyToOne
	private SchoolYear schoolYear;
	@OneToMany(mappedBy = "registration", cascade = CascadeType.ALL)
	private List<RegistrationCustomField> customFields;
	
	@PrePersist
	public void prePersist() {
		this.status = Status.PENDING;
	}
	
	public enum Status {
		PENDING,
		APPROVED,
		REJECTED,
		ARCHIVED
	}
}
