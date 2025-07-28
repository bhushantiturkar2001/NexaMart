package com.nexamart.service;

import com.nexamart.request.LoginRequest;
import com.nexamart.response.AuthResponse;
import com.nexamart.response.SignUpRequest;

public interface AuthService {
	
	void sentLoginOtp(String email) throws Exception;
	String createUser(SignUpRequest req) throws Exception;
	AuthResponse signing(LoginRequest req);
	

}
