package com.Makerspace.MakerspaceInventoryApp.Inventory;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class CreateInventoryService {

    private final InventoryRepository InventoryRepository;
    private final InventoryValidator InventoryValidator;

    public CreateInventoryService(com.Makerspace.MakerspaceInventoryApp.Inventory.InventoryRepository inventoryRepository, com.Makerspace.MakerspaceInventoryApp.Inventory.InventoryValidator inventoryValidator, com.Makerspace.MakerspaceInventoryApp.Inventory.InventoryValidator inventoryValidator1) {
        InventoryRepository = inventoryRepository;
        InventoryValidator = inventoryValidator1;
    }

    /*
        - We want to generate the SKU number based on the last used SKU in a particular domain.
        - If using 3D printer and we have three items there already using SKUs 1000-1003, then we want to get the SKU of the last item
        which would be 1003 and then add 1 to that.
            - May need to implement a custom JPA query in the Inventory Repository to retrieve only the last occupying item
                - select sku number from inventory where domain is equal to whatever domain and retrieve the last one
        - Also need to handle cases of the item possibly being the first entry in the domain (create it)
        or the last entry in the domain (throw an error stating there is no more space)
    */
    private Integer generateSKU(String domain){
        Map<String, Integer> skuMapping = new HashMap<>();
        skuMapping.put("Shop", 1000);
        skuMapping.put("3D Printer", 2000);
        skuMapping.put("Laser", 3000);
        skuMapping.put("Water Jet", 4000);
        skuMapping.put("Vinyl Cutting", 5000);
        skuMapping.put("Maker Tables", 6000);
        skuMapping.put("Textiles", 7000);
        skuMapping.put("Fasteners", 8000);
        skuMapping.put("Materials", 9000);

        if (!skuMapping.containsKey(domain)){
            throw new BadRequestException("The domain " + domain + " does not exist!");
        }

        int startingSKUNumberFromDomain = skuMapping.get(domain);
        //Need to check if the new item created is the first entry of its respective domain
        if (InventoryRepository.findBySKU(startingSKUNumberFromDomain).isEmpty()){ //if there is no current entry for the 1000/2000/3000, etc
            return skuMapping.get(domain); //we're just going to return the starting value for that domain
        }
        else if (InventoryRepository.findBySKU(startingSKUNumberFromDomain + 999).isPresent()){
            throw new BadRequestException(domain + "is at capacity. Please consider reassigning SKU numbers.");
        }

        int lastUsedSKU = InventoryRepository.findLastSKUByDomain(domain);
        return lastUsedSKU + 1;
    }

    //Grabs the inventory object from the request, validates it, and sets the SKU based on a precomputed list of unused SKUs.
    public Inventory createInventory(Inventory inventory){
        InventoryValidator.validateInventoryInput(inventory);
        inventory.setSKU(generateSKU(inventory.getDomain()));
        inventory.setLast_update(LocalDateTime.now());
        return InventoryRepository.save(inventory);
    }

}
