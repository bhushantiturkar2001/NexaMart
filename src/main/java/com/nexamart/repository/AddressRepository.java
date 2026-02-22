package com.nexamart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexamart.modal.Address;

public interface AddressRepository extends JpaRepository<Address, Long> {

}
