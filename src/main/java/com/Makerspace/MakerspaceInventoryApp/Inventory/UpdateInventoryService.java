package com.Makerspace.MakerspaceInventoryApp.Inventory;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import com.Makerspace.MakerspaceInventoryApp.Transaction.Transaction;
import com.Makerspace.MakerspaceInventoryApp.Transaction.TransactionRepository; 
import java.sql.Timestamp; // For setting transaction timestamps


@Service
public class UpdateInventoryService {

    private final InventoryRepository InventoryRepository;
    private final InventoryValidator InventoryValidator;
    private final TransactionRepository TransactionRepository;

    public UpdateInventoryService(com.Makerspace.MakerspaceInventoryApp.Inventory.InventoryRepository inventoryRepository, com.Makerspace.MakerspaceInventoryApp.Inventory.InventoryValidator inventoryValidator, TransactionRepository transactionRepository) {
        InventoryRepository = inventoryRepository;
        InventoryValidator = inventoryValidator;
        TransactionRepository = transactionRepository;
    }

    public InventoryDTO decrementQuantity(Integer SKU, Integer amount) {
        Optional<Inventory> queriedInventory = InventoryRepository.findBySKU(SKU);
        if (queriedInventory.isEmpty()) {
            throw new InventoryNotFoundException("Inventory entity not found.");
        }
    
        Inventory inventory = queriedInventory.get();
        Integer updatedOHQ = inventory.getOhq() - amount;
    
        // Prevent negative OHQ values (edge case)
        if (updatedOHQ < 0) {
            throw new IllegalArgumentException("Cannot decrement OHQ below zero.");
        }
    
        inventory.setOhq(updatedOHQ);
        inventory.setLast_update(LocalDateTime.now());
        // create transaction on update
        Transaction transaction = new Transaction();
        transaction.setTransaction_inventory_id(inventory.getSKU()); 
        transaction.setQuantity(-amount);  // Negative for decrement
        transaction.setTime_of_transaction(LocalDateTime.now());
        TransactionRepository.save(transaction);
    
        // low stock slert
        if (updatedOHQ < inventory.getMohq()) {
            // Log warning, send email, etc.
            //alertService.sendLowStockAlert(inventory);
            System.out.println("Low OHQ");
        }
    
        Inventory savedInventory = InventoryRepository.save(inventory);
        return new InventoryDTO(savedInventory);
    }

    public InventoryDTO incrementQuantity(Integer SKU, Integer amount) {
        Optional<Inventory> queriedInventory = InventoryRepository.findBySKU(SKU);
        if (queriedInventory.isEmpty()) {
            throw new InventoryNotFoundException("Inventory entity not found.");
        }

        Inventory inventory = queriedInventory.get();
        Integer updatedOHQ = inventory.getOhq() + amount;

        // Prevent negative OHQ values (edge case)
        if (updatedOHQ < 0) {
            throw new IllegalArgumentException("Cannot decrement OHQ below zero.");
        }

        inventory.setOhq(updatedOHQ);
        inventory.setLast_update(LocalDateTime.now());
        // create transaction on update
        Transaction transaction = new Transaction();
        transaction.setTransaction_inventory_id(inventory.getSKU());
        transaction.setQuantity(+amount);  // positive for increment
        transaction.setTime_of_transaction(LocalDateTime.now());
        TransactionRepository.save(transaction);


        Inventory savedInventory = InventoryRepository.save(inventory);
        return new InventoryDTO(savedInventory);
    }
    

    public InventoryDTO updateInventoryDetails(Integer SKU, Inventory inventory){
        Optional<Inventory> queriedInventory = InventoryRepository.findBySKU(SKU);
        if (queriedInventory.isEmpty()){
            throw new InventoryNotFoundException("Inventory entity not found.");
        }
        Integer oldValue = queriedInventory.get().getOhq();
        InventoryValidator.validateInventoryInput(inventory);
        inventory.setSKU(queriedInventory.get().getSKU());
        inventory.setLast_update(LocalDateTime.now());

        //Create a Transaction on Update only if ohq has been updated
        if (inventory.getOhq() != oldValue) {
            Transaction transaction = new Transaction();
            transaction.setTransaction_inventory_id(inventory.getSKU());
            transaction.setQuantity(inventory.getOhq() - oldValue);
            transaction.setTime_of_transaction(LocalDateTime.now());
            TransactionRepository.save(transaction);
        }


        Inventory savedInventory = InventoryRepository.save(inventory);
        return new InventoryDTO(savedInventory);
    }

}
