package com.example.springjwt.service;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.springjwt.config.CustomUserDetails;
import com.example.springjwt.config.JwtTokenProvider;
import com.example.springjwt.domain.Auth;
import com.example.springjwt.domain.Role;
import com.example.springjwt.domain.User;
import com.example.springjwt.dto.AuthRequestDto;
import com.example.springjwt.dto.AuthResponseDto;
import com.example.springjwt.dto.UserRequestDto;
import com.example.springjwt.repository.AuthRepository;
import com.example.springjwt.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
	private final UserRepository userRepository;
	private final AuthRepository authRepository;
	private final PasswordEncoder passwordEncoder;
	private final JwtTokenProvider jwtTokenProvider;

	/** 로그인 */
	@Transactional
	public AuthResponseDto login(AuthRequestDto requestDto) {
		// CHECK USERNAME AND PASSWORD
		User user = this.userRepository.findByUsername(requestDto.getUsername()).orElseThrow(
			() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다. username = " + requestDto.getUsername()));
		if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
			throw new IllegalArgumentException("비밀번호가 일치하지 않습니다. username = " + requestDto.getUsername());
		}

		// GENERATE ACCESS_TOKEN AND REFRESH_TOKEN
		String accessToken = this.jwtTokenProvider.generateAccessToken(
			new UsernamePasswordAuthenticationToken(new CustomUserDetails(user), user.getPassword()));
		String refreshToken = this.jwtTokenProvider.generateRefreshToken(
			new UsernamePasswordAuthenticationToken(new CustomUserDetails(user), user.getPassword()));

		// CHECK IF AUTH ENTITY EXISTS, THEN UPDATE TOKEN
		if (this.authRepository.existsByUser(user)) {
			user.getAuth().updateAccessToken(accessToken);
			user.getAuth().updateRefreshToken(refreshToken);
			return new AuthResponseDto(user.getAuth());
		}

		// IF NOT EXISTS AUTH ENTITY, SAVE AUTH ENTITY AND TOKEN
		Auth auth = this.authRepository.save(Auth.builder()
			.user(user)
			.tokenType("Bearer")
			.accessToken(accessToken)
			.refreshToken(refreshToken)
			.build());
		return new AuthResponseDto(auth);
	}

	/** 회원가입 */
	@Transactional
	public void signup(UserRequestDto requestDto) {
		// SAVE USER ENTITY
		requestDto.setRole(Role.ROLE_USER);
		requestDto.setPassword(passwordEncoder.encode(requestDto.getPassword()));
		this.userRepository.save(requestDto.toEntity());
	}

	/** Token 갱신 */
	@Transactional
	public String refreshToken(String refreshToken) {
		// CHECK IF REFRESH_TOKEN EXPIRATION AVAILABLE, UPDATE ACCESS_TOKEN AND RETURN
		if (this.jwtTokenProvider.validateToken(refreshToken)) {
			Auth auth = this.authRepository.findByRefreshToken(refreshToken).orElseThrow(
				() -> new IllegalArgumentException("해당 REFRESH_TOKEN 을 찾을 수 없습니다.\nREFRESH_TOKEN = " + refreshToken));

			String newAccessToken = this.jwtTokenProvider.generateAccessToken(
				new UsernamePasswordAuthenticationToken(
					new CustomUserDetails(auth.getUser()), auth.getUser().getPassword()));
			auth.updateAccessToken(newAccessToken);
			return newAccessToken;
		}

		// IF NOT AVAILABLE REFRESH_TOKEN EXPIRATION, REGENERATE ACCESS_TOKEN AND REFRESH_TOKEN
		// IN THIS CASE, USER HAVE TO LOGIN AGAIN, SO REGENERATE IS NOT APPROPRIATE
		return null;
	}
}