package com.nexamart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexamart.modal.VerificationCode;



public interface VerificationCodeRepository extends JpaRepository<VerificationCode, Long> {
	
	VerificationCode  findByEmail(String email);

}
