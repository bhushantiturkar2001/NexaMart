package com.nexamart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexamart.modal.WishList;

public interface WishListRepository extends JpaRepository<WishList, Long> {
	
	WishList findByUserId(Long userId);

}
