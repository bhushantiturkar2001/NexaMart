package com.nexamart.service;

import com.nexamart.modal.Product;
import com.nexamart.modal.User;
import com.nexamart.modal.WishList;

public interface WishListService {
	
	WishList createWishList(User user);
	WishList getWishListByUserId(User user);
    WishList addProductToWishList(User user,Product product);

}
