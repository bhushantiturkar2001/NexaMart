package com.nexamart.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nexamart.exception.ProductException;
import com.nexamart.modal.Product;
import com.nexamart.modal.Seller;
import com.nexamart.request.CreateProductRequest;
import com.nexamart.service.ProductService;
import com.nexamart.service.SellerService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/sellers/products")
public class SellerProductController {

	private final SellerService sellerService;
	private final ProductService productService;

	@GetMapping
	public ResponseEntity<List<Product>> getProductBySellerId(@RequestHeader("Authorization") String jwt)
			throws Exception {
		Seller seller = sellerService.getSellerProfile(jwt);

		List<Product> product = productService.getProductBySellerId(seller.getId());

		return new ResponseEntity<List<Product>>(product, HttpStatus.OK);
	}

    @PostMapping
	public ResponseEntity<Product> createProduct(@RequestBody CreateProductRequest request,
			@RequestHeader("Authorization") String jwt) throws Exception {
		Seller sellerProfile = sellerService.getSellerProfile(jwt);
		
		Product product = productService.createProduct(request, sellerProfile);
        return new ResponseEntity<>(product,HttpStatus.CREATED);
	}
    
    @DeleteMapping("/{productId}")
	public ResponseEntity<Void> deleteProduct(@PathVariable Long productId){
		try {
			productService.deleteProduct(productId);
			return new ResponseEntity<Void>(HttpStatus.OK);
			
		} catch (ProductException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} 
		
	}
    
    @PutMapping("/{productId}")
    public ResponseEntity<Product> updateProduct(@PathVariable Long id, @RequestBody Product product) throws ProductException{
    	Product updateProduct = productService.updateProduct(id, product);
    	return new ResponseEntity<Product>(updateProduct,HttpStatus.OK);
    	
    }

}
