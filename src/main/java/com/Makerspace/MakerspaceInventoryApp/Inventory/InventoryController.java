package com.Makerspace.MakerspaceInventoryApp.Inventory;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*") // Allow all origins for this controller
@RequestMapping(value = "/api/inventory")
public class InventoryController {

    private final GetInventoryService GetInventoryService;
    private final UpdateInventoryService UpdateInventoryService;
    private final DeleteInventoryService DeleteInventoryService;

    public InventoryController(com.Makerspace.MakerspaceInventoryApp.Inventory.GetInventoryService getInventoryService, com.Makerspace.MakerspaceInventoryApp.Inventory.CreateInventoryService createInventoryService, com.Makerspace.MakerspaceInventoryApp.Inventory.UpdateInventoryService updateInventoryService, DeleteInventoryService deleteInventoryService) {
        GetInventoryService = getInventoryService;
        UpdateInventoryService = updateInventoryService;
        this.DeleteInventoryService = deleteInventoryService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<InventoryDTO>> getInventory(){
        return GetInventoryService.getInventory();
    }

    // @PreAuthorize("hasAnyRole('ADMIN', 'TECH')")
    @PutMapping("/{SKU}")
    public ResponseEntity<InventoryDTO> updateInventoryDetails(@PathVariable Integer SKU, @RequestBody Inventory inventory){
        return ResponseEntity.ok(UpdateInventoryService.updateInventoryDetails(SKU, inventory));
    }

    @PutMapping("/{SKU}/withdraw")
    public ResponseEntity<InventoryDTO> withdrawItem(@PathVariable Integer SKU, @RequestBody InventoryWithdrawRequest request){
        return ResponseEntity.ok(UpdateInventoryService.decrementQuantity(SKU, request.getAmount()));
    }

    @PutMapping("/{SKU}/deposit")
    public ResponseEntity<InventoryDTO> depositInventory(@PathVariable Integer SKU, @RequestBody InventoryWithdrawRequest request){
        return ResponseEntity.ok(UpdateInventoryService.incrementQuantity(SKU, request.getAmount()));
    }

    @DeleteMapping("/{sku}")
    public ResponseEntity<String> deleteInventory(@PathVariable Integer sku) {
        DeleteInventoryService.deleteInventory(sku);
        return ResponseEntity.ok("Inventory with SKU " + sku + " deleted successfully");
    }

}
