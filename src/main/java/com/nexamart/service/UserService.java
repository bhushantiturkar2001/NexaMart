package com.nexamart.service;

import com.nexamart.modal.User;

public interface UserService {
	
	User findByJwtToken(String jwt) throws Exception;
	User findUserByEmail(String email) throws Exception;
	
}
