import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class VehicleRentalTest {

    private RentalSystem rentalSystem;
    private Vehicle testCar;
    private Customer testCustomer;

    @BeforeEach
    public void setUp() {
        rentalSystem = RentalSystem.getInstance();

        rentalSystem.getVehicles().clear();
        rentalSystem.getCustomers().clear();
        rentalSystem.getRentalHistory().getRentalHistory().clear();

        testCar = new Car("Toyota", "Camry", 2020, 4);
        testCar.setLicensePlate("ABC123");

        testCustomer = new Customer(1, "Vanshika");

        rentalSystem.addVehicle(testCar);
        rentalSystem.addCustomer(testCustomer);
    }

    @Test
    public void testLicensePlateValidation() {
        assertDoesNotThrow(() -> {
            Vehicle car1 = new Car("Toyota", "Camry", 2020, 4);
            car1.setLicensePlate("AAA100");

            Vehicle car2 = new Car("Honda", "Civic", 2021, 4);
            car2.setLicensePlate("ABC567");

            Vehicle car3 = new Car("Ford", "Focus", 2022, 4);
            car3.setLicensePlate("ZZZ999");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Vehicle car = new Car("Toyota", "Yaris", 2020, 4);
            car.setLicensePlate("");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Vehicle car = new Car("Toyota", "Yaris", 2020, 4);
            car.setLicensePlate(null);
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Vehicle car = new Car("Toyota", "Yaris", 2020, 4);
            car.setLicensePlate("AAA1000");
        });

        assertThrows(IllegalArgumentException.class, () -> {
            Vehicle car = new Car("Toyota", "Yaris", 2020, 4);
            car.setLicensePlate("ZZZ99");
        });
    }

    @Test
    public void testRentAndReturnVehicle() {
        // rent the car
        rentalSystem.rentVehicle(testCar, testCustomer, LocalDate.of(2025, 4, 10), 250.0);
        assertEquals(Vehicle.VehicleStatus.RENTED, testCar.getStatus());

        // return the car
        rentalSystem.returnVehicle(testCar, testCustomer, LocalDate.of(2025, 4, 12), 25.0);
        assertEquals(Vehicle.VehicleStatus.AVAILABLE, testCar.getStatus());

        // check rental history has two records (rent + return)
        assertEquals(2, rentalSystem.getRentalHistory().getRentalHistory().size());
    }
}
