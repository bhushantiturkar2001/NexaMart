package com.nexamart.service;

import com.nexamart.domain.USER_ROLE;
import com.nexamart.request.LoginRequest;
import com.nexamart.response.AuthResponse;
import com.nexamart.response.SignUpRequest;

public interface AuthService {
	
	void sentLoginOtp(String email, USER_ROLE role) throws Exception;
	String createUser(SignUpRequest req) throws Exception;
	AuthResponse signing(LoginRequest req);
	

}
