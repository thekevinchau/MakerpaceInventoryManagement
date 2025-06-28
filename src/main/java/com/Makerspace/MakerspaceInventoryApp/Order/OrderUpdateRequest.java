package com.Makerspace.MakerspaceInventoryApp.Order;

import lombok.Data;

import java.util.Date;

@Data
public class OrderUpdateRequest {
    private String statusUpdate;
    private Integer amountReceived;
}
