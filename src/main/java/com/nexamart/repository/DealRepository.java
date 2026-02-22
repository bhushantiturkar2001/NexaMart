package com.nexamart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexamart.modal.Deal;

public interface DealRepository extends JpaRepository<Deal, Long> {

}
