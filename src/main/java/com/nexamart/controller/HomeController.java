package com.nexamart.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
	
	@GetMapping("/")
	public String HomeControllerMethod() {
		return "Welcome to nexmart";
	}

}
