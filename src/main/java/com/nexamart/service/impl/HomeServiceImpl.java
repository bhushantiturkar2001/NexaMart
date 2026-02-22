package com.nexamart.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nexamart.domain.HomeCategorySection;
import com.nexamart.modal.Deal;
import com.nexamart.modal.Home;
import com.nexamart.modal.HomeCategory;
import com.nexamart.repository.DealRepository;
import com.nexamart.service.HomeService;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService {
	
	private final DealRepository dealRepository;

	@Override
	public Home createHomePageData(List<HomeCategory> allCategories) {

		List<HomeCategory> gridCategories = allCategories.stream()
				.filter(category -> category.getSection() == HomeCategorySection.GRID).collect(Collectors.toList());
		
		List<HomeCategory> shopByCategories = allCategories.stream()
				.filter(category -> category.getSection() == HomeCategorySection.SHOP_BY_CATEGORIES).collect(Collectors.toList());
		
		List<HomeCategory> electricCategories = allCategories.stream()
				.filter(category -> category.getSection() == HomeCategorySection.ELECTRIC_CATEGORIES).collect(Collectors.toList());
		
		List<HomeCategory> dealCategories = allCategories.stream()
				.filter(category -> category.getSection() == HomeCategorySection.DEALS).toList();
		
		List<Deal> createdDeals = new ArrayList<>();
		
		if (dealRepository.findAll().isEmpty()) {
			
			List<Deal> deals = allCategories.stream()
					.filter(category -> category.getSection() == HomeCategorySection.DEALS)
					.map(category -> new Deal(null,10,category))
					.collect(Collectors.toList());
			
			createdDeals = dealRepository.saveAll(deals);
			
		}else createdDeals = dealRepository.findAll();
		
		Home home = new Home();
		
		home.setGrid(gridCategories);
		home.setShopByCategories(shopByCategories);
		home.setElectricCategories(electricCategories);
		home.setDeals(createdDeals);
		home.setDealCategories(dealCategories);
		
		return home;
	}

}
