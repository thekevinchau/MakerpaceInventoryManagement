package com.Makerspace.MakerspaceInventoryApp.Item;

import com.Makerspace.MakerspaceInventoryApp.Exceptions.GlobalNotFoundException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

//pagination
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.PageImpl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Sort;

@Service
public class GetItemService {

    private final ItemRepository itemRepository;

    public GetItemService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    // Get all items
    public ResponseEntity<List<ItemDTO>> getAllItems() {
        List<Item> items = itemRepository.findAll();
        List<ItemDTO> itemDTOs = items.stream().map(ItemDTO::new).toList();
        return ResponseEntity.status(HttpStatus.OK).body(itemDTOs);
    }

    // Get paginated items
    public ResponseEntity<Page<ItemDTO>> getPaginatedItems(int page, int size, String searchTerm, String domain) {
        if (searchTerm != null && !searchTerm.isEmpty()) {
            searchTerm = "%" + searchTerm + "%"; // Wildcard for partial matching
        }
        
        Pageable pageable = size == -1 ? Pageable.unpaged() : PageRequest.of(page, size);
        
        Page<Item> items;
        
        if (domain.equalsIgnoreCase("All")) {
            // If domain is "All", don't filter by domain
            if (searchTerm != null && !searchTerm.isEmpty()) {
                items = itemRepository.findBySearchTerm(searchTerm, pageable);
            } else {
                items = itemRepository.findAll(pageable);
            }
        } else {
            // Filter by domain
            if (searchTerm != null && !searchTerm.isEmpty()) {
                items = itemRepository.findBySearchTermAndDomain(searchTerm, domain, pageable);
            } else {
                items = itemRepository.findByDomain(domain, pageable);
            }
        }
    
        Page<ItemDTO> itemDTOs = items.map(ItemDTO::new);
        return ResponseEntity.ok(itemDTOs);
    }
    

    // Get item by ID
    public ResponseEntity<ItemDTO> getItemBySKU(Integer SKU) {
        Optional<Item> item = itemRepository.findBySku(SKU);
        if (item.isPresent()) {
            ItemDTO itemDTO = new ItemDTO(item.get());
            return ResponseEntity.ok(itemDTO);
        }
        throw new GlobalNotFoundException("Item not found."); // Return 404 if not found
    }

    // Filter for items needing restock
    public ResponseEntity<List<ItemDTO>> getLowStockItems() {
        List<Item> items = itemRepository.findLowStockItems();
        List<ItemDTO> itemDTOs = items.stream().map(ItemDTO::new).toList();
        return ResponseEntity.ok(itemDTOs);
    }

    // Filter for items needing restock (paginated)
    public ResponseEntity<Page<ItemDTO>> getLowStockItemsPaginated(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<Item> items = itemRepository.findLowStockItemsPaginated(pageable);
        Page<ItemDTO> itemDTOs = items.map(ItemDTO::new);
        return ResponseEntity.ok(itemDTOs);
    }


    public ResponseEntity<List<ItemDTO>> getItemsByQueryParams(Map<String, String> params){
        Specification<Item> spec = Specification.where(null);

        for (Map.Entry<String,String> entry: params.entrySet()){
            if (entry.getKey().equals("domain")){
                spec = spec.and(ItemSpecification.hasDomainName(entry.getValue()));
            }
            if (entry.getKey().equals("type")){
                spec = spec.and(ItemSpecification.hasTypeName(entry.getValue()));
            }
            if (entry.getKey().equals("brand")){
                spec = spec.and(ItemSpecification.hasBrandName(entry.getValue()));
            }
        }
        List<Item> items = itemRepository.findAll(spec);
        List<ItemDTO> itemDTOs = items.stream().map(ItemDTO::new).toList();
        return ResponseEntity.status(HttpStatus.OK).body(itemDTOs);

    }

    //get items sorted
    // public Page<Item> getSortedItems(String sortBy, String direction, int page, int size) {
    //     Sort sort = Sort.by(Sort.Direction.fromString(direction), "inventory." + sortBy);
    //     Pageable pageable = PageRequest.of(page, size, sort);
    //     return itemRepository.findAllWithInventory(pageable);
    // }

    //function that combines searching and sorting
    public ResponseEntity<Page<ItemDTO>> getSortedAndFilteredItems(
        int page,
        int size,
        String searchTerm,
        String domain,
        String sortBy,
        String direction
) {
    if (searchTerm != null && !searchTerm.isEmpty()) {
        searchTerm = "%" + searchTerm + "%"; // Wildcard for partial match
    }

    Sort sort = Sort.by(Sort.Direction.fromString(direction), "inventory." + sortBy);
    Pageable pageable = size == -1 ? PageRequest.of(0, Integer.MAX_VALUE, sort) : PageRequest.of(page, size, sort);

    Page<Item> items;

    if (domain.equalsIgnoreCase("All")) {
        if (searchTerm != null && !searchTerm.isEmpty()) {
            items = itemRepository.findBySearchTerm(searchTerm, pageable);
        } else {
            items = itemRepository.findAllWithInventory(pageable); // <--- fixed
        }
    } else {
        if (searchTerm != null && !searchTerm.isEmpty()) {
            items = itemRepository.findBySearchTermAndDomain(searchTerm, domain, pageable);
        } else {
            items = itemRepository.findByDomain(domain, pageable);
        }
    }

    Page<ItemDTO> itemDTOs = items.map(ItemDTO::new);
    return ResponseEntity.ok(itemDTOs);
}



}