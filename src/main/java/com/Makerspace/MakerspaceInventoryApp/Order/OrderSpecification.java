package com.Makerspace.MakerspaceInventoryApp.Order;

import org.springframework.data.jpa.domain.Specification;

import java.sql.Timestamp;

public class OrderSpecification {

    public static Specification<Order> hasDomainName(String domain){
        return (root, query, criteriaBuilder) ->
            criteriaBuilder.equal(root.get("item").get("inventory").get("domain"), domain);
    }

    public static Specification<Order> hasTypeName(String type){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("item").get("inventory").get("type"), type)
                );
    }

    public static Specification<Order> hasBrandName(String brand){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("item").get("inventory").get("brand"), brand)
                );
    }

    public static Specification<Order> hasTimeRange(Timestamp startTime, Timestamp endTime){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.between(root.get("orderDate"), startTime, endTime)
                );
    }




}
