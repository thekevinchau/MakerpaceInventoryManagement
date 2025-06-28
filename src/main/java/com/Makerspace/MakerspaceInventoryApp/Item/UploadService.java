package com.Makerspace.MakerspaceInventoryApp.Item;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// BulkUploadService.java
@Service
public class UploadService {

    private final CreateItemService createItemService;

    public UploadService(CreateItemService createItemService) {
        this.createItemService = createItemService;
    }

    @Transactional
    public void uploadAll(List<ItemCreationDTO> itemCreations) {
        for (ItemCreationDTO itemCreation : itemCreations) {
            // If any throw an exception, the whole transaction rolls back
            ResponseEntity<ItemDTO> response = createItemService.createItem(itemCreation);
            if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
                throw new RuntimeException("Item creation failed for: " + itemCreation.getItem().getItem_name());
            }
        }
    }
}

