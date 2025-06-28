package com.Makerspace.MakerspaceInventoryApp.Transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Sort;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class GetTransactionService {

    private final TransactionRepository transactionRepository;

    public GetTransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Get all transactions with domain, item_name, and unit_of_measurement
    public ResponseEntity<List<TransactionDTO>> getAllTransactions() {
        List<TransactionDTO> transactionDTOs = transactionRepository.findAllTransactionsWithDetails();
        return ResponseEntity.status(HttpStatus.OK).body(transactionDTOs);
    }

    // Get paginated transactions with details
    public ResponseEntity<Page<TransactionDTO>> getPaginatedTransactions(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionDTO> transactionDTOs = transactionRepository.findAllTransactionsWithDetails(pageable);
        return ResponseEntity.ok(transactionDTOs);
    }

    // Get transaction by ID with details
    public ResponseEntity<TransactionDTO> getTransactionById(Integer id) {
        Optional<TransactionDTO> transactionDTO = transactionRepository.findTransactionByIdWithDetails(id);
        return transactionDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    public ResponseEntity<Page<TransactionDTO>> getPageableTransactionsSearchDomain(int page, int size, String searchTerm, String domain, String sortBy, String direction) {
        Page<TransactionDTO> transactionDTO;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            searchTerm = "%" + searchTerm + "%"; // Wildcard for partial match
        }

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = size == -1 ? Pageable.unpaged() : PageRequest.of(page, size, sort);

        if (domain.equalsIgnoreCase("All")) {
            // If domain is "All", don't filter by domain
            if (searchTerm != null && !searchTerm.isEmpty()) {
                transactionDTO = transactionRepository.findPaginatedTransactionsSearch(pageable, searchTerm);
            } else {
                transactionDTO = transactionRepository.findAllTransactionsWithDetails(pageable);
            }
        } else {
            // Filter by domain
            if (searchTerm != null && !searchTerm.isEmpty()) {
                transactionDTO = transactionRepository.findPaginatedTransactionsSearchDomain(pageable, searchTerm, domain);
            } else {
                transactionDTO = transactionRepository.findPaginatedTransactionsDomain(pageable, domain);
            }
        }
        return ResponseEntity.ok(transactionDTO);
    }


    // Get paginated transactions with details
    public ResponseEntity<Page<TransactionDTO>> getPaginatedTransactionsDates(int page, int size, LocalDateTime startDate, LocalDateTime endDate) {
        Pageable pageable = PageRequest.of(page, size);
        Page<TransactionDTO> transactionDTOs = transactionRepository.findPaginatedTransactionsDates(pageable, startDate, endDate);
        return ResponseEntity.ok(transactionDTOs);
    }


    public ResponseEntity<Page<TransactionDTO>> getPageableTransactionsSearchDomainDates(int page, int size, String searchTerm, String domain, String sortBy, String direction, LocalDateTime startDate, LocalDateTime endDate) {
        Page<TransactionDTO> transactionDTO;

        if (searchTerm != null && !searchTerm.isEmpty()) {
            searchTerm = "%" + searchTerm + "%"; // Wildcard for partial match
        }

        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
        Pageable pageable = size == -1 ? Pageable.unpaged() : PageRequest.of(page, size, sort);

        if (domain.equalsIgnoreCase("All")) {
            if (startDate != null && endDate != null){
                // If domain is "All", don't filter by domain
                // if dates are not empty
                if (searchTerm != null && !searchTerm.isEmpty()) {
                    transactionDTO = transactionRepository.findPaginatedTransactionsSearchDates(pageable, searchTerm, startDate, endDate);
                } else {
                    transactionDTO = transactionRepository.findPaginatedTransactionsDates(pageable, startDate, endDate);
                }
            } else {
                // If domain is "All", don't filter by domain
                // if dates are empty
                if (searchTerm != null && !searchTerm.isEmpty()) {
                    transactionDTO = transactionRepository.findPaginatedTransactionsSearch(pageable, searchTerm);
                } else {
                    transactionDTO = transactionRepository.findAllTransactionsWithDetails(pageable);
                }
            }
        } else {
            // Filter by domain
            // if dates are not empty
            if (startDate != null && endDate != null){
                if (searchTerm != null && !searchTerm.isEmpty()) {
                    transactionDTO = transactionRepository.findPaginatedTransactionsSearchDomainDates(pageable, searchTerm, domain, startDate, endDate);
                } else {
                    transactionDTO = transactionRepository.findPaginatedTransactionsDomainDates(pageable, domain, startDate, endDate);
                }
            }
            else {
                // Filter by domain
                // if dates are empty
                if (searchTerm != null && !searchTerm.isEmpty()) {
                    transactionDTO = transactionRepository.findPaginatedTransactionsSearchDomain(pageable, searchTerm, domain);
                } else {
                    transactionDTO = transactionRepository.findPaginatedTransactionsDomain(pageable, domain);
                }
            }
        }
        return ResponseEntity.ok(transactionDTO);
    }
}
