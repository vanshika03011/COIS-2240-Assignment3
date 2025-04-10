import java.util.Scanner;
import java.time.LocalDate;

public class VehicleRentalApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        RentalSystem rentalSystem = RentalSystem.getInstance(); // using singleton

        while (true) {
            System.out.println("1: Add Vehicle\n2: Add Customer\n3: Rent Vehicle\n4: Return Vehicle\n5: Display Available Vehicles\n6: Show Rental History\n7: Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // fix

            switch (choice) {
                case 1:
                    System.out.println("  \n1: Car\n  2: Motorcycle\n  3: Truck");
                    int type = scanner.nextInt();
                    scanner.nextLine(); // fix

                    System.out.print("Enter license plate: ");
                    String plate = scanner.nextLine().toUpperCase();
                    System.out.print("Enter make: ");
                    String make = scanner.nextLine();
                    System.out.print("Enter model: ");
                    String model = scanner.nextLine();
                    System.out.print("Enter year: ");
                    int year = scanner.nextInt();
                    scanner.nextLine(); // fix

                    Vehicle vehicle;
                    if (type == 1) {
                        System.out.print("Enter number of seats: ");
                        int seats = scanner.nextInt();
                        scanner.nextLine(); // fix
                        vehicle = new Car(make, model, year, seats);
                    } else if (type == 2) {
                        System.out.print("Has sidecar? (true/false): ");
                        boolean sidecar = scanner.nextBoolean();
                        scanner.nextLine(); // fix
                        vehicle = new Motorcycle(make, model, year, sidecar);
                    } else if (type == 3) {
                        System.out.print("Enter the cargo capacity: ");
                        double cargoCapacity = scanner.nextDouble();
                        scanner.nextLine(); // fix
                        vehicle = new Truck(make, model, year, cargoCapacity);
                    } else {
                        vehicle = null;
                    }

                    if (vehicle != null) {
                        try {
                            vehicle.setLicensePlate(plate); // validate here
                            rentalSystem.addVehicle(vehicle);
                            System.out.println("Vehicle added.");
                        } catch (IllegalArgumentException e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Vehicle not added.");
                    }
                    break;

                case 2:
                    System.out.print("Enter customer ID: ");
                    int cid = scanner.nextInt();
                    scanner.nextLine(); // fix
                    scanner.nextLine(); 
                    System.out.print("Enter name: ");
                    String cname = scanner.nextLine();

                    rentalSystem.addCustomer(new Customer(cid, cname));
                    System.out.println("Customer added.");
                    break;

                    
                case 3:
                    System.out.println("List of Vehicles:");
                    rentalSystem.displayAvailableVehicles();

                    System.out.print("Enter license plate: ");
                    String rentPlate = scanner.nextLine().toUpperCase();

                    System.out.println("Registered Customers:");
                    rentalSystem.displayAllCustomers();

                    System.out.print("Enter customer name: ");
                    String cnameRent = scanner.nextLine();

                    System.out.print("Enter rental amount: ");
                    double rentAmount = scanner.nextDouble();
                    scanner.nextLine(); // fix

                    Vehicle vehicleToRent = rentalSystem.findVehicleByPlate(rentPlate);
                    Customer customerToRent = rentalSystem.findCustomerByName(cnameRent);

                    if (vehicleToRent == null || customerToRent == null) {
                        System.out.println("Vehicle or customer not found.");
                        break;
                    }

                    rentalSystem.rentVehicle(vehicleToRent, customerToRent, LocalDate.now(), rentAmount);
                    break;

                case 4:
                    System.out.println("List of Vehicles:");
                    rentalSystem.displayAvailableVehicles();

                    System.out.print("Enter license plate: ");
                    String returnPlate = scanner.nextLine().toUpperCase();

                    System.out.println("Registered Customers:");
                    rentalSystem.displayAllCustomers();

                    System.out.print("Enter customer name: ");
                    String cnameReturn = scanner.nextLine();

                    System.out.print("Enter return fees: ");
                    double returnFees = scanner.nextDouble();
                    scanner.nextLine(); // fix

                    Vehicle vehicleToReturn = rentalSystem.findVehicleByPlate(returnPlate);
                    Customer customerToReturn = rentalSystem.findCustomerByName(cnameReturn);

                    if (vehicleToReturn == null || customerToReturn == null) {
                        System.out.println("Vehicle or customer not found.");
                        break;
                    }

                    rentalSystem.returnVehicle(vehicleToReturn, customerToReturn, LocalDate.now(), returnFees);
                    break;

                case 5:
                    rentalSystem.displayAvailableVehicles();
                    break;

                case 6:
                    System.out.println("Rental History:");
                    rentalSystem.displayRentalHistory();
                    break;

                case 7:
                    scanner.close();
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}
