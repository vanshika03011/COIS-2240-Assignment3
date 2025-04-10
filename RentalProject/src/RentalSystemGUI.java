import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.LocalDate;

public class RentalSystemGUI extends Application {

    private RentalSystem rentalSystem = RentalSystem.getInstance();

    private TextField plateField = new TextField();
    private TextField makeField = new TextField();
    private TextField modelField = new TextField();
    private TextField yearField = new TextField();
    private ComboBox<String> vehicleTypeBox = new ComboBox<>();

    private TextField seatsField = new TextField();
    private CheckBox sidecarBox = new CheckBox("Sidecar");
    private TextField cargoCapacityField = new TextField();

    private TextArea outputArea = new TextArea();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Vehicle Rental System");

        TabPane tabPane = new TabPane();
        tabPane.getTabs().addAll(
            createAddVehicleTab(),
            createAddCustomerTab(),
            createRentVehicleTab(),
            createReturnVehicleTab(),
            createAvailableVehiclesTab(),
            createRentalHistoryTab()
        );

        outputArea.setEditable(false);
        outputArea.setPrefHeight(150);
        ScrollPane outputScroll = new ScrollPane(outputArea);
        outputScroll.setFitToWidth(true);
        outputScroll.setFitToHeight(true);

        VBox content = new VBox(10, tabPane, outputScroll);
        content.setPadding(new Insets(10));

        Scene scene = new Scene(content, 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private Tab createAddVehicleTab() {
        Tab tab = new Tab("Add Vehicle");
        tab.setClosable(false);

        vehicleTypeBox.getItems().addAll("Car", "Motorcycle", "Truck");

        Button addButton = new Button("Add Vehicle");
        addButton.setOnAction(e -> addVehicle());

        VBox vbox = new VBox(10,
            new Label("License Plate:"), plateField,
            new Label("Make:"), makeField,
            new Label("Model:"), modelField,
            new Label("Year:"), yearField,
            new Label("Vehicle Type:"), vehicleTypeBox,
            new Label("Seats (Car):"), seatsField,
            sidecarBox,
            new Label("Cargo Capacity (Truck):"), cargoCapacityField,
            addButton
        );
        vbox.setPadding(new Insets(10));

        tab.setContent(new ScrollPane(vbox));
        return tab;
    }

    private void addVehicle() {
        try {
            String plate = plateField.getText().toUpperCase();
            String make = makeField.getText();
            String model = modelField.getText();
            int year = Integer.parseInt(yearField.getText());
            String type = vehicleTypeBox.getValue();

            Vehicle vehicle = null;
            switch (type) {
                case "Car":
                    int seats = Integer.parseInt(seatsField.getText());
                    vehicle = new Car(make, model, year, seats);
                    break;
                case "Motorcycle":
                    boolean sidecar = sidecarBox.isSelected();
                    vehicle = new Motorcycle(make, model, year, sidecar);
                    break;
                case "Truck":
                    double cargo = Double.parseDouble(cargoCapacityField.getText());
                    vehicle = new Truck(make, model, year, cargo);
                    break;
            }

            if (vehicle != null) {
                vehicle.setLicensePlate(plate);
                rentalSystem.addVehicle(vehicle);
                outputArea.setText("Vehicle added successfully.");
            }
        } catch (Exception ex) {
            outputArea.setText("Error adding vehicle: " + ex.getMessage());
        }
    }

    private Tab createAddCustomerTab() {
        Tab tab = new Tab("Add Customer");
        tab.setClosable(false);

        TextField idField = new TextField();
        TextField nameField = new TextField();
        Button addButton = new Button("Add Customer");

        addButton.setOnAction(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String name = nameField.getText();
                rentalSystem.addCustomer(new Customer(id, name));
                outputArea.setText("Customer added successfully.");
            } catch (Exception ex) {
                outputArea.setText("Error: " + ex.getMessage());
            }
        });

        VBox vbox = new VBox(10,
            new Label("Customer ID:"), idField,
            new Label("Name:"), nameField,
            addButton
        );
        vbox.setPadding(new Insets(10));

        tab.setContent(new ScrollPane(vbox));
        return tab;
    }

    private Tab createRentVehicleTab() {
        Tab tab = new Tab("Rent Vehicle");
        tab.setClosable(false);

        TextField plateField = new TextField();
        TextField customerNameField = new TextField();
        TextField amountField = new TextField();
        Button rentButton = new Button("Rent");

        rentButton.setOnAction(e -> {
            Vehicle v = rentalSystem.findVehicleByPlate(plateField.getText());
            Customer c = rentalSystem.findCustomerByName(customerNameField.getText());
            double amt = Double.parseDouble(amountField.getText());
            if (v != null && c != null) {
                rentalSystem.rentVehicle(v, c, LocalDate.now(), amt);
                outputArea.setText("Vehicle rented successfully.");
            } else {
                outputArea.setText("Vehicle or customer not found.");
            }
        });

        VBox vbox = new VBox(10,
            new Label("License Plate:"), plateField,
            new Label("Customer Name:"), customerNameField,
            new Label("Rental Amount:"), amountField,
            rentButton
        );
        vbox.setPadding(new Insets(10));

        tab.setContent(new ScrollPane(vbox));
        return tab;
    }

    private Tab createReturnVehicleTab() {
        Tab tab = new Tab("Return Vehicle");
        tab.setClosable(false);

        TextField plateField = new TextField();
        TextField customerNameField = new TextField();
        TextField feeField = new TextField();
        Button returnButton = new Button("Return");

        returnButton.setOnAction(e -> {
            Vehicle v = rentalSystem.findVehicleByPlate(plateField.getText());
            Customer c = rentalSystem.findCustomerByName(customerNameField.getText());
            double fee = Double.parseDouble(feeField.getText());
            if (v != null && c != null) {
                rentalSystem.returnVehicle(v, c, LocalDate.now(), fee);
                outputArea.setText("Vehicle returned successfully.");
            } else {
                outputArea.setText("Vehicle or customer not found.");
            }
        });

        VBox vbox = new VBox(10,
            new Label("License Plate:"), plateField,
            new Label("Customer Name:"), customerNameField,
            new Label("Return Fee:"), feeField,
            returnButton
        );
        vbox.setPadding(new Insets(10));

        tab.setContent(new ScrollPane(vbox));
        return tab;
    }

    private Tab createAvailableVehiclesTab() {
        Tab tab = new Tab("Available Vehicles");
        tab.setClosable(false);

        Button showButton = new Button("Show Available Vehicles");
        showButton.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            for (Vehicle v : rentalSystem.findAllVehicles()) {
                if (v.getStatus() == Vehicle.VehicleStatus.AVAILABLE) {
                    sb.append(v.getInfo()).append("\n");
                }
            }
            outputArea.setText(sb.toString());
        });

        VBox vbox = new VBox(10, showButton);
        vbox.setPadding(new Insets(10));
        tab.setContent(new ScrollPane(vbox));

        return tab;
    }

    private Tab createRentalHistoryTab() {
        Tab tab = new Tab("Rental History");
        tab.setClosable(false);

        Button showButton = new Button("Show Rental History");
        showButton.setOnAction(e -> {
            StringBuilder sb = new StringBuilder();
            for (RentalRecord record : rentalSystem.getRentalHistory().getRentalHistory()) {
                sb.append(record.toString()).append("\n");
            }
            outputArea.setText(sb.toString());
        });

        VBox vbox = new VBox(10, showButton);
        vbox.setPadding(new Insets(10));
        tab.setContent(new ScrollPane(vbox));

        return tab;
    }
}
