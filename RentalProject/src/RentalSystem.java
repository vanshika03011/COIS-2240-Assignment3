import java.util.List;
import java.time.LocalDate;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RentalSystem {
   private static RentalSystem instance;  // Singleton instance for RentalSystem

   private List<Vehicle> vehicles = new ArrayList<>();  // List to store all vehicles
   private List<Customer> customers = new ArrayList<>();  // List to store all customers
   private RentalHistory rentalHistory = new RentalHistory();  // Rental history to track rentals and returns

   public RentalSystem() {
       loadData();  // Load existing data (customers, vehicles, rental records) from files
   }

   public static RentalSystem getInstance() {
       if (instance == null) {
           instance = new RentalSystem();  // Create new instance if none exists
       }
       return instance;
   }

   // Add a new vehicle to the system after checking for duplicates
   public void addVehicle(Vehicle vehicle) {
       for (Vehicle v : vehicles) {
           if (v.getLicensePlate().equalsIgnoreCase(vehicle.getLicensePlate())) {
               System.out.println("Duplicate license plate. Vehicle not added.");
               return;
           }
       }
       vehicles.add(vehicle);
       saveVehicle(vehicle);  // Save the newly added vehicle to a file
   }

   // Add a new customer to the system after checking for duplicates
   public void addCustomer(Customer customer) {
       for (Customer c : customers) {
           if (c.getCustomerId() == customer.getCustomerId()) {
               System.out.println("Duplicate customer ID. Customer not added.");
               return;
           }
       }
       customers.add(customer);
       saveCustomer(customer);  // Save the newly added customer to a file
   }

   // Rent a vehicle to a customer if it's available
   public void rentVehicle(Vehicle vehicle, Customer customer, LocalDate date, double amount) {
       if (vehicle.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
           vehicle.setStatus(Vehicle.VehicleStatus.RENTED);  // Update vehicle status to rented
           RentalRecord record = new RentalRecord(vehicle, customer, date, amount, "RENT");
           rentalHistory.addRecord(record);  // Add rental record to history
           saveRecord(record);  // Save rental record to a file
           System.out.println("Vehicle rented to " + customer.getCustomerName());
       } else {
           System.out.println("Vehicle is not available for renting.");
       }
   }

   // Return a vehicle and update rental status
   public void returnVehicle(Vehicle vehicle, Customer customer, LocalDate date, double extraFees) {
       if (vehicle.getStatus() == Vehicle.VehicleStatus.RENTED) {
           vehicle.setStatus(Vehicle.VehicleStatus.AVAILABLE);  // Update vehicle status to available
           RentalRecord record = new RentalRecord(vehicle, customer, date, extraFees, "RETURN");
           rentalHistory.addRecord(record);  // Add return record to history
           saveRecord(record);  // Save return record to a file
           System.out.println("Vehicle returned by " + customer.getCustomerName());
       } else {
           System.out.println("Vehicle is not rented.");
       }
   }

   // Display all available vehicles
   public void displayAvailableVehicles() {
       System.out.println("|     Type         |\tPlate\t|\tMake\t|\tModel\t|\tYear\t|");
       System.out.println("---------------------------------------------------------------------------------");

       for (Vehicle v : vehicles) {
           if (v.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
               System.out.println("|     " + (v instanceof Car ? "Car          " : "Motorcycle   ") + "|\t" + v.getLicensePlate() + "\t|\t" + v.getMake() + "\t|\t" + v.getModel() + "\t|\t" + v.getYear() + "\t|\t");
           }
       }
       System.out.println();
   }

   // Display details of all vehicles
   public void displayAllVehicles() {
       for (Vehicle v : vehicles) {
           System.out.println("  " + v.getInfo());
       }
   }

   // Display details of all customers
   public void displayAllCustomers() {
       for (Customer c : customers) {
           System.out.println("  " + c.toString());
       }
   }

   // Display the rental history
   public void displayRentalHistory() {
       for (RentalRecord record : rentalHistory.getRentalHistory()) {
           System.out.println(record.toString());
       }
   }

   // Find a vehicle by its license plate
   public Vehicle findVehicleByPlate(String plate) {
       for (Vehicle v : vehicles) {
           if (v.getLicensePlate().equalsIgnoreCase(plate)) {
               return v;
           }
       }
       return null;  // Return null if no matching vehicle is found
   }

   // Find a customer by their ID
   public Customer findCustomerById(int id) {
       for (Customer c : customers)
           if (c.getCustomerId() == id)
               return c;
       return null;  // Return null if no matching customer is found
   }

   // Find a customer by their name
   public Customer findCustomerByName(String name) {
       for (Customer c : customers)
           if (c.getCustomerName().equalsIgnoreCase(name))
               return c;
       return null;  // Return null if no matching customer is found
   }

   // Save a vehicle's details to a file
   public void saveVehicle(Vehicle vehicle) {
       try (FileWriter writer = new FileWriter("vehicles.txt", true)) {
           writer.write(vehicle.getLicensePlate() + "," + vehicle.getMake() + "," + vehicle.getModel() + "," + vehicle.getYear() + "," + vehicle.getClass().getSimpleName() + "\n");
       } catch (IOException e) {
           System.out.println("Error saving vehicle: " + e.getMessage());
       }
   }

   // Save a customer's details to a file
   public void saveCustomer(Customer customer) {
       try (FileWriter writer = new FileWriter("customers.txt", true)) {
           writer.write(customer.getCustomerId() + "," + customer.getCustomerName() + "\n");
       } catch (IOException e) {
           System.out.println("Error saving customer: " + e.getMessage());
       }
   }

   // Save a rental record to a file
   public void saveRecord(RentalRecord record) {
       try (FileWriter writer = new FileWriter("rental_records.txt", true)) {
           writer.write(record.toString() + "\n");
       } catch (IOException e) {
           System.out.println("Error saving rental record: " + e.getMessage());
       }
   }

   // Load data from files (customers, vehicles, and rental records)
   private void loadData() {
       // load customers
       try (BufferedReader reader = new BufferedReader(new FileReader("customers.txt"))) {
           String line;
           while ((line = reader.readLine()) != null) {
               String[] parts = line.split(",");
               int id = Integer.parseInt(parts[0]);
               String name = parts[1];
               customers.add(new Customer(id, name));
           }
       } catch (Exception e) {
           System.out.println("No existing customers to load.");
       }

       // load vehicles
       try (BufferedReader reader = new BufferedReader(new FileReader("vehicles.txt"))) {
           String line;
           while ((line = reader.readLine()) != null) {
               String[] parts = line.split(",");
               String plate = parts[0];
               String make = parts[1];
               String model = parts[2];
               int year = Integer.parseInt(parts[3]);
               String type = parts[4];

               Vehicle vehicle = null;
               if (type.equals("Car")) {
                   vehicle = new Car(make, model, year, 4);
               } else if (type.equals("Motorcycle")) {
                   vehicle = new Motorcycle(make, model, year, false);
               } else if (type.equals("Truck")) {
                   vehicle = new Truck(make, model, year, 1000);
               }

               if (vehicle != null) {
                   vehicle.setLicensePlate(plate);
                   vehicles.add(vehicle);
               }
           }
       } catch (Exception e) {
           System.out.println("No existing vehicles to load.");
       }

       // load rental records
       try (BufferedReader reader = new BufferedReader(new FileReader("rental_records.txt"))) {
           String line;
           while ((line = reader.readLine()) != null) {
               String[] parts = line.split(" \\| ");
               if (parts.length != 5) continue;

               String type = parts[0].trim();
               String plate = parts[1].split(":")[1].trim();
               String customerName = parts[2].split(":")[1].trim();
               String dateStr = parts[3].split(":")[1].trim();
               String amountStr = parts[4].split(":")[1].replace("$", "").trim();

               Vehicle vehicle = findVehicleByPlate(plate);
               Customer customer = findCustomerByName(customerName);

               if (vehicle != null && customer != null) {
                   LocalDate date = LocalDate.parse(dateStr);
                   double amount = Double.parseDouble(amountStr);
                   RentalRecord record = new RentalRecord(vehicle, customer, date, amount, type);
                   rentalHistory.addRecord(record);
               }
           }
       } catch (Exception e) {
           System.out.println("No rental records to load.");
       }
   }
}
