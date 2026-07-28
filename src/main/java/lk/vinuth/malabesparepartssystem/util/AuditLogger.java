package lk.vinuth.malabesparepartssystem.util;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLogger {

    private static final String LOG_FILE = "audit_log.txt";

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void logAction(
            String action,
            String itemCode,
            Integer quantity
    ) {

        String timestamp =
                LocalDateTime.now().format(DATE_FORMAT);

        String logEntry =
                timestamp
                        + " | Action: "
                        + action
                        + " | Item Code: "
                        + itemCode;

        if (quantity != null) {
            logEntry += " | Quantity: " + quantity;
        }

        try (BufferedWriter writer =
                     new BufferedWriter(
                             new FileWriter(LOG_FILE, true)
                     )) {

            writer.write(logEntry);
            writer.newLine();

        } catch (IOException exception) {
            System.out.println(
                    "Could not write to audit log: "
                            + exception.getMessage()
            );
        }
    }
}