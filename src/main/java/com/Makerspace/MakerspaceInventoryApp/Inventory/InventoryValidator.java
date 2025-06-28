package com.Makerspace.MakerspaceInventoryApp.Inventory;


import org.springframework.stereotype.Component;

@Component
public class InventoryValidator {

    private void validateNumbers(int ohq, int mohq, int moq, double unit_cost){
        if (moq < 0) throw new BadRequestException("MOQ cannot be a negative value.");
        if (mohq < 0) throw new BadRequestException("MOHQ cannot be a negative value");
        if (ohq < 0) throw new BadRequestException("OHQ cannot be a negative value.");
        if (unit_cost < 0) throw new BadRequestException("Unit cost cannot be a negative value.");
        //if (Math.floor(unit_cost) == unit_cost) throw new BadRequestException(("Unit cost must be a decimal value!"));
    }

    private void validateEmptyStrings(String domain, String type, String brand){
        if (domain.isBlank()){
            throw new BadRequestException("The domain must not be empty!");
        }
        if (type.isBlank()){
            throw new BadRequestException("The type must not be empty!");
        }
        if (brand.isBlank()){
            throw new BadRequestException("The brand must not be empty!");
        }
    }

    //only need to validate domain and type to ensure database integrity. Brand doesn't require validation because brands may be of
    //various lengths.

    public void validateInventoryInput(Inventory inventory){
        validateNumbers(inventory.getOhq(), inventory.getMohq(), inventory.getMoq(), inventory.getUnit_cost());
        validateEmptyStrings(inventory.getDomain(), inventory.getType(), inventory.getBrand());
    }
}
