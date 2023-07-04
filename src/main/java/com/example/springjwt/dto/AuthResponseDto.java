package com.example.springjwt.dto;

import com.example.springjwt.domain.Auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponseDto {
	private String tokenType;
	private String accessToken;
	private String refreshToken;

	@Builder
	public AuthResponseDto(Auth entity) {
		this.tokenType = entity.getTokenType();
		this.accessToken = entity.getAccessToken();
		this.refreshToken = entity.getRefreshToken();
	}
}