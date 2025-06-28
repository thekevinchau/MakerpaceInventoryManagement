package com.Makerspace.MakerspaceInventoryApp.Item;

import com.Makerspace.MakerspaceInventoryApp.Inventory.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateItemService {

    private final ItemRepository ItemRepository;

    public UpdateItemService(com.Makerspace.MakerspaceInventoryApp.Item.ItemRepository itemRepository, com.Makerspace.MakerspaceInventoryApp.Inventory.UpdateInventoryService updateInventoryService, com.Makerspace.MakerspaceInventoryApp.Inventory.InventoryRepository inventoryRepository, com.Makerspace.MakerspaceInventoryApp.Inventory.GetInventoryService getInventoryService) {
        ItemRepository = itemRepository;
    }


    public ResponseEntity<ItemDTO> updateItemDetails(Integer sku, Item item){
        Optional<Item> queriedItem = ItemRepository.findBySku(sku);
        if (queriedItem.isPresent()){
            Item oldItem = queriedItem.get();
            oldItem.setItem_name(item.getItem_name());
            oldItem.setDescription(item.getDescription());
            oldItem.setUnit_of_measurement(item.getUnit_of_measurement());
            return ResponseEntity.status(HttpStatus.OK).body(new ItemDTO(ItemRepository.save(oldItem)));
        }
        throw new ItemNotFoundException("Unable to find item to update!");
    }
}
