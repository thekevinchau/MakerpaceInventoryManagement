package com.Makerspace.MakerspaceInventoryApp.Item;

import com.Makerspace.MakerspaceInventoryApp.Inventory.CreateInventoryService;
import com.Makerspace.MakerspaceInventoryApp.Inventory.Inventory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CreateItemService {
    private final CreateInventoryService CreateInventoryService;
    private final ItemRepository ItemRepository;


    public CreateItemService(com.Makerspace.MakerspaceInventoryApp.Inventory.CreateInventoryService createInventoryService, com.Makerspace.MakerspaceInventoryApp.Item.ItemRepository itemRepository1) {
        CreateInventoryService = createInventoryService;
        ItemRepository = itemRepository1;
    }

    //Helper function to help separate code inside createItem function so Phil can add his transaction code freely.
    private ItemDTO createItemHelper(ItemCreationDTO itemCreation){
        Inventory savedInventory = CreateInventoryService.createInventory(itemCreation.getInventory());
        Item newItem = itemCreation.getItem();
        newItem.setSku(savedInventory.getSKU());
        newItem.setInventory(savedInventory);
        Item savedItem = ItemRepository.save(newItem);
        return new ItemDTO(savedItem);
    }

    public ResponseEntity<ItemDTO> createItem(ItemCreationDTO itemCreation){
        //validateItem inputs here
        ItemDTO itemDTO = createItemHelper(itemCreation);
        return ResponseEntity.status(HttpStatus.OK).body(itemDTO);
    }
}
