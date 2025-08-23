package com.nexamart.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.nexamart.domain.OrderStatus;
import com.nexamart.domain.PaymentStatus;
import com.nexamart.modal.Address;
import com.nexamart.modal.Cart;
import com.nexamart.modal.CartItem;
import com.nexamart.modal.Order;
import com.nexamart.modal.OrderItem;
import com.nexamart.modal.User;
import com.nexamart.repository.AddressRepository;
import com.nexamart.repository.OrderItemRepository;
import com.nexamart.repository.OrderRepository;
import com.nexamart.service.OrderService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final OrderRepository orderRepository;
	private final AddressRepository addressRepository;
	private final OrderItemRepository orderItemRepository;

	@Override
	public Set<Order> createOrder(User user, Address shippingAddress, Cart cart) {
		if (user.getAddress().contains(shippingAddress)) {
			user.getAddress().add(shippingAddress);
		}

		Address address = addressRepository.save(shippingAddress);

		// brand 1 => 4 shirt == brand 2 => pants 3

		Map<Long, List<CartItem>> itemsBySellerMap = cart.getCartItem().stream()
				.collect(Collectors.groupingBy(item -> item.getProduct().getSeller().getId()));

		Set<Order> orders = new HashSet<>();

		for (Map.Entry<Long, List<CartItem>> entry : itemsBySellerMap.entrySet()) {
			Long seller = entry.getKey();
			List<CartItem> items = entry.getValue();

			int totalOrderPrice = items.stream().mapToInt(CartItem::getSellingPrice).sum();

			int totalItem = items.stream().mapToInt(CartItem::getQuantity).sum();

			Order createdOrder = new Order();

			createdOrder.setUser(user);
			createdOrder.setSellerId(seller); // check
			createdOrder.setTotalMrpPrice(totalOrderPrice);
			createdOrder.setTotalSellingPrice(totalOrderPrice);
			createdOrder.setTotalItem(totalItem);
			createdOrder.setShippingAddress(address);
			createdOrder.setOrderStatus(OrderStatus.PENDING);
			createdOrder.getPaymentDetails().setStatus(PaymentStatus.PENDING);

			Order savedOrder = orderRepository.save(createdOrder);
			orders.add(savedOrder);

			List<OrderItem> orderItems = new ArrayList<>();

			for (CartItem item : items) {
				OrderItem orderItem = new OrderItem();

				orderItem.setOrder(savedOrder);
				orderItem.setMrpPrice(item.getMrpPrice());
				orderItem.setProduct(item.getProduct());
				orderItem.setQuantity(item.getQuantity());
				orderItem.setSize(item.getSize());
				orderItem.setUserId(item.getUserId());
				orderItem.setSellingPrice(item.getSellingPrice());

				savedOrder.getOrderItems().add(orderItem);

				OrderItem savedOrderItem = orderItemRepository.save(orderItem);

				orderItems.add(savedOrderItem);

			}

		}

		return orders;

	}

	@Override
	public Order findOrderById(long id) throws Exception {

		return orderRepository.findById(id).orElseThrow(() -> new Exception("order not found..."));
	}

	@Override
	public List<Order> userOrderHistory(Long userId) {
		return orderRepository.findByUserId(userId);
	}

	@Override
	public List<Order> sellersOrder(Long sellerId) {

		return orderRepository.findBySellerId(sellerId);
	}

	@Override
	public Order updateOrderStatus(long id, OrderStatus status) throws Exception {
		Order order = findOrderById(id);
		order.setOrderStatus(status);
		return orderRepository.save(order);
	}

	@Override
	public Order cancelOrder(Long orderId, User user) throws Exception {
		Order order = findOrderById(orderId);

		if (!user.getId().equals(order.getUser().getId())) {
			throw new Exception("You dont have the acces to order.");
		}

		order.setOrderStatus(OrderStatus.CANCELLED);
		return orderRepository.save(order);
	}

	@Override
	public OrderItem getOrderItemById(Long id) throws Exception {

		return orderItemRepository.findById(id).orElseThrow(() -> new Exception("order item not exist."));
	}

}
