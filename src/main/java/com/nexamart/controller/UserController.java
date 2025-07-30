package com.nexamart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

import com.nexamart.modal.User;
import com.nexamart.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
    
	@GetMapping("/users/profile")
	public ResponseEntity<User> createUserHandler(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findByJwtToken(jwt);
		return ResponseEntity.ok(user);
		
	}

}
