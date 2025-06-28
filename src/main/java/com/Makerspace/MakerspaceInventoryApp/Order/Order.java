package com.Makerspace.MakerspaceInventoryApp.Order;

import com.Makerspace.MakerspaceInventoryApp.Item.Item;
import com.Makerspace.MakerspaceInventoryApp.Supplier.Supplier;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Entity
@Table(name = "Order", schema = "makerspace_inventory") // Specify the schema if it's not the default
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Integer orderId;

    @ManyToOne
    @JoinColumn(name = "sku", referencedColumnName = "sku")
    private Item item;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "last_ordered", columnDefinition = "DATE DEFAULT CURRENT_DATE")
    private LocalDateTime orderDate;

    @Column(name="date_received")
    private LocalDateTime receivedDate;

    @Column(name = "status", length = 25)
    private String status;

    @Column(name="amount_received")
    private Integer amountReceived;
}