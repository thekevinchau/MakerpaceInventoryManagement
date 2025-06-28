package com.Makerspace.MakerspaceInventoryApp.Order;

import com.Makerspace.MakerspaceInventoryApp.Item.ItemDTO;
import com.Makerspace.MakerspaceInventoryApp.Supplier.Supplier;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

import com.Makerspace.MakerspaceInventoryApp.Item.Item;
import lombok.Data;

@Data
public class OrderDTO {
    private Integer orderId;
    private ItemDTO itemDTO;
    private Integer quantity;
    private LocalDateTime orderDate;
    private LocalDateTime receivedDate;
    private String status;

    public OrderDTO(Order order) {
        this.orderId = order.getOrderId();
        this.itemDTO = new ItemDTO(order.getItem());
        this.quantity = order.getQuantity();
        this.orderDate = order.getOrderDate();
        this.receivedDate = order.getReceivedDate();
        this.status = order.getStatus();
    }
}
