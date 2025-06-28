package com.Makerspace.MakerspaceInventoryApp.Order;

import com.Makerspace.MakerspaceInventoryApp.Exceptions.GlobalBadRequestException;
import com.Makerspace.MakerspaceInventoryApp.Exceptions.GlobalNotFoundException;
import com.Makerspace.MakerspaceInventoryApp.Item.Item;
import com.Makerspace.MakerspaceInventoryApp.Item.ItemRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Service
public class CreateOrderService {

    private final OrderRepository OrderRepository;
    private final ItemRepository ItemRepository;

    public CreateOrderService(com.Makerspace.MakerspaceInventoryApp.Order.OrderRepository orderRepository, com.Makerspace.MakerspaceInventoryApp.Item.ItemRepository itemRepository) {
        OrderRepository = orderRepository;
        ItemRepository = itemRepository;
    }

    public ResponseEntity<OrderDTO> createOrder(OrderPOSTRequest request){
        Order order = new Order();
        Optional<Item> queriedItem = ItemRepository.findBySku(request.getSku());
        Optional<Order> existingOrder = OrderRepository.findByItem_Sku(request.getSku());
        if (existingOrder.isPresent()) {
            throw new GlobalBadRequestException("Order Already Exists!");
        }
        if (queriedItem.isEmpty()){
            throw new GlobalNotFoundException("Item with SKU " + request.getSku() + " not found.");
        }
        order.setItem(queriedItem.get());
        order.setQuantity(request.getQuantity());
        order.setStatus("Ordered");
        order.setOrderDate(LocalDateTime.now());
        Order savedOrder = OrderRepository.save(order);
        return ResponseEntity.ok(new OrderDTO(savedOrder));
    }
}
