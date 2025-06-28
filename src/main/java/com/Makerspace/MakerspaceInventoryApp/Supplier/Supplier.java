package com.Makerspace.MakerspaceInventoryApp.Supplier;

import com.Makerspace.MakerspaceInventoryApp.Inventory.Inventory;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Supplier", schema = "makerspace_inventory")
public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "supplier_id")
    private Integer supplier_id; // Changed from String to Integer

    @Column(name = "supplier_name", nullable = false)
    private String supplier_name;

    @Column(name = "supplier_link")
    private String supplier_link;

}