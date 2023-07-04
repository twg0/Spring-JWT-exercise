package com.example.springjwt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.springjwt.dto.AuthRequestDto;
import com.example.springjwt.dto.AuthResponseDto;
import com.example.springjwt.dto.UserRequestDto;
import com.example.springjwt.service.AuthService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class AuthRestController {
	private final AuthService authService;

	/** 로그인 API */
	@PostMapping("/api/v1/auth/login")
	public ResponseEntity<?> login(@RequestBody AuthRequestDto requestDto) {
		AuthResponseDto responseDto = this.authService.login(requestDto);
		return ResponseEntity.status(HttpStatus.OK).body(responseDto);
	}

	/** 회원가입 API */
	@PostMapping("/api/v1/auth/signup")
	public ResponseEntity<?> singUp(@RequestBody UserRequestDto requestDto) {
		this.authService.signup(requestDto);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	/** 토큰갱신 API */
	@GetMapping("/api/v1/auth/refresh")
	public ResponseEntity<?> refreshToken(@RequestHeader("REFRESH_TOKEN") String refreshToken) {
		String newAccessToken = this.authService.refreshToken(refreshToken);
		return ResponseEntity.status(HttpStatus.OK).body(newAccessToken);
	}
}