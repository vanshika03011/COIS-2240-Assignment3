public class Truck extends Vehicle {
    private double cargoCapacity;

    public Truck(String make, String model, int year, double cargoCapacity) {
        super(make, model, year);
        this.cargoCapacity = cargoCapacity;
    }

    public double getCargoCapacity() {
        return cargoCapacity;
    }

    public void setCargoCapacity(double cargoCapacity) {
        this.cargoCapacity = cargoCapacity;
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
        return "Truck - Plate: " + getLicensePlate() +
               ", Make: " + getMake() +
               ", Model: " + getModel() +
               ", Year: " + getYear() +
               ", Cargo: " + cargoCapacity + "kg" +
               ", Status: " + getStatus();
    }
}
