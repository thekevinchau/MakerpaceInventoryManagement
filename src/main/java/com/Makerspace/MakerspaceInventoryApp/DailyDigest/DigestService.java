package com.Makerspace.MakerspaceInventoryApp.DailyDigest;

import com.Makerspace.MakerspaceInventoryApp.Item.Item;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.*;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DigestService {

    // This cron runs every minute (for testing)
    @Scheduled(cron = "0 * * * * *")
    // This cron runs every day at 8:00 AM UTC (adjust if needed)
    //@Scheduled(cron = "0 0 8 * * *")
    public void sendDailyDigest() {
        // Fetch low stock items and assign it to lowStockItems
        List<Item>lowStockItems = null;

        String subject = "📦 MakerSpace Inventory Daily Digest";
        String body = buildEmailBody(lowStockItems);

        sendEmail(subject, body);
    }

    private String buildEmailBody(List<Item> items) {
        StringBuilder sb = new StringBuilder("Here is your daily inventory update:\n\n");
        for (Item item : items) {
            // - name (sku): # in stock
            sb.append("- ")
              .append(item.getItem_name())
              .append(" (SKU: ")
              .append(item.getSku())
              .append("): ")
              .append(item.getInventory())
              .append(" in stock\n");
        }
        return sb.toString();
    }

    private void sendEmail(String subject, String body) {
        try {
            AmazonSimpleEmailService client = AmazonSimpleEmailServiceClientBuilder.standard()
                .withRegion(Regions.US_EAST_1)
                .build();

            SendEmailRequest request = new SendEmailRequest()
                .withDestination(new Destination().withToAddresses("zwitte@udel.edu"))
                .withMessage(new Message()
                    .withBody(new Body()
                        .withText(new Content().withCharset("UTF-8").withData(body)))
                    .withSubject(new Content().withCharset("UTF-8").withData(subject)))
                .withSource("noreply@makerspaceinventory.com");

            client.sendEmail(request);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
