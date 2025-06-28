package com.Makerspace.MakerspaceInventoryApp.Transaction;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Makerspace.MakerspaceInventoryApp.Item.ItemDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
// import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;

// import org.springframework.data.domain.Pageable;
// import org.springframework.data.domain.Sort;
// import org.springframework.format.annotation.DateTimeFormat;

// import java.util.List;
// import java.sql.Timestamp;
// import java.time.LocalDateTime;

@RestController
@RequestMapping(value = "/api/transactions")
@CrossOrigin(origins = "*") // Allow all origins for this controller
public class TransactionController {

    private final GetTransactionService getTransactionService;

    public TransactionController(GetTransactionService getTransactionService) {
        this.getTransactionService = getTransactionService;
    }

    // Get all transactions with domain, item_name, and unit_of_measurement
    @GetMapping("/all")
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        return getTransactionService.getAllTransactions();
    }

    // Get paginated transactions with domain, item_name, and unit_of_measurement
    @GetMapping("/paginated")
    public ResponseEntity<Page<TransactionDTO>> getPaginatedTransactions(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return getTransactionService.getPaginatedTransactions(page, size);
    }

    @GetMapping("/paginated/search")
    public ResponseEntity<Page<TransactionDTO>> getPaginatedTransactionsSearchDomain(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "") String searchTerm,
        @RequestParam(defaultValue = "") String domain,
        @RequestParam(defaultValue = "transaction_id") String sortBy,
        @RequestParam(defaultValue = "asc") String direction
    ) {
        return getTransactionService.getPageableTransactionsSearchDomain(page, size, searchTerm, domain, sortBy, direction);
    }

    @GetMapping("/paginated/search/dates")
    public ResponseEntity<Page<TransactionDTO>> getPaginatedTransactionsSearchDomainDates(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "") String searchTerm,
        @RequestParam(defaultValue = "") String domain,
        @RequestParam(defaultValue = "transaction_id") String sortBy,
        @RequestParam(defaultValue = "asc") String direction,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime startDate,
        @RequestParam(required = false)
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
        LocalDateTime endDate
    ) {
        
        return getTransactionService.getPageableTransactionsSearchDomainDates(page, size, searchTerm, domain, sortBy, direction, startDate, endDate);
    }

    // Get transaction by ID
    @GetMapping("/{id}")
    public ResponseEntity<TransactionDTO> getTransactionById(@PathVariable Integer id) {
        return getTransactionService.getTransactionById(id);
    }

    @GetMapping("/dates")
    public ResponseEntity<Page<TransactionDTO>> getTransactionByDates(
    @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size,
    @RequestParam(required = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime startDate,
    @RequestParam(required = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    LocalDateTime endDate) {
        return getTransactionService.getPaginatedTransactionsDates(page, size, startDate, endDate);
    }

}