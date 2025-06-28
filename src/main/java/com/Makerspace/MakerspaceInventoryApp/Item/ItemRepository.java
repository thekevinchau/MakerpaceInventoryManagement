package com.Makerspace.MakerspaceInventoryApp.Item;

//import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties.Pageable;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param; 

import java.util.List;

import org.springframework.data.domain.Page;

import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Integer>, JpaSpecificationExecutor<Item> {
   Page<Item> findAll(Pageable pageable);

   //filter to find all items where ohq < mohq
   @Query("SELECT i FROM Item i JOIN i.inventory inv WHERE inv.ohq < inv.mohq")
   List<Item> findLowStockItems();

   @Query("SELECT i FROM Item i JOIN i.inventory inv WHERE inv.ohq < inv.mohq")
   Page<Item> findLowStockItemsPaginated(Pageable pageable);
   Optional<Item> findBySku(Integer sku);

   // Custom query to search by searchTerm (for All items per page)
   @Query("SELECT i FROM Item i " +
      "JOIN i.inventory inventory " +
      "WHERE LOWER(i.item_name) LIKE LOWER(:searchTerm) " +
      "OR LOWER(i.description) LIKE LOWER(:searchTerm) " +
      "OR CAST(i.sku AS string) LIKE :searchTerm " +                   // Convert numerical field to string for partial matching
      "OR LOWER(inventory.part_number) LIKE LOWER(:searchTerm) " +
      "OR LOWER(inventory.type) LIKE LOWER(:searchTerm) " +
      "OR LOWER(inventory.brand) LIKE LOWER(:searchTerm) " +
      "OR CAST(inventory.ohq AS string) LIKE :searchTerm " +           // Convert numerical field to string
      "OR CAST(inventory.mohq AS string) LIKE :searchTerm " +
      "OR CAST(inventory.moq AS string) LIKE :searchTerm " +
      "OR CAST(inventory.unit_cost AS string) LIKE :searchTerm " +     // Convert numerical field to string
      "OR LOWER(inventory.link) LIKE LOWER(:searchTerm) ")
   List<Item> findBySearchTerm(@Param("searchTerm") String searchTerm);
   
   // Normal Search term + Pagination
   @Query("SELECT i FROM Item i JOIN i.inventory inventory " +
       "WHERE LOWER(i.item_name) LIKE LOWER(:searchTerm) " +
       "OR LOWER(i.description) LIKE LOWER(:searchTerm) " +
       "OR CAST(i.sku AS string) LIKE :searchTerm " +
       "OR LOWER(inventory.part_number) LIKE LOWER(:searchTerm) " +
       "OR LOWER(inventory.type) LIKE LOWER(:searchTerm) " +
       "OR LOWER(inventory.brand) LIKE LOWER(:searchTerm) " +
       "OR CAST(inventory.ohq AS string) LIKE :searchTerm " +
       "OR CAST(inventory.mohq AS string) LIKE :searchTerm " +
       "OR CAST(inventory.moq AS string) LIKE :searchTerm " +
       "OR CAST(inventory.unit_cost AS string) LIKE :searchTerm " +
       "OR LOWER(inventory.link) LIKE LOWER(:searchTerm)")
Page<Item> findBySearchTerm(@Param("searchTerm") String searchTerm, Pageable pageable);


   // Find by domain only
   @Query("SELECT i FROM Item i LEFT JOIN i.inventory inventory WHERE inventory.domain = :domain")
   Page<Item> findByDomain(@Param("domain") String domain, Pageable pageable);

   // Find by domain and search term
   @Query("SELECT i FROM Item i " +
         "LEFT JOIN i.inventory inventory " +
         "WHERE inventory.domain = :domain " +
         "AND (LOWER(i.item_name) LIKE LOWER(:searchTerm) " +
         "OR LOWER(i.description) LIKE LOWER(:searchTerm) " +
         "OR CAST(i.sku AS string) LIKE :searchTerm " +
         "OR LOWER(inventory.part_number) LIKE LOWER(:searchTerm) " +
         "OR LOWER(inventory.type) LIKE LOWER(:searchTerm) " +
         "OR LOWER(inventory.brand) LIKE LOWER(:searchTerm) " +
         "OR CAST(inventory.ohq AS string) LIKE :searchTerm " +
         "OR CAST(inventory.mohq AS string) LIKE :searchTerm " +
         "OR CAST(inventory.moq AS string) LIKE :searchTerm " +
         "OR CAST(inventory.unit_cost AS string) LIKE :searchTerm " +
         "OR LOWER(inventory.link) LIKE LOWER(:searchTerm))")
   Page<Item> findBySearchTermAndDomain(@Param("searchTerm") String searchTerm, @Param("domain") String domain, Pageable pageable);

   
   // void deleteBySku(Integer SKU);

   //for sorting
   @Query("SELECT i FROM Item i JOIN i.inventory inv")
   Page<Item> findAllWithInventory(Pageable pageable);

   // @Query("SELECT i FROM Item i LEFT JOIN FETCH i.inventory")
   // Page<Item> findAllWithInventory(Pageable pageable);



}
