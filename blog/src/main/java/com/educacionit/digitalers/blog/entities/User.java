package com.educacionit.digitalers.blog.entities;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	@Column(unique = true, nullable = false)
	private String email;
	@Column(name = "password")
	private String key;
	private Boolean active;
	private LocalDateTime creationDate;
	@Column(columnDefinition = "TINYINT DEFAULT 0", insertable = false, updatable = true)
	private Byte failedAttemps;
	@OneToMany(mappedBy = "user", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
	@JsonIgnore
	private List<Publication> publications;
	
	@Override
	public String toString() {
		return "User [id=" + id + ", email=" + email + ", key=" + key + ", active=" + active + ", creationDate="
				+ creationDate + ", failedAttemps=" + failedAttemps + "]";
	}
	
}