package fr.aredli.easorms.registration.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Table
@Data
public class SchoolYear {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private String id;
	private LocalDate startDate;
	private LocalDate endDate;
	private boolean current = false;
}
