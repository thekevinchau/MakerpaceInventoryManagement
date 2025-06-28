// DeleteInventoryService.java
package com.Makerspace.MakerspaceInventoryApp.Inventory;

import com.Makerspace.MakerspaceInventoryApp.Item.ItemRepository;
import com.Makerspace.MakerspaceInventoryApp.Transaction.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DeleteInventoryService {
    private final InventoryRepository inventoryRepository;
    private final ItemRepository itemRepository;
    private final TransactionRepository transactionRepository;

    public DeleteInventoryService(
        InventoryRepository inventoryRepository,
        ItemRepository itemRepository,
        TransactionRepository transactionRepository
    ) {
        this.inventoryRepository = inventoryRepository;
        this.itemRepository = itemRepository;
        this.transactionRepository = transactionRepository;
    }

    @Transactional
    public void deleteInventory(Integer sku) {
        Inventory inventory = inventoryRepository.findById(sku)
            .orElseThrow(() -> new RuntimeException("Inventory not found with SKU: " + sku));

        // Delete associated Item
        if (inventory.getItem() != null) {
            itemRepository.delete(inventory.getItem());
        }

        // Delete associated Transactions
        transactionRepository.deleteAll(inventory.getTransactions());

        // Delete the Inventory itself
        inventoryRepository.delete(inventory);
    }
}