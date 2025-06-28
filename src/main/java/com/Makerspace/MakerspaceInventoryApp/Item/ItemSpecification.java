package com.Makerspace.MakerspaceInventoryApp.Item;

import org.springframework.data.jpa.domain.Specification;

public class ItemSpecification {

    //Domain filter
    public static Specification<Item> hasDomainName(String domain){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("inventory").get("domain"), domain));
    }

    //Type filter
    public static Specification<Item> hasTypeName(String type){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("inventory").get("type"), type));
    }

    //Brand filter
    public static Specification<Item> hasBrandName(String brand){
        return ((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(
                        root.get("inventory").get("brand"), brand));
    }



}
