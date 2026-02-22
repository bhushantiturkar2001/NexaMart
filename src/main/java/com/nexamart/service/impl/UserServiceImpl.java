package com.nexamart.service.impl;

import org.springframework.stereotype.Service;

import com.nexamart.config.JwtProvider;
import com.nexamart.modal.User;
import com.nexamart.repository.UserRepository;
import com.nexamart.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final JwtProvider jwtProvider;
	private final UserRepository userRepository;

	@Override
	public User findByJwtToken(String jwt) throws Exception {
		String email = jwtProvider.getEmailFromJwtToken(jwt);

		User user = this.findUserByEmail(email);

		return user;
	}

	@Override
	public User findUserByEmail(String email) throws Exception {
		User user = userRepository.findByEmail(email);

		if (user == null) {
			throw new Exception("User not found with this email: " + email);
		}
		return user;
	}

}
