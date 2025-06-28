package com.Makerspace.MakerspaceInventoryApp.Item;

import com.Makerspace.MakerspaceInventoryApp.Inventory.Inventory;
import lombok.Data;

@Data
public class ItemCreationDTO {
    private Item item;
    private Inventory inventory;
}

