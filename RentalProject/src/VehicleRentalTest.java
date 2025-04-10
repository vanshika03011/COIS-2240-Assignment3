import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VehicleRentalTest {

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
}
