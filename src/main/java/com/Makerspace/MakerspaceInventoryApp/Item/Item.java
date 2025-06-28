package com.Makerspace.MakerspaceInventoryApp.Item;

import com.Makerspace.MakerspaceInventoryApp.Inventory.Inventory;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "Item", schema = "makerspace_inventory")
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Integer item_id;

    @Column(name = "item_name")
    private String item_name;

    @Column(name = "description")
    private String description;

    @Column(name = "unit_of_measurement")
    private String unit_of_measurement;

    @Column(name= "SKU")
    private Integer sku;

    @OneToOne(cascade = CascadeType.REMOVE, orphanRemoval = true)
    @JoinColumn(name="SKU", insertable = false, updatable = false, nullable = false)
    private Inventory inventory;

}
