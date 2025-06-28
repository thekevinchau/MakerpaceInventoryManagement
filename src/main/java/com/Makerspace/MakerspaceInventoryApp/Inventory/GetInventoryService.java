package com.Makerspace.MakerspaceInventoryApp.Inventory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.hibernate.Session;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GetInventoryService {

    private final InventoryRepository InventoryRepository;

    public GetInventoryService(com.Makerspace.MakerspaceInventoryApp.Inventory.InventoryRepository inventoryRepository, com.Makerspace.MakerspaceInventoryApp.Item.ItemRepository itemRepository) {
        InventoryRepository = inventoryRepository;
    }

    public ResponseEntity<List<InventoryDTO>> getInventory(){
        List<Inventory> inventory = InventoryRepository.findAll();
        List<InventoryDTO> inventoryDTOS = inventory.stream().map(InventoryDTO::new).toList();
        return ResponseEntity.status(HttpStatus.OK).body(inventoryDTOS);
    }

    public Inventory getBySKU(Integer SKU){
        Optional<Inventory> inventory = InventoryRepository.findBySKU(SKU);
        if (inventory.isPresent()){
            return inventory.get();
        }
        throw new InventoryNotFoundException("Inventory status not found!");
    }

    public ResponseEntity<List<InventoryDTO>> getInventoryByDomain(String domain){
        List<Inventory> inventory = InventoryRepository.findByDomain(domain);
        List<InventoryDTO> inventoryDTO = inventory.stream().map(InventoryDTO::new).toList();
        return ResponseEntity.ok(inventoryDTO);
    }


}
