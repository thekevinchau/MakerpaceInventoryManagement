package com.Makerspace.MakerspaceInventoryApp.Order;

import lombok.Data;

@Data
public class OrderPOSTRequest {
    private Integer sku;
    private Integer quantity;
}
