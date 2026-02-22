package com.nexamart.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nexamart.modal.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
  Category findByCategoryId (String categoryId);
}
