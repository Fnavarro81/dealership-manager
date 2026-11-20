package com.pluralsight.dealershipmanager;

import java.io.*;
import java.math.BigDecimal;
import java.util.regex.Pattern;

public class DealershipFileManager {
    public Dealership getDealership() throws FileNotFoundException {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader("inventory.csv"))) {
            boolean isHeader = true;
            String line;
            Dealership dealership = new Dealership();
            while ((line = bufferedReader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] splitValues = line.split(Pattern.quote("|"));

                if (isHeader) {
                    dealership.setName(splitValues[0]);
                    dealership.setAddress(splitValues[1]);
                    dealership.setPhone(splitValues[2]);
                    isHeader = false;
                } else {
                    int vin = Integer.parseInt(splitValues[0]);
                    int year = Integer.parseInt(splitValues[1]);
                    String make = splitValues[2];
                    String model = splitValues[3];
                    String type = splitValues[4];
                    String color = splitValues[5];
                    int odometer = Integer.parseInt(splitValues[6]);
                    BigDecimal price = BigDecimal.valueOf(Double.parseDouble(splitValues[7]));
                    dealership.addVehicle(new Vehicle(vin, year, odometer, make, model, color, type, price));
                }
            }

            return dealership;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void saveDealership(Dealership dealership) {
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("inventory.csv"))) {

            String header = dealership.getName() + "|" + dealership.getAddress() + "|" + dealership.getPhone();
            bufferedWriter.write(header);
            bufferedWriter.newLine();

            for (Vehicle vehicle : dealership.getAllVehicles()) {
                String vehicleLine = String.format("%d|%d|%s|%s|%s|%s|%d|%s",
                        vehicle.getVin(),
                        vehicle.getYear(),
                        vehicle.getMake(),
                        vehicle.getModel(),
                        vehicle.getType(),
                        vehicle.getColor(),
                        vehicle.getOdometer(),
                        vehicle.getPrice()
                );
                bufferedWriter.write(vehicleLine);
                bufferedWriter.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
