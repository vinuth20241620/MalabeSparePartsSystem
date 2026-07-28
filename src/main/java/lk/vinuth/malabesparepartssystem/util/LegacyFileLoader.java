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

/**
 * LegacyFileLoader
 *
 * This utility class reads the old text files
 * supplied with the coursework.
 *
 * It converts each line into Java objects so
 * the rest of the program can use them.
 *
 * This class only loads data.
 * It does not display anything to the user.
 */
public class LegacyFileLoader {

    /**
     * Reads every line from the inventory file.
     *
     * @param fileName inventory text file
     * @return list of SparePart objects
     */
    public List<SparePart> loadInventory(String fileName) {

        // Create an empty list to store every spare part.
        List<SparePart> spareParts = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(fileName))) {
            // Read the file one line at a time.
            String line;

            while ((line = reader.readLine()) != null) {

                // Remove spaces from the beginning and end.
                line = line.trim();

                // Ignore empty lines.
                if (line.isEmpty()) {
                    continue;
                }

                /*
                 * Convert the current text line into a SparePart object.
                 * The parsing work will be handled by a separate method
                 * that we will add in the next sections.
                 */
                SparePart sparePart = parseInventoryLine(line);

                // Add only successfully converted records.
                if (sparePart != null) {
                    spareParts.add(sparePart);
                }
            }

        } catch (IOException exception) {

            /*
             * Display a simple message if the file cannot be opened
             * or read. The application will continue running.
             */
            System.out.println(
                    "Could not read inventory file: "
                            + exception.getMessage()
            );
        }

        // Return every valid spare part loaded from the file.
        return spareParts;
    }
    /**
     * Converts one inventory text line into a SparePart object.
     *
     * The legacy file uses several separators, so this method
     * first changes them into one standard separator.
     *
     * @param line one line from inventory_legacy.txt
     * @return a SparePart object, or null if the line is invalid
     */
    private SparePart parseInventoryLine(String line) {

        try {
            /*
             * Replace pipes and semicolons with commas.
             *
             * We do not replace every colon because a colon
             * could appear inside other text in future files.
             */
            /*
             * Protect dates such as "Oct 15, 2023".
             *
             * The comma inside the date must not be treated as a
             * separator between inventory fields.
             *
             * Example:
             * "Oct 15, 2023" becomes "Oct 15 2023".
             */
            String cleanedLine = line.replaceAll(
                    "([A-Za-z]{3})\\s+(\\d{1,2}),\\s*(\\d{4})",
                    "$1 $2 $3"
            );

            /*
             * Convert all supplied field separators into commas.
             */
            cleanedLine = cleanedLine
                    .replace('|', ',')
                    .replace(';', ',')
                    .replace(':', ',');

            /*
             * Split the line into a maximum of eight sections.
             *
             * The -1 keeps empty fields, such as a missing brand
             * or missing image filename.
             */
            String[] fields = cleanedLine.split(",", -1);
            /*
             * A date such as "Oct 15, 2023" contains a comma.
             * That produces nine fields instead of eight.
             * Join the two date pieces back together.
             */

            // A valid inventory record must contain at least seven fields.
            if (fields.length < 7) {
                System.out.println("Skipped invalid inventory line: " + line);
                return null;
            }

            // Remove unnecessary spaces from each field.
            for (int index = 0; index < fields.length; index++) {
                fields[index] = fields[index].trim();
            }

            // Read the part code.
            String partCode = fields[0];

            // Read the part name.
            String partName = fields[1];

            // Read the brand. It may be empty.
            String brand = fields[2];

            /*
             * Remove currency text and symbols from the price.
             *
             * Examples:
             * "Rs. 4500.00" becomes "4500.00"
             * "Rs850" becomes "850"
             */
            /*
             * Remove the Rs or Rs. currency prefix first.
             *
             * This prevents the full stop in "Rs." from being
             * mistaken for the decimal point of the price.
             */
            String cleanedPrice = fields[3]
                    .replaceAll("(?i)Rs\\.?", "")
                    .replaceAll("[^0-9.]", "");

            double price = Double.parseDouble(cleanedPrice);

            // Convert the quantity text into an integer.
            int quantity = Integer.parseInt(fields[4]);

            // Standardise the category text.
            String category = standardiseCategory(fields[5]);

            // Convert the date text into a LocalDate object.
            LocalDate dateAdded = parseDate(fields[6]);

            /*
             * The image filename is optional.
             * Use an empty string if the field is missing.
             */
            String imageFileName = "";

            if (fields.length >= 8) {
                imageFileName = fields[7];
            }

            // Create and return the completed SparePart object.
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

            // This happens when the price or quantity is not a valid number.
            System.out.println(
                    "Skipped inventory line with invalid number: " + line
            );

            return null;

        } catch (DateTimeParseException exception) {

            // This happens when the date format is not recognised.
            System.out.println(
                    "Skipped inventory line with invalid date: " + line
            );

            return null;
        }
    }
    /**
     * Converts category text into one consistent format.
     *
     * Examples:
     * "ENGINE" becomes "Engine"
     * "electrical" becomes "Electrical"
     *
     * @param category original category text
     * @return cleaned category text
     */
    private String standardiseCategory(String category) {

        // Remove unnecessary spaces.
        String cleanedCategory = category.trim();

        // Return an empty string if the category is missing.
        if (cleanedCategory.isEmpty()) {
            return "";
        }

        // Convert everything to lowercase first.
        cleanedCategory = cleanedCategory.toLowerCase();

        // Capitalise only the first letter.
        return cleanedCategory.substring(0, 1).toUpperCase()
                + cleanedCategory.substring(1);
    }

    /**
     * Converts several possible date formats into LocalDate.
     *
     * @param dateText original date text
     * @return converted LocalDate value
     */
    private LocalDate parseDate(String dateText) {

        // Remove unnecessary spaces.
        String cleanedDate = dateText.trim();

        // List of supported date formats from the legacy file.
        DateTimeFormatter[] formats = {

                // Numeric year-month-day format, for example 2023-10-12.
                DateTimeFormatter.ofPattern("uuuu-MM-dd"),

                // Day/month/year format, for example 12/05/2023.
                DateTimeFormatter.ofPattern("dd/MM/uuuu"),

                // Day-month-year format, for example 01-02-2024.
                DateTimeFormatter.ofPattern("dd-MM-uuuu"),

                /*
                 * English month-name format with a comma,
                 * for example Oct 15, 2023.
                 */
                new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("MMM d uuuu")
                        .toFormatter(Locale.ENGLISH),

                /*
                 * English month-name format with hyphens,
                 * for example 15-Aug-2023.
                 */
                new DateTimeFormatterBuilder()
                        .parseCaseInsensitive()
                        .appendPattern("dd-MMM-uuuu")
                        .toFormatter(Locale.ENGLISH),

                // Alternative year/month/day format.
                DateTimeFormatter.ofPattern("uuuu/MM/dd")
        };

        // Try each format until one works.
        for (DateTimeFormatter format : formats) {
            try {
                return LocalDate.parse(cleanedDate, format);
            } catch (DateTimeParseException ignored) {
                // Try the next format.
            }
        }

        // No supported date format matched.
        throw new DateTimeParseException(
                "Unsupported date format",
                cleanedDate,
                0
        );
    }
    /**
     * Reads every line from the dealer file.
     *
     * @param fileName dealer text file
     * @return list of Dealer objects
     */
    public List<Dealer> loadDealers(String fileName) {

        // Create an empty list to store dealer objects.
        List<Dealer> dealers = new ArrayList<>();

        try (BufferedReader reader =
                     new BufferedReader(new FileReader(fileName))) {

            // Read the dealer file one line at a time.
            String line;

            while ((line = reader.readLine()) != null) {

                // Remove spaces from the beginning and end.
                line = line.trim();

                // Ignore blank lines.
                if (line.isEmpty()) {
                    continue;
                }

                // Convert the current line into a Dealer object.
                Dealer dealer = parseDealerLine(line);

                // Add only valid dealer records.
                if (dealer != null) {
                    dealers.add(dealer);
                }
            }

        } catch (IOException exception) {

            // Display a message if the dealer file cannot be read.
            System.out.println(
                    "Could not read dealer file: "
                            + exception.getMessage()
            );
        }

        // Return every valid dealer loaded from the file.
        return dealers;
    }
    /**
     * Converts one line from dealers_legacy.txt
     * into a Dealer object.
     *
     * The supplied dealer file contains four fields:
     * dealer ID, dealer name, phone number and location.
     *
     * @param line one line from the dealer legacy file
     * @return a Dealer object, or null if the line is invalid
     */
    private Dealer parseDealerLine(String line) {

        try {
            /*
             * Convert the different separators used in the
             * legacy file into commas.
             */
            String cleanedLine = line
                    .replace('|', ',')
                    .replace(';', ',');

            /*
             * Split the line while keeping empty values.
             *
             * Keeping empty values is important because some
             * dealers do not have a phone number.
             */
            String[] fields = cleanedLine.split(",", -1);

            /*
             * Every supplied dealer record should contain:
             *
             * 0 = dealer ID
             * 1 = dealer name
             * 2 = phone number
             * 3 = location
             */
            if (fields.length < 4) {
                System.out.println(
                        "Skipped invalid dealer line: " + line
                );

                return null;
            }

            // Remove unnecessary spaces from every field.
            for (int index = 0; index < fields.length; index++) {
                fields[index] = fields[index].trim();
            }

            String dealerId = fields[0];
            String dealerName = fields[1];
            String phoneNumber = fields[2];
            String location = fields[3];

            /*
             * The supplied file does not contain an email field.
             * Therefore, an empty email is stored.
             */
            String email = "";

            /*
             * The current Dealer model calls this field address.
             * We store the supplied dealer location in that field.
             */
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