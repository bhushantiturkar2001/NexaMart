package com.nexamart.service;

import java.util.List;
import java.util.Set;

import com.nexamart.domain.OrderStatus;
import com.nexamart.modal.Address;
import com.nexamart.modal.Cart;
import com.nexamart.modal.Order;
import com.nexamart.modal.OrderItem;
import com.nexamart.modal.User;

public interface OrderService {

	Set<Order> createOrder(User user, Address shippingAddress, Cart cart);

	Order findOrderById(long id) throws Exception;

	List<Order> userOrderHistory(Long userId);

	List<Order> sellersOrder(Long sellerId);

	Order updateOrderStatus(long id, OrderStatus status) throws Exception;

	Order cancelOrder(Long orderId, User user) throws Exception;
	
	OrderItem getOrderItemById(Long id) throws Exception;

}
