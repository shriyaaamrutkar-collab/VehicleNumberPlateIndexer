import java.util.Scanner;

public class NumberPlate {

    private static final int TABLE_SIZE = 100;
    private static final String[] plates = new String[TABLE_SIZE];
    private static final String[] owners = new String[TABLE_SIZE];
    private static final Scanner scanner = new Scanner(System.in);

    private static String normalizePlate(String input) {
        return input.trim().replaceAll("[\\s-]", "").toUpperCase();
    }

    private static boolean isValidPlate(String plate) {
        return plate.matches("[A-Z]{2}[0-9]{1,2}[A-Z]{1,3}[0-9]{1,4}");
    }

    private static int hashFunction(String plate) {
        String lastTwo = plate.substring(plate.length() - 2);
        return Integer.parseInt(lastTwo);
    }

    private static String readValidPlate() {
        while (true) {
            System.out.print("Enter vehicle number plate (e.g. MH02AB1234): ");
            String plate = normalizePlate(scanner.nextLine());
            if (isValidPlate(plate)) {
                return plate;
            }
            System.out.println("Invalid format. Example: MH02AB1234. Try again.");
        }
    }

    private static void addVehicle() {
        String plate = readValidPlate();
        int startIndex = hashFunction(plate);
        int index = startIndex;

        for (int probes = 0; probes < TABLE_SIZE; probes++) {
            if (plates[index] == null) {
                System.out.print("Enter owner name: ");
                String owner = scanner.nextLine().trim();
                if (owner.isEmpty()) {
                    owner = "Not provided";
                }
                plates[index] = plate;
                owners[index] = owner;
                System.out.println("Vehicle added at index " + index + " (hash index: " + startIndex + ").");
                if (index != startIndex) {
                    System.out.println("Collision occurred. Linear probing placed it at index " + index + ".");
                }
                return;
            }
            if (plates[index].equals(plate)) {
                System.out.println("This plate is already registered at index " + index + ".");
                return;
            }
            index = (index + 1) % TABLE_SIZE;
        }
        System.out.println("Hash table is full.");
    }

    private static void searchVehicle() {
        String plate = readValidPlate();
        int startIndex = hashFunction(plate);
        int index = startIndex;

        for (int probes = 0; probes < TABLE_SIZE; probes++) {
            if (plates[index] == null) {
                System.out.println("Vehicle not found.");
                return;
            }
            if (plates[index].equals(plate)) {
                System.out.println("Number Plate : " + plates[index]);
                System.out.println("Owner Name   : " + owners[index]);
                System.out.println("Hash Index   : " + startIndex);
                System.out.println("Stored Index : " + index);
                return;
            }
            index = (index + 1) % TABLE_SIZE;
        }
        System.out.println("Vehicle not found.");
    }

    private static void displayTable() {
        boolean any = false;
        for (int i = 0; i < TABLE_SIZE; i++) {
            if (plates[i] != null) {
                any = true;
                System.out.printf("Index %02d  |  Plate: %-12s  |  Owner: %s%n", i, plates[i], owners[i]);
            }
        }
        if (!any) {
            System.out.println("Hash table is empty.");
        }
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
            }
            System.out.println("Enter a number from 1 to 4.");
        }
    }

    public static void main(String[] args) {
        System.out.println("Vehicle Number Plate Indexer");
        System.out.println("Truncation Hashing with Linear Probing");

        while (true) {
            System.out.println();
            System.out.println("1. Add vehicle");
            System.out.println("2. Search vehicle");
            System.out.println("3. Display table");
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
                    scanner.close();
                    return;
            }
        }
    }
}
