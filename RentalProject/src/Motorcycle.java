public class Motorcycle extends Vehicle {
    private boolean hasSidecar;

    public Motorcycle(String make, String model, int year, boolean hasSidecar) {
        super(make, model, year);
        this.hasSidecar = hasSidecar;
    }

    public boolean hasSidecar() {
        return hasSidecar;
    }

    public void setHasSidecar(boolean hasSidecar) {
        this.hasSidecar = hasSidecar;
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
        return "Motorcycle - Plate: " + getLicensePlate() +
               ", Make: " + getMake() +
               ", Model: " + getModel() +
               ", Year: " + getYear() +
               ", Sidecar: " + (hasSidecar ? "Yes" : "No") +
               ", Status: " + getStatus();
    }
}
