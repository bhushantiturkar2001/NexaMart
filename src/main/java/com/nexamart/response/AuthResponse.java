package com.nexamart.response;

import com.nexamart.domain.USER_ROLE;

import lombok.Data;

@Data
public class AuthResponse {
	
	private String jwt;
	private String msg;
	private USER_ROLE role;

}
