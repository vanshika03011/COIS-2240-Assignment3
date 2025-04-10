public abstract class Vehicle implements Rentable {
    private String licensePlate;
    private String make;
    private String model;
    private int year;
    private VehicleStatus status;

    public enum VehicleStatus {
        AVAILABLE, RENTED
    }

    public Vehicle(String make, String model, int year) {
        this.make = capitalize(make);
        this.model = capitalize(model);
        this.year = year;
        this.status = VehicleStatus.AVAILABLE;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        if (!isValidPlate(licensePlate)) {
            throw new IllegalArgumentException("Invalid license plate. Must be 3 uppercase letters followed by 3 digits (e.g., ABC123).");
        }
        this.licensePlate = licensePlate;
    }

    public String getMake() {
        return make;
    }

    public String getModel() {
        return model;
    }

    public int getYear() {
        return year;
    }

    public VehicleStatus getStatus() {
        return status;
    }

    public void setStatus(VehicleStatus status) {
        this.status = status;
    }

    protected String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase();
    }

    private boolean isValidPlate(String plate) {
        return plate != null && plate.matches("^[A-Z]{3}\\d{3}$");
    }

    public abstract String getInfo();
}
