package com.Makerspace.MakerspaceInventoryApp.Item;

import com.Makerspace.MakerspaceInventoryApp.Inventory.Inventory;
import com.Makerspace.MakerspaceInventoryApp.Inventory.InventoryDTO;
import org.apache.poi.ss.usermodel.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class ExcelService {

    public List<ItemCreationDTO> parseExcel(MultipartFile file) throws Exception {
        List<ItemCreationDTO> items = new ArrayList<>();

        try (InputStream inputStream = file.getInputStream();
             Workbook workbook = WorkbookFactory.create(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Skip header row
            if (rows.hasNext()) rows.next();

            while (rows.hasNext()) {
                Row row = rows.next();

                //skip rows w/o an item_name
                Cell itemNameCell = row.getCell(0);
                if (itemNameCell == null || itemNameCell.getCellType() == CellType.BLANK || getCellValue(itemNameCell).trim().isEmpty()) {
                    continue; // skip this row
                }

                // Create and populate Item
                Item item = new Item();
                item.setItem_name(getCellValue(row.getCell(0)));
                item.setDescription(getCellValue(row.getCell(1)));
                item.setUnit_of_measurement(getCellValue(row.getCell(2)));

                // Create and populate InventoryDTO
                Inventory inventory= new Inventory();
                inventory.setPart_number(getCellValue(row.getCell(3)));
                inventory.setDomain(getCellValue(row.getCell(4)));
                inventory.setType(getCellValue(row.getCell(5)));
                inventory.setBrand(getCellValue(row.getCell(6)));
                inventory.setOhq(parseInteger(row.getCell(7)));
                inventory.setMohq(parseInteger(row.getCell(8)));
                inventory.setMoq(parseInteger(row.getCell(9)));
                inventory.setUnit_cost(parseDouble(row.getCell(10)));
                inventory.setLink(getCellValue(row.getCell(11)));

                // Wrap in ItemCreationDTO
                ItemCreationDTO creationDTO = new ItemCreationDTO();
                creationDTO.setItem(item);
                creationDTO.setInventory(inventory);
                items.add(creationDTO);
            }
        }

        return items;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return null;
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue();
            case NUMERIC -> {
                double numericValue = cell.getNumericCellValue();
                // If it's actually an integer (e.g., 4200.0), don't include decimal
                if (numericValue == Math.floor(numericValue)) {
                    yield String.valueOf((long) numericValue);
                } else {
                    yield String.valueOf(numericValue);
                }
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            default -> null;
        };
    }
    

    private Integer parseInteger(Cell cell) {
        try {
            if (cell == null || cell.getCellType() == CellType.BLANK) return 0;
            return (int) cell.getNumericCellValue();
        } catch (Exception e) {
            return null;
        }
    }

    private Double parseDouble(Cell cell) {
        try {
            if (cell == null || cell.getCellType() == CellType.BLANK) return 0.0;
            return cell.getNumericCellValue();
        } catch (Exception e) {
            return null;
        }
    }
}
