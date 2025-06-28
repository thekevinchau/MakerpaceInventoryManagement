package com.Makerspace.MakerspaceInventoryApp.Inventory;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {
    List<Inventory> findByDomain(String domain);
    Optional<Inventory> findBySKU(Integer sku);
    @Query("SELECT i.SKU FROM Inventory i WHERE domain LIKE :domain ORDER BY i.SKU DESC LIMIT 1")
    int findLastSKUByDomain(@Param("domain") String domain);

    //for deleting an item
    //void deleteBySKU(Integer sku);
    @Modifying
    @Query("DELETE FROM Inventory i WHERE i.item.sku = :sku")
    void deleteByItemSku(@Param("sku") Integer sku);

}
