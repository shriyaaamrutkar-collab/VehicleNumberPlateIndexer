import java.util.Scanner;

/**
 * DSA Micro-Project: Vehicle Number Plate Indexer
 * Unit 5: Hashing (Truncation Hash Function + Linear Probing)
 *
 * Simplification for this academic demo:
 * - Accepts Indian-style registration numbers such as MH02AB1234.
 * - The hash index is formed from the last two digits of the registration number.
 * - A 100-slot hash table is used, so the index is always between 0 and 99.
 */
public class VehicleNumberPlateIndexer {
    private static final int TABLE_SIZE = 100;
    private static final VehicleRecord[] table = new VehicleRecord[TABLE_SIZE];
    private static final Scanner scanner = new Scanner(System.in);

    private static class VehicleRecord {
        String plateNumber;
        String ownerName;

        VehicleRecord(String plateNumber, String ownerName) {
            this.plateNumber = plateNumber;
            this.ownerName = ownerName;
        }
    }

    // Normalize spaces/hyphens and use uppercase for consistent comparisons.
    private static String normalizePlate(String input) {
        return input.trim().replaceAll("[\\s-]", "").toUpperCase();
    }

    // Basic validation for common Indian registration-number formats.
    private static boolean isValidPlate(String plate) {
        return plate.matches("[A-Z]{2}[0-9]{1,2}[A-Z]{1,3}[0-9]{1,4}");
    }

    // Truncation hashing: select the final two digits as the table index.
    private static int hashFunction(String plate) {
        String lastTwoDigits = plate.substring(plate.length() - 2);
        return Integer.parseInt(lastTwoDigits); // Index range: 0 to 99
    }

    private static String readValidPlate() {
        while (true) {
            System.out.print("Enter vehicle number plate (example MH02AB1234): ");
            String plate = normalizePlate(scanner.nextLine());
            if (isValidPlate(plate)) {
                return plate;
            }
            System.out.println("Invalid format. Example: MH02AB1234. Try again.");
        }
    }

    private static void addVehicle() {
        String plate = readValidPlate();
        String owner;

        // Check duplicates before inserting.
        int startIndex = hashFunction(plate);
        int index = startIndex;
        for (int probes = 0; probes < TABLE_SIZE; probes++) {
            if (table[index] == null) {
                System.out.print("Enter owner's name: ");
                owner = scanner.nextLine().trim();
                if (owner.isEmpty()) {
                    owner = "Not provided";
                }
                table[index] = new VehicleRecord(plate, owner);
                System.out.println("Vehicle added successfully.");
                System.out.println("Hash index: " + startIndex + " | Stored at index: " + index);
                if (index != startIndex) {
                    System.out.println("A collision occurred; Linear Probing placed it at index " + index + ".");
                }
                return;
            }

            if (table[index].plateNumber.equals(plate)) {
                System.out.println("This vehicle number plate is already registered at index " + index + ".");
                return;
            }
            index = (index + 1) % TABLE_SIZE;
        }
        System.out.println("The hash table is full. No more vehicles can be added.");
    }

    private static void searchVehicle() {
        String plate = readValidPlate();
        int startIndex = hashFunction(plate);
        int index = startIndex;

        for (int probes = 0; probes < TABLE_SIZE; probes++) {
            if (table[index] == null) {
                System.out.println("Vehicle not found. Search stopped at empty index " + index + ".");
                return;
            }
            if (table[index].plateNumber.equals(plate)) {
                System.out.println("Vehicle found!");
                System.out.println("Number Plate : " + table[index].plateNumber);
                System.out.println("Owner Name   : " + table[index].ownerName);
                System.out.println("Hash Index   : " + startIndex);
                System.out.println("Stored Index : " + index);
                return;
            }
            index = (index + 1) % TABLE_SIZE;
        }
        System.out.println("Vehicle not found after checking the table.");
    }

    private static void displayTable() {
        System.out.println("\n----- VEHICLE HASH TABLE (occupied slots) -----");
        boolean foundAny = false;
        for (int i = 0; i < TABLE_SIZE; i++) {
            if (table[i] != null) {
                foundAny = true;
                System.out.printf("Index %02d | Plate: %-12s | Owner: %s%n",
                        i, table[i].plateNumber, table[i].ownerName);
            }
        }
        if (!foundAny) {
            System.out.println("The hash table is empty.");
        }
        System.out.println("------------------------------------------------\n");
    }

    private static int readMenuChoice() {
        while (true) {
            System.out.print("Choose an option (1-4): ");
            String input = scanner.nextLine().trim();
            try {
                int choice = Integer.parseInt(input);
                if (choice >= 1 && choice <= 4) {
                    return choice;
                }
            } catch (NumberFormatException ignored) {
                // Ask again below.
            }
            System.out.println("Please enter a number from 1 to 4.");
        }
    }

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("     VEHICLE NUMBER PLATE INDEXER");
        System.out.println(" Truncation Hashing + Linear Probing");
        System.out.println("========================================");

        while (true) {
            System.out.println("\n1. Add vehicle");
            System.out.println("2. Search vehicle by number plate");
            System.out.println("3. Display hash table");
            System.out.println("4. Exit");

            int choice = readMenuChoice();
            switch (choice) {
                case 1:
                    addVehicle();
                    break;
                case 2:
                    searchVehicle();
                    break;
                case 3:
                    displayTable();
                    break;
                case 4:
                    System.out.println("Thank you for using Vehicle Number Plate Indexer!");
                    scanner.close();
                    return;
                default:
                    // The menu input validator prevents this case.
                    System.out.println("Invalid choice.");
            }
        }
    }
}
