package ui;

import data.DealershipFileManager;
import model.Dealership;
import model.Vehicle;

import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class UserInterface {
    private static final Scanner inputScanner = new Scanner(System.in);
    private Dealership dealership;


    public UserInterface() {

    }

    public void display() {
        boolean isRunning = true;
        init();

        do {
            printDisplayScreen();
            int choice = promptForMenuChoice();

            switch (choice) {
                case 1:
                    processGetByPriceRequest();
                    break;
                case 2:
                    processGetByMakeModelRequest();
                    break;
                case 3:
                    processGetByYearRequest();
                    break;
                case 4:
                    processGetByColorRequest();
                    break;
                case 5:
                    processGetByMileageRequest();
                    break;
                case 6:
                    processGetByVehicleTypeRequest();
                    break;
                case 7:
                    processGetAllVehiclesRequest();
                    break;
                case 8:
                    processAddVehicleRequest();
                    break;
                case 9:
                    processRemoveVehicleRequest();
                    break;
                case 0: {
                    System.out.println("GoodBye!");
                    isRunning = false;
                    break;
                }
                default:
                    System.out.println("Invalid option. Please choose: 1, 2, 3, 4, 5, 6, 7, 8, 9, 0");
            }
        } while (isRunning);
    }

    private int promptForMenuChoice() {
        return promptForInt("Enter your choice: ", 0, 9);
    }

    private int promptForInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = inputScanner.nextLine();
            try {
                return Integer.parseInt(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private int promptForInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = inputScanner.nextLine();
            try {
                int value = Integer.parseInt(input.trim());
                if (value >= min && value <= max) {
                    return value;
                }
                System.out.println("Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    private BigDecimal promptForBigDecimal(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = inputScanner.nextLine();
            try {
                return new BigDecimal(input.trim());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid decimal number (e.g., 15000.00).");
            }
        }
    }

    public void processGetByPriceRequest() {
        System.out.println("===== Search By Price =====");
        BigDecimal min = promptForBigDecimal("Enter minimum price: ");
        BigDecimal max = promptForBigDecimal("Enter maximum price: ");

        List<Vehicle> vehicles = dealership.getVehiclesByPrice(min, max);
        printDealershipTable(vehicles);
        pressEnterToContinue();
    }

    public void processGetByMakeModelRequest() {
        System.out.println("===== Search By Make / Model =====");
        System.out.print("Enter make: ");
        String make = inputScanner.nextLine();

        System.out.print("Enter model: ");
        String model = inputScanner.nextLine();

        List<Vehicle> vehicles = dealership.getVehiclesByMakeModel(make, model);
        printDealershipTable(vehicles);
        pressEnterToContinue();
    }

    public void processGetByYearRequest() {
        System.out.println("===== Search By Year =====");
        int min = promptForInt("Enter minimum year: ");
        int max = promptForInt("Enter maximum year: ");

        List<Vehicle> vehicles = dealership.getVehiclesByYear(min, max);
        printDealershipTable(vehicles);
        pressEnterToContinue();
    }

    public void processGetByColorRequest() {
        System.out.println("===== Search By Color =====");
        System.out.print("Enter color: ");
        String color = inputScanner.nextLine();

        List<Vehicle> vehicles = dealership.getVehiclesByColor(color);
        printDealershipTable(vehicles);
        pressEnterToContinue();
    }

    public void processGetByMileageRequest() {
        System.out.println("===== Search By Mileage =====");
        int min = promptForInt("Enter minimum mileage: ");
        int max = promptForInt("Enter maximum mileage: ");

        List<Vehicle> vehicles = dealership.getVehiclesByMileage(min, max);
        printDealershipTable(vehicles);
        pressEnterToContinue();
    }

    public void processGetByVehicleTypeRequest() {
        System.out.println("===== Search By Vehicle Type =====");
        System.out.print("Enter vehicle type (e.g., SUV, Truck, Sedan): ");
        String type = inputScanner.nextLine();

        List<Vehicle> vehicles = dealership.getVehiclesByType(type);
        printDealershipTable(vehicles);
        pressEnterToContinue();
    }

    public void processGetAllVehiclesRequest() {
        System.out.println("===== All Vehicles =====");
        printDealershipTable(dealership.getAllVehicles());
        pressEnterToContinue();

    }

    public void processAddVehicleRequest() {
        System.out.println("===== Add A Vehicle =====");
        int vin = promptForInt("Enter VIN: ");
        int year = promptForInt("Enter Year: ");
        int odometer = promptForInt("Enter Odometer: ");

        System.out.print("Enter Make: ");
        String make = inputScanner.nextLine();

        System.out.print("Enter Model: ");
        String model = inputScanner.nextLine();

        System.out.print("Enter Color: ");
        String color = inputScanner.nextLine();

        System.out.print("Enter Type: ");
        String type = inputScanner.nextLine();

        BigDecimal price = promptForBigDecimal("Enter Price: ");

        Vehicle newVehicle = new Vehicle(vin, year, odometer, make, model, color, type, price);
        dealership.addVehicle(newVehicle);

        DealershipFileManager fileManager = new DealershipFileManager();
        fileManager.saveDealership(dealership);

        System.out.println("Vehicle added and saved successfully!");
        pressEnterToContinue();
    }

    public void processRemoveVehicleRequest() {
        System.out.println("===== Remove A Vehicle =====");
        int vinToRemove = promptForInt("Enter the VIN of the vehicle to remove: ");

        Vehicle vehicleToRemove = null;
        for (Vehicle vehicle : dealership.getAllVehicles()) {
            if (vehicle.getVin() == vinToRemove) {
                vehicleToRemove = vehicle;
                break;
            }
        }

        if (vehicleToRemove != null) {
            dealership.removeVehicle(vehicleToRemove);

            DealershipFileManager fileManager = new DealershipFileManager();
            fileManager.saveDealership(dealership);

            System.out.println("Vehicle with VIN " + vinToRemove + " has been removed and saved.");
        } else {
            System.out.println("No vehicle found with VIN " + vinToRemove);
        }

        pressEnterToContinue();
    }


    public static void printDisplayScreen() {
        System.out.println("===== Welcome =====");
        System.out.println("\t1 - Find vehicles by price");
        System.out.println("\t2 - Find vehicles by make / model");
        System.out.println("\t3 - Find vehicles by year");
        System.out.println("\t4 - Find vehicles by color");
        System.out.println("\t5 - Find vehicles by mileage range");
        System.out.println("\t6 - Find vehicles by type");
        System.out.println("\t7 - View all vehicles");
        System.out.println("\t8 - Add vehicles");
        System.out.println("\t9 - Remove vehicles");
        System.out.println("\t0 - Exit");
    }

    private void init() {
        DealershipFileManager fileManager = new DealershipFileManager();

        try {
            this.dealership = fileManager.getDealership();

            System.out.println("Dealership loaded successfully: " + dealership.getName());
        } catch (FileNotFoundException e) {
            System.out.println("Could not find the inventory file.");
        }
    }

    public static void printDealershipTable(List<Vehicle> vehicleList) {
        if (vehicleList.isEmpty()) {
            System.out.println("No vehicles found.");
            return;
        }

        System.out.printf("%-19s | %-6s | %-8s | %-10s | %-10s | %-10s | %-10s | %-12s%n", "vin", "year", "odometer", "make", "model", "color", "type", "price");
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------");

        for (Vehicle vehicle : vehicleList) {
            System.out.printf("%-19s | %-6s | %-8s | %-10s | %-10s | %-10s | %-10s | %-11.2f%n",
                    vehicle.getVin(),
                    vehicle.getYear(),
                    vehicle.getOdometer(),
                    vehicle.getMake(),
                    vehicle.getModel(),
                    vehicle.getColor(),
                    vehicle.getType(),
                    vehicle.getPrice().doubleValue());
        }
    }

    public static void pressEnterToContinue() {
        System.out.println();
        System.out.println("Press enter to go back to the menu...");
        inputScanner.nextLine();
    }
}
