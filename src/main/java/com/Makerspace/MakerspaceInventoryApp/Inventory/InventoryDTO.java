package com.Makerspace.MakerspaceInventoryApp.Inventory;

import lombok.Data;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Data
public class InventoryDTO {
    private String part_number;
    private String domain;
    private String type;
    private String brand;
    private Integer ohq;
    private Integer mohq;
    private Integer moq;
    private Double unit_cost;
    private String link;
    private LocalDateTime last_update;

    public InventoryDTO(Inventory inventory) {
        this.part_number = inventory.getPart_number();
        this.domain = inventory.getDomain();
        this.type = inventory.getType();
        this.brand = inventory.getBrand();
        this.ohq = inventory.getOhq();
        this.mohq = inventory.getMohq();
        this.moq = inventory.getMoq();
        this.unit_cost = inventory.getUnit_cost();
        this.link = inventory.getLink();
        this.last_update = inventory.getLast_update();
    }

}
