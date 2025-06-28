package com.Makerspace.MakerspaceInventoryApp.Order;

import com.Makerspace.MakerspaceInventoryApp.Exceptions.GlobalBadRequestException;
import com.Makerspace.MakerspaceInventoryApp.Item.ItemNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class GetOrderService {
    private final OrderRepository OrderRepository;

    public GetOrderService(com.Makerspace.MakerspaceInventoryApp.Order.OrderRepository orderRepository) {
        OrderRepository = orderRepository;
    }

    public ResponseEntity<OrderDTO> GetOrderById(Integer id){
        Optional<Order> order = OrderRepository.findByItem_Sku(id);
        if (order.isEmpty()){
            return ResponseEntity.ok(null);
        }

        return ResponseEntity.ok(new OrderDTO(order.get()));
    }

    public ResponseEntity<Page<OrderDTO>> GetOrderByQueryParams (Map<String, String> filters, int pageNumber, int pageSize){
        Specification<Order> spec = Specification.where(null);

        for (Map.Entry<String,String> entry: filters.entrySet()){
            if (entry.getKey().equals("domain")){
                spec = spec.and(OrderSpecification.hasDomainName(entry.getValue()));
            }
            if (entry.getKey().equals("brand")){
                spec = spec.and(OrderSpecification.hasBrandName(entry.getValue()));
            }
            if (entry.getKey().equals("type")){
                spec = spec.and(OrderSpecification.hasTypeName(entry.getValue()));
            }
            //if (entry.getKey().equals(""))
        }
        Page<Order> orders = OrderRepository.findAll(spec, PageRequest.of(pageNumber, pageSize));
        Page<OrderDTO> orderDTOs = orders.map(OrderDTO::new);
        return ResponseEntity.ok(orderDTOs);
    }

}
