package lk.vinuth.malabesparepartssystem.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import lk.vinuth.malabesparepartssystem.model.Dealer;
import lk.vinuth.malabesparepartssystem.model.SparePart;

public class LegacyFileLoader {

    public List<SparePart> loadInventory(String fileName) {

        List<SparePart> spareParts = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                SparePart sparePart = parseInventoryLine(line);

                if (sparePart != null) {
                    spareParts.add(sparePart);
                }
            }

        } catch (IOException exception) {

            System.out.println(
                    "Could not read inventory file: "
                            + exception.getMessage()
            );
        }

        return spareParts;
    }
    private SparePart parseInventoryLine(String line) {

        try {
            String cleanedLine = line.replaceAll(
                    "([A-Za-z]{3})\\s+(\\d{1,2}),\\s*(\\d{4})",
                    "$1 $2 $3"
            );

            cleanedLine = cleanedLine
                    .replace('|', ',')
                    .replace(';', ',')
                    .replace(':', ',');

            String[] fields = cleanedLine.split(",", -1);

            if (fields.length < 7) {
                System.out.println("Skipped invalid inventory line: " + line);
                return null;
            }

            for (int index = 0; index < fields.length; index++) {
                fields[index] = fields[index].trim();
            }

            String partCode = fields[0];

            String partName = fields[1];

            String brand = fields[2];

            String cleanedPrice = fields[3]
                    .replaceAll("(?i)Rs\\.?", "")
                    .replaceAll("[^0-9.]", "");

            double price = Double.parseDouble(cleanedPrice);

            int quantity = Integer.parseInt(fields[4]);

            String category = standardiseCategory(fields[5]);

            LocalDate dateAdded = parseDate(fields[6]);

            String imageFileName = "";

            if (fields.length >= 8) {
                imageFileName = fields[7];
            }

            return new SparePart(
                    partCode,
                    partName,
                    brand,
                    price,
                    quantity,
                    category,
                    dateAdded,
                    imageFileName
            );

        } catch (NumberFormatException exception) {

            System.out.println(
                    "Skipped inventory line with invalid number: " + line
            );

            return null;

        } catch (DateTimeParseException exception) {

            System.out.println(
                    "Skipped inventory line with invalid date: " + line
            );

            return null;
        }
    }
    private String standardiseCategory(String category) {

        String cleanedCategory = category.trim();


        if (cleanedCategory.isEmpty()) {
            return "";
        }

        cleanedCategory = cleanedCategory.toLowerCase();

        return cleanedCategory.substring(0, 1).toUpperCase()
                + cleanedCategory.substring(1);
    }

    private LocalDate parseDate(String dateText) {

        String cleanedDate = dateText.trim();

        DateTimeFormatter[] formats = {

                DateTimeFormatter.ofPattern("uuuu-MM-dd"),

                DateTimeFormatter.ofPattern("dd/MM/uuuu"),

                DateTimeFormatter.ofPattern("dd-MM-uuuu"),

                new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("MMM d uuuu")
                        .toFormatter(Locale.ENGLISH),

                new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("dd-MMM-uuuu")
                        .toFormatter(Locale.ENGLISH),

                DateTimeFormatter.ofPattern("uuuu/MM/dd")
        };

        for (DateTimeFormatter format : formats) {
            try {
                return LocalDate.parse(cleanedDate, format);
            } catch (DateTimeParseException ignored) {
            }
        }

        throw new DateTimeParseException(
                "Unsupported date format",
                cleanedDate,
                0
        );
    }
    public List<Dealer> loadDealers(String fileName) {

        List<Dealer> dealers = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(fileName))) {

            String line;

            while ((line = reader.readLine()) != null) {

                line = line.trim();

                if (line.isEmpty()) {
                    continue;
                }

                Dealer dealer = parseDealerLine(line);

                if (dealer != null) {
                    dealers.add(dealer);
                }
            }

        } catch (IOException exception) {

            System.out.println(
                    "Could not read dealer file: "
                            + exception.getMessage()
            );
        }

        return dealers;
    }
    private Dealer parseDealerLine(String line) {

        try {
            String cleanedLine = line
                    .replace('|', ',')
                    .replace(';', ',');

            String[] fields = cleanedLine.split(",", -1);

            if (fields.length < 4) {
                System.out.println(
                        "Skipped invalid dealer line: " + line
                );

                return null;
            }

            for (int index = 0; index < fields.length; index++) {
                fields[index] = fields[index].trim();
            }

            String dealerId = fields[0];
            String dealerName = fields[1];
            String phoneNumber = fields[2];
            String location = fields[3];

            String email = "";

            String address = location;

            return new Dealer(
                    dealerId,
                    dealerName,
                    phoneNumber,
                    email,
                    address
            );

        } catch (Exception exception) {

            System.out.println(
                    "Skipped dealer line: " + line
            );

            return null;
        }
    }

}