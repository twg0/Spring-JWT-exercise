package com.example.springjwt.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.example.springjwt.config.JwtTokenProvider;
import com.example.springjwt.dto.UserRequestDto;
import com.example.springjwt.dto.UserResponseDto;
import com.example.springjwt.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserRestController {

	private final UserService userService;
	private final JwtTokenProvider jwtTokenProvider;

	/** 회원정보 조회 API */
	@GetMapping("/api/v1/user")
	public ResponseEntity<?> findUser(@RequestHeader("Authorization") String accessToken) {
		Long id = this.jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));
		UserResponseDto userResponseDto = this.userService.findById(id);
		return ResponseEntity.status(HttpStatus.OK).body(userResponseDto);
	}

	/** 회원정보 수정 API */
	@PutMapping("/api/v1/user")
	public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String accessToken,
		@RequestBody UserRequestDto requestDto) {
		Long id = this.jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));
		this.userService.update(id, requestDto);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}

	/** 회원정보 삭제 API */
	@DeleteMapping("/api/v1/user")
	public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String accessToken) {
		Long id = this.jwtTokenProvider.getUserIdFromToken(accessToken.substring(7));
		this.userService.delete(id);
		return ResponseEntity.status(HttpStatus.OK).body(null);
	}
}