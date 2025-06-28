package com.Makerspace.MakerspaceInventoryApp.Order;

import com.Makerspace.MakerspaceInventoryApp.Exceptions.GlobalBadRequestException;
import com.Makerspace.MakerspaceInventoryApp.Exceptions.GlobalNotFoundException;
import com.Makerspace.MakerspaceInventoryApp.Inventory.Inventory;
import com.Makerspace.MakerspaceInventoryApp.Inventory.InventoryRepository;
import com.Makerspace.MakerspaceInventoryApp.Item.ItemDTO;
import com.Makerspace.MakerspaceInventoryApp.Item.ItemNotFoundException;
import com.Makerspace.MakerspaceInventoryApp.Item.ItemRepository;
import com.Makerspace.MakerspaceInventoryApp.Transaction.TransactionRepository;
import com.Makerspace.MakerspaceInventoryApp.Transaction.Transaction;
import com.Makerspace.MakerspaceInventoryApp.Transaction.TransactionRepository; 

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UpdateOrderService {

    private final OrderRepository OrderRepository;
    private final InventoryRepository InventoryRepository;
    private final TransactionRepository TransactionRepository;

    private final List<String> statusList = new ArrayList<>(){{
        add("Ordered");
        add("Received");
        add("Cancelled");
    }};

    public UpdateOrderService(com.Makerspace.MakerspaceInventoryApp.Order.OrderRepository orderRepository, com.Makerspace.MakerspaceInventoryApp.Inventory.InventoryRepository inventoryRepository, TransactionRepository transactionRepository) {
        OrderRepository = orderRepository;
        InventoryRepository = inventoryRepository;
        TransactionRepository = transactionRepository;
    }


    //Abstract into a function -------->
    //List all items that are currently low stock
    //If ordered, set last ordered to current date.
    //If received, change received_date to the time it was received and .
    //If cancelled, create a new Order entry and replicate the data and remove the old one.

    private void handleReceivedOrder (Integer quantity, Order order, Inventory inventory){
        
        order.setStatus("Received");
        order.setReceivedDate(LocalDateTime.now());
        OrderRepository.save(order);
        Integer currentOHQ = inventory.getOhq();
        inventory.setOhq(currentOHQ + quantity);

        //Create Transaction on Items Recieved
        Transaction transaction = new Transaction();
        transaction.setTransaction_inventory_id(inventory.getSKU());
        transaction.setQuantity(quantity - currentOHQ);
        transaction.setTime_of_transaction(LocalDateTime.now());
        TransactionRepository.save(transaction);

        InventoryRepository.save(inventory);
    }

    private OrderDTO updateStatus(String status, Order order, Inventory inventory){
        switch(status){
            case "Received" -> handleReceivedOrder(order.getQuantity(), order, inventory);
        }
        return new OrderDTO(order);
    }

    /*
    RECEIVED ORDER:
        - Client sends request to update status of the order to "received"
        - Set the order's status to be received, change the received datetime, and then grab the order's inventory.
        - Grab the inventory's current OHQ and add the amount received to it.


     */
    public ResponseEntity<OrderDTO> updateStatus (Integer id, OrderUpdateRequest request) throws GlobalBadRequestException {
        //Check to see if the order status submitted is one of the three possible ones.
        if (!statusList.contains(request.getStatusUpdate())){
            throw new GlobalBadRequestException("Invalid order status!");
        }
        //Check if the Order exists
        Optional<Order> orderOptional = OrderRepository.findById(id);
        if (orderOptional.isEmpty()){
            throw new GlobalNotFoundException("Order with ID " + id + " was not found.");
        }
        //Check if the inventory exists
        Optional<Inventory> inventoryOptional = InventoryRepository.findBySKU(orderOptional.get().getItem().getSku());
        if (inventoryOptional.isEmpty()){
            throw new GlobalNotFoundException("Inventory does not exist!");
        }
        Optional<Order> queriedOrder = OrderRepository.findById(id);
        Order savedOrder;
        if (queriedOrder.isPresent()){
            Order oldOrder = queriedOrder.get();
            oldOrder.setStatus(request.getStatusUpdate());
            oldOrder.setQuantity(request.getAmountReceived());
            return ResponseEntity.status(HttpStatus.OK).body(new OrderDTO(OrderRepository.save(oldOrder)));
        }
        throw new GlobalNotFoundException("Order to Update Not Found");    
    }


}
