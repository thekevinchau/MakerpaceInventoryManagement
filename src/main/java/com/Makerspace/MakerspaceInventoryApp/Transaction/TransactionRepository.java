package com.Makerspace.MakerspaceInventoryApp.Transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {

    @Query("""
    SELECT new com.Makerspace.MakerspaceInventoryApp.Transaction.TransactionDTO(
        t,
        inv.domain, i.item_name, i.unit_of_measurement
    )
    FROM Transaction t
    JOIN Inventory inv ON t.transaction_inventory_id = inv.SKU
    JOIN Item i ON i.sku = inv.SKU
    """)
    List<TransactionDTO> findAllTransactionsWithDetails();


    @Query("""
    SELECT new com.Makerspace.MakerspaceInventoryApp.Transaction.TransactionDTO(
        t,
        inv.domain, i.item_name, i.unit_of_measurement
    )
    FROM Transaction t
    JOIN Inventory inv ON t.transaction_inventory_id = inv.SKU
    JOIN Item i ON i.sku = inv.SKU
    """)
    Page<TransactionDTO> findAllTransactionsWithDetails(Pageable pageable);


    @Query("""
    SELECT new com.Makerspace.MakerspaceInventoryApp.Transaction.TransactionDTO(
        t,
        inv.domain, i.item_name, i.unit_of_measurement
    )
    FROM Transaction t
    JOIN Inventory inv ON t.transaction_inventory_id = inv.SKU
    JOIN Item i ON i.sku = inv.SKU
    WHERE t.transaction_id = :id
    """)
    Optional<TransactionDTO> findTransactionByIdWithDetails(@Param("id") Integer id);

    @Query("""
    SELECT new com.Makerspace.MakerspaceInventoryApp.Transaction.TransactionDTO(
        t,
        inv.domain, i.item_name, i.unit_of_measurement
    )
    FROM Transaction t
    JOIN Inventory inv ON t.transaction_inventory_id = inv.SKU
    JOIN Item i ON i.sku = inv.SKU
    WHERE inv.domain = :domain 
    AND (LOWER(i.item_name) LIKE LOWER(:searchTerm)
    OR LOWER(inv.domain) LIKE LOWER(:searchTerm)
    OR LOWER(i.unit_of_measurement) LIKE LOWER(:searchTerm)
    OR CAST(i.sku AS string) LIKE :searchTerm
    OR CAST(t.quantity AS string) LIKE :searchTerm
    OR CAST(t.transaction_id AS string) LIKE :searchTerm
    OR CAST(t.time_of_transaction AS string) LIKE :searchTerm)
    """)
    Page<TransactionDTO> findPaginatedTransactionsSearchDomain(Pageable pageable, @Param("searchTerm") String searchTerm, @Param("domain") String domain);

    @Query("""
    SELECT new com.Makerspace.MakerspaceInventoryApp.Transaction.TransactionDTO(
        t,
        inv.domain, i.item_name, i.unit_of_measurement
    )
    FROM Transaction t
    JOIN Inventory inv ON t.transaction_inventory_id = inv.SKU
    JOIN Item i ON i.sku = inv.SKU
    WHERE LOWER(i.item_name) LIKE LOWER(:searchTerm)
    OR LOWER(inv.domain) LIKE LOWER(:searchTerm)
    OR LOWER(i.unit_of_measurement) LIKE LOWER(:searchTerm)
    OR CAST(i.sku AS string) LIKE :searchTerm
    OR CAST(t.quantity AS string) LIKE :searchTerm
    OR CAST(t.transaction_id AS string) LIKE :searchTerm
    OR CAST(t.time_of_transaction AS string) LIKE :searchTerm
    """)
    Page<TransactionDTO> findPaginatedTransactionsSearch(Pageable pageable, @Param("searchTerm") String searchTerm);

    @Query("""
    SELECT new com.Makerspace.MakerspaceInventoryApp.Transaction.TransactionDTO(
        t,
        inv.domain, i.item_name, i.unit_of_measurement
    )
    FROM Transaction t
    JOIN Inventory inv ON t.transaction_inventory_id = inv.SKU
    JOIN Item i ON i.sku = inv.SKU
    WHERE inv.domain = :domain
    """)
    Page<TransactionDTO> findPaginatedTransactionsDomain(Pageable pageable, @Param("domain") String domain);

    @Query("""
    SELECT new com.Makerspace.MakerspaceInventoryApp.Transaction.TransactionDTO(
        t,
        inv.domain, i.item_name, i.unit_of_measurement
    )
    FROM Transaction t
    JOIN Inventory inv ON t.transaction_inventory_id = inv.SKU
    JOIN Item i ON i.sku = inv.SKU
    WHERE inv.domain = :domain 
    AND (LOWER(i.item_name) LIKE LOWER(:searchTerm)
    OR LOWER(inv.domain) LIKE LOWER(:searchTerm)
    OR LOWER(i.unit_of_measurement) LIKE LOWER(:searchTerm)
    OR CAST(i.sku AS string) LIKE :searchTerm
    OR CAST(t.quantity AS string) LIKE :searchTerm
    OR CAST(t.transaction_id AS string) LIKE :searchTerm
    OR CAST(t.time_of_transaction AS string) LIKE :searchTerm)
    AND (t.time_of_transaction BETWEEN :startDate AND :endDate)
    """)
    Page<TransactionDTO> findPaginatedTransactionsSearchDomainDates(Pageable pageable, @Param("searchTerm") String searchTerm, @Param("domain") String domain, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
        
    @Query("""
    SELECT new com.Makerspace.MakerspaceInventoryApp.Transaction.TransactionDTO(
        t,
        inv.domain, i.item_name, i.unit_of_measurement
    )
    FROM Transaction t
    JOIN Inventory inv ON t.transaction_inventory_id = inv.SKU
    JOIN Item i ON i.sku = inv.SKU
    WHERE (LOWER(i.item_name) LIKE LOWER(:searchTerm)
    OR LOWER(inv.domain) LIKE LOWER(:searchTerm)
    OR LOWER(i.unit_of_measurement) LIKE LOWER(:searchTerm)
    OR CAST(i.sku AS string) LIKE :searchTerm
    OR CAST(t.quantity AS string) LIKE :searchTerm
    OR CAST(t.transaction_id AS string) LIKE :searchTerm
    OR CAST(t.time_of_transaction AS string) LIKE :searchTerm)
    AND (t.time_of_transaction BETWEEN :startDate AND :endDate)
    """)
    Page<TransactionDTO> findPaginatedTransactionsSearchDates(Pageable pageable, @Param("searchTerm") String searchTerm, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    @Query("""
    SELECT new com.Makerspace.MakerspaceInventoryApp.Transaction.TransactionDTO(
        t,
        inv.domain, i.item_name, i.unit_of_measurement
    )
    FROM Transaction t
    JOIN Inventory inv ON t.transaction_inventory_id = inv.SKU
    JOIN Item i ON i.sku = inv.SKU
    WHERE t.time_of_transaction BETWEEN :startDate AND :endDate
    """)
    Page<TransactionDTO> findPaginatedTransactionsDates(Pageable pageable, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
                
    @Query("""
    SELECT new com.Makerspace.MakerspaceInventoryApp.Transaction.TransactionDTO(
        t,
        inv.domain, i.item_name, i.unit_of_measurement
    )
    FROM Transaction t
    JOIN Inventory inv ON t.transaction_inventory_id = inv.SKU
    JOIN Item i ON i.sku = inv.SKU
    WHERE inv.domain = :domain
    AND (t.time_of_transaction BETWEEN :startDate AND :endDate)
    """)
    Page<TransactionDTO> findPaginatedTransactionsDomainDates(Pageable pageable, @Param("domain") String domain,  @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    //for deleting the transactions associated with an item
    //void deleteByTransactionInventoryId(@Param("transaction_inventory_id") Integer transactionInventoryId);

//     @Modifying
// @Query("DELETE FROM Transaction t WHERE t.inventory.id = :inventoryId")
// void deleteByInventoryId(@Param("inventoryId") Integer inventoryId);


}
