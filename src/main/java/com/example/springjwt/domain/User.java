package com.example.springjwt.domain;

import com.example.springjwt.config.BaseTime;
import com.example.springjwt.dto.UserRequestDto;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "USERS")
public class User extends BaseTime {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 50, nullable = false, unique = true)
	private String email;

	@Column(length = 50, nullable = false, unique = true)
	private String contact;

	@Column(length = 50, nullable = false, unique = true)
	private String username;

	@Column(length = 100, nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	private Role role;

	@OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
	private Auth auth;

	@Builder
	public User(String email, String contact, String username, String password, Role role) {
		this.email = email;
		this.contact = contact;
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public void update(UserRequestDto userRequestDTO) {
		this.email = userRequestDTO.getEmail();
		this.contact = userRequestDTO.getContact();
		this.username = userRequestDTO.getUsername();
		this.password = userRequestDTO.getPassword();
		this.role = userRequestDTO.getRole();
	}
}
