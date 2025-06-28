package com.Makerspace.MakerspaceInventoryApp.Inventory;

import com.Makerspace.MakerspaceInventoryApp.Item.Item;
import com.Makerspace.MakerspaceInventoryApp.Transaction.Transaction;
import com.Makerspace.MakerspaceInventoryApp.Supplier.Supplier;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "Inventory", schema = "makerspace_inventory")
public class Inventory {

    @Id
    @Column(name = "SKU")
    private Integer SKU;

    @Column(name = "part_number")
    private String part_number;

    @Column(name = "domain")
    private String domain;

    @Column(name = "type")
    private String type;

    @Column(name = "brand")
    private String brand;

    @Column(name = "ohq")
    private Integer ohq; // on hand quantity

    @Column(name = "mohq")
    private Integer mohq; // minimum on hand quantity

    @Column(name = "moq")
    private Integer moq; // minimum order quantity


    @Column(name = "unit_cost")
    private Double unit_cost;

    @Column(name = "link")
    private String link;

    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss")
    @Column(name = "last_update")
    private LocalDateTime last_update;

    @OneToOne(mappedBy = "inventory" , cascade = CascadeType.REMOVE)
    private Item item;

    @OneToMany(mappedBy = "inventory" , cascade = CascadeType.REMOVE)
    private List<Transaction> transactions = new ArrayList<>();
}