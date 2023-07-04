package com.example.springjwt.dto;

import com.example.springjwt.domain.Role;
import com.example.springjwt.domain.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRequestDto {
	private Role role;
	private String email;
	private String contact;
	private String username;
	private String password;

	public User toEntity() {
		return User.builder()
			.role(this.role)
			.email(this.email)
			.contact(this.contact)
			.username(this.username)
			.password(this.password)
			.build();
	}
}
