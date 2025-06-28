package com.Makerspace.MakerspaceInventoryApp.Item;

import com.Makerspace.MakerspaceInventoryApp.Inventory.Inventory;
import com.Makerspace.MakerspaceInventoryApp.Inventory.InventoryDTO;
import lombok.Data;

@Data
public class ItemDTO {
    //private Integer item_id;
    private Integer SKU;
    private String item_name;
    private String description;
    private InventoryDTO inventoryDTO;
    private String unit_of_measurement;

    public ItemDTO(Item item) {
        //this.item_id = item.getItem_id();
        this.SKU = item.getSku();
        this.item_name = item.getItem_name();
        this.inventoryDTO = new InventoryDTO(item.getInventory());
        this.description = item.getDescription();
        this.unit_of_measurement = item.getUnit_of_measurement();
    }
}