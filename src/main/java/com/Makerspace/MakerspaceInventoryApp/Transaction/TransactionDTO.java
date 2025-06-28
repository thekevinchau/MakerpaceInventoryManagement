package com.Makerspace.MakerspaceInventoryApp.Transaction;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class TransactionDTO {
    private Integer transaction_id;
    private Integer transaction_inventory_id;
    private Integer quantity;
    private LocalDateTime time_of_transaction;
    private String domain;  // Added field from Inventory
    private String item_name;  // Added field from Item
    private String unit_of_measurement;  // Added field from Item

    public TransactionDTO(Transaction transaction, String domain, String item_name, String unit_of_measurement) {
        this.transaction_id = transaction.getTransaction_id();
        this.transaction_inventory_id = transaction.getTransaction_inventory_id();
        this.quantity = transaction.getQuantity();
        this.time_of_transaction = transaction.getTime_of_transaction();
        this.domain = domain;
        this.item_name = item_name;
        this.unit_of_measurement = unit_of_measurement;
    }
}