package com.Makerspace.MakerspaceInventoryApp.Item;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/api/items")
@CrossOrigin(origins = "*") // Allow all origins for this controller

public class ItemController {

    private final GetItemService getItemService;
    private final CreateItemService CreateItemService;
    private final UpdateItemService UpdateItemService;
    private final ExcelService excelService;
    private final UploadService uploadService;

    public ItemController(GetItemService getItemService, com.Makerspace.MakerspaceInventoryApp.Item.CreateItemService createItemService, com.Makerspace.MakerspaceInventoryApp.Item.UpdateItemService updateItemService, ExcelService excelService, UploadService uploadService) {
        this.getItemService = getItemService;
        CreateItemService = createItemService;
        UpdateItemService = updateItemService;
        this.excelService = excelService;
        this.uploadService = uploadService;
    }

    // @PreAuthorize("hasAnyRole('ADMIN', 'TECH')")
    @PostMapping("/create")
    public ResponseEntity<ItemDTO> createItem(@RequestBody ItemCreationDTO itemCreation){
         return CreateItemService.createItem(itemCreation);
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadExcel(@RequestParam("file") MultipartFile file) {
        try {
            List<ItemCreationDTO> itemCreations = excelService.parseExcel(file);

            // Will throw and rollback if any item fails
            uploadService.uploadAll(itemCreations);

            return ResponseEntity.ok("Items uploaded successfully!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to process Excel file: " + e.getMessage());
        }
    }




    //return paginated items
    @GetMapping("/paginated")
    public ResponseEntity<Page<ItemDTO>> getPaginatedItems(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "") String searchTerm,
        @RequestParam(defaultValue = "All") String domain
    ) {
        return getItemService.getPaginatedItems(page, size, searchTerm, domain);
    }

    // Get all items
    @GetMapping("/all")
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        return getItemService.getAllItems();
    }

    //get items that need restocking
    @GetMapping("/low-stock")
    public ResponseEntity<List<ItemDTO>> getLowStockItems() {
        return getItemService.getLowStockItems();
    }

    @GetMapping("/low-stock/paginated")
    public ResponseEntity<Page<ItemDTO>> getLowStockItemsPaginated(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return getItemService.getLowStockItemsPaginated(page, size);
    }

    // Get item by ID
    @GetMapping("/{sku}")
    public ResponseEntity<ItemDTO> getItemById(@PathVariable Integer sku) {
        return getItemService.getItemBySKU(sku);
    }

    //Get item by query parameters
    @GetMapping()
    public ResponseEntity<List<ItemDTO>> getItemByQueryParams(@RequestParam Map<String, String> params){
        return getItemService.getItemsByQueryParams(params);
    }

    //sort by ohq, mohq, or unit_cost
    // @GetMapping("/sorted")
    // public ResponseEntity<Page<Item>> getSortedItems(
    //         @RequestParam(defaultValue = "ohq") String sortBy,
    //         @RequestParam(defaultValue = "asc") String direction,
    //         @RequestParam(defaultValue = "0") int page,
    //         @RequestParam(defaultValue = "10") int size) {

    //     Page<Item> sortedItems = getItemService.getSortedItems(sortBy, direction, page, size);
    //     return ResponseEntity.ok(sortedItems);
    // }

    // @PreAuthorize("hasAnyRole('ADMIN', 'TECH')")
    @PutMapping("/{SKU}")
    public ResponseEntity<ItemDTO> updateItemDetails(@PathVariable Integer SKU, @RequestBody Item item){
        return UpdateItemService.updateItemDetails(SKU, item);
    }

    //new route that combines searching and sorting
    @GetMapping("/sorted-search")
    public ResponseEntity<Page<ItemDTO>> getSortedAndFilteredItems(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "") String searchTerm,
            @RequestParam(defaultValue = "All") String domain,
            @RequestParam(defaultValue = "ohq") String sortBy,
            @RequestParam(defaultValue = "asc") String direction
    ) {
        return getItemService.getSortedAndFilteredItems(page, size, searchTerm, domain, sortBy, direction);
    }


}