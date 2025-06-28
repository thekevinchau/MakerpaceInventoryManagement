package com.Makerspace.MakerspaceInventoryApp.Transaction;

import com.Makerspace.MakerspaceInventoryApp.Inventory.Inventory;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "Transaction", schema = "makerspace_inventory")
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private Integer transaction_id;

    @Column(name = "transaction_inventory_id", nullable = false)
    private Integer transaction_inventory_id;

    @ManyToOne
    @JoinColumn(name = "transaction_inventory_id", insertable = false, updatable = false)
    private Inventory inventory;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "time_of_transaction")
    private LocalDateTime time_of_transaction;
}
