package com.Makerspace.MakerspaceInventoryApp.Order;

import org.apache.coyote.Response;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*") // Allow all origins for this controller

public class OrderController {

    private final GetOrderService GetOrderService;
    private final UpdateOrderService UpdateOrderService;
    private final CreateOrderService CreateOrderService;

    public OrderController(com.Makerspace.MakerspaceInventoryApp.Order.GetOrderService getOrderService, com.Makerspace.MakerspaceInventoryApp.Order.UpdateOrderService updateOrderService, com.Makerspace.MakerspaceInventoryApp.Order.CreateOrderService createOrderService) {
        GetOrderService = getOrderService;
        UpdateOrderService = updateOrderService;
        CreateOrderService = createOrderService;
    }
    @PostMapping()
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderPOSTRequest request){
        return CreateOrderService.createOrder(request);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Integer id){
        return GetOrderService.GetOrderById(id);
    }

    @GetMapping()
    public ResponseEntity<Page<OrderDTO>> getOrderByQueryParams(
            @RequestParam Map<String,String> filters,
            @RequestParam (value = "pageNum", defaultValue = "0", required = true) int pageNum,
            @RequestParam (value="pageSize", defaultValue = "10", required = true) int pageSize)
    {
        return GetOrderService.GetOrderByQueryParams(filters, pageNum, pageSize);
    }

     @PutMapping("/{id}")
     public ResponseEntity<OrderDTO> updateOrder(@PathVariable Integer id, @RequestBody OrderUpdateRequest request){
        return UpdateOrderService.updateStatus(id, request);
     }

}
