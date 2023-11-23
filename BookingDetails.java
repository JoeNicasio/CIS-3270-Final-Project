package application;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class BookingDetails extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Flight Booking Details");

        // Create a GridPane for the layout
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 20, 20, 20));

        // Add controls to the GridPane
        grid.add(new Label("Passenger Name:"), 0, 0);
        TextField passengerNameField = new TextField();
        grid.add(passengerNameField, 1, 0);

        grid.add(new Label("Departure:"), 0, 1);
        TextField departureField = new TextField();
        grid.add(departureField, 1, 1);

        grid.add(new Label("Destination:"), 0, 2);
        TextField destinationField = new TextField();
        grid.add(destinationField, 1, 2);

        grid.add(new Label("Departure Date:"), 0, 3);
        DatePicker departureDatePicker = new DatePicker();
        grid.add(departureDatePicker, 1, 3);

        grid.add(new Label("Class of Service:"), 0, 4);
        ComboBox<String> classComboBox = new ComboBox<>();
        classComboBox.getItems().addAll("Economy", "Business", "First Class");
        grid.add(classComboBox, 1, 4);
        
        grid.add(new Label("Credit Card Number;"), 0, 5);
        TextField creditField = new TextField();
        grid.add(creditField, 1, 5);

        // Add a button to submit the flight booking
        Button submitButton = new Button("Book Flight");
        submitButton.setOnAction(e -> {
            // Handle the flight booking submission logic here
            String passengerName = passengerNameField.getText();
            String departure = departureField.getText();
            String destination = destinationField.getText();
            String departureDate = departureDatePicker.getValue().toString();
            String classOfService = classComboBox.getValue();
            String credit = creditField.getText();

            // You can process the flight booking details as needed (e.g., store in a database)
            System.out.println("Flight Booking Details:");
            System.out.println("Passenger Name: " + passengerName);
            System.out.println("Departure: " + departure);
            System.out.println("Destination: " + destination);
            System.out.println("Departure Date: " + departureDate);
            System.out.println("Class of Service: " + classOfService);
            System.out.println("Credit Card Number; " + credit);
        });
        grid.add(submitButton, 1, 6);

        // Create a scene and set it on the stage
        Scene scene = new Scene(grid, 400, 300);
        primaryStage.setScene(scene);

        // Show the stage
        primaryStage.show();
    }
}
