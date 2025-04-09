public class Car extends Vehicle {
    private int numberOfSeats;

    public Car(String make, String model, int year, int numberOfSeats) {
        super(make, model, year);
        this.numberOfSeats = numberOfSeats;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    @Override
    public void rentVehicle() {
        setStatus(VehicleStatus.RENTED);
    }

    @Override
    public void returnVehicle() {
        setStatus(VehicleStatus.AVAILABLE);
    }

    @Override
    public String getInfo() {
        return "Car - Plate: " + getLicensePlate() +
               ", Make: " + getMake() +
               ", Model: " + getModel() +
               ", Year: " + getYear() +
               ", Seats: " + numberOfSeats +
               ", Status: " + getStatus();
    }
}
