package com.nexamart.service;

import java.util.List;

import com.nexamart.modal.Home;
import com.nexamart.modal.HomeCategory;

public interface HomeService {
	// Getting all category
	public Home createHomePageData(List<HomeCategory> allCategories);

}
