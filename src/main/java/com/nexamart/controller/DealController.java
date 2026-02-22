package com.nexamart.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexamart.modal.Deal;
import com.nexamart.response.ApiResponse;
import com.nexamart.service.DealService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/deals")
public class DealController {

	private final DealService dealService;

	@PostMapping
	public ResponseEntity<Deal> createDeal(@RequestBody Deal deal) {

		Deal createDeals = dealService.createDeal(deal);
		return new ResponseEntity<>(createDeals, HttpStatus.ACCEPTED);

	}

	@PatchMapping("/{id}")
	public ResponseEntity<Deal> updateDeal(@PathVariable Long id, @RequestBody Deal deal) throws Exception {
		Deal updateDeal = dealService.updateDeal(deal, id);
		return ResponseEntity.ok(updateDeal);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<ApiResponse> deleteDeal(@PathVariable Long id) throws Exception {
		dealService.deleteDeal(id);

		ApiResponse response = new ApiResponse();
		response.setMessage("Deal deleted");

		return new ResponseEntity<>(response, HttpStatus.ACCEPTED);

	}

}
