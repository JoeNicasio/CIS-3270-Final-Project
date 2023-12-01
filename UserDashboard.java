package application;


import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.util.List;

public class UserDashboard {

    // Assuming UserFlightsDAO and SessionManager are already implemented in your application
    private UserFlightsDAO userFlightsDAO = new UserFlightsDAO();
    private ObservableList<Flight> userFlights = FXCollections.observableArrayList();
    private TableView<Flight> flightsTable = new TableView<>();

    public void display(Stage primaryStage) {
        Stage dashboardStage = new Stage();

        dashboardStage.initModality(Modality.APPLICATION_MODAL);
        dashboardStage.initOwner(primaryStage);
        dashboardStage.setTitle("User Dashboard");

        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10, 10, 10, 10));
        layout.setAlignment(Pos.CENTER);

        Button bookFlightButton = new Button("Book a Flight");
        Button deleteFlightButton = new Button("Delete a Flight");

        bookFlightButton.setOnAction(e -> {
            FlightSearch flightSearch = new FlightSearch();
            flightSearch.display(dashboardStage);
        });

        deleteFlightButton.setOnAction(e -> displayUserFlights(dashboardStage));

        layout.getChildren().addAll(bookFlightButton, deleteFlightButton);

        Scene scene = new Scene(layout, 300, 200);
        dashboardStage.setScene(scene);
        dashboardStage.showAndWait();
    }

    private void displayUserFlights(Stage dashboardStage) {
        Stage flightsStage = new Stage();

        flightsStage.initModality(Modality.APPLICATION_MODAL);
        flightsStage.initOwner(dashboardStage);
        flightsStage.setTitle("Your Flights");

        setupFlightsTable();
        populateUserFlightsTable();

        Button deleteFlightButton = new Button("Delete Selected Flight");
        deleteFlightButton.setOnAction(e -> {
            Flight selectedFlight = flightsTable.getSelectionModel().getSelectedItem();
            if (selectedFlight != null) {
                boolean success = userFlightsDAO.deleteUserFlight(SessionManager.getCurrentUserId(), selectedFlight.getFlightId());
                if (success) {
                    userFlights.remove(selectedFlight);
                    // Show success message
                    new Alert(Alert.AlertType.INFORMATION, "Flight deleted successfully.").showAndWait();
                } else {
                    // Show error message
                    new Alert(Alert.AlertType.ERROR, "Failed to delete the flight.").showAndWait();
                }
            } else {
                new Alert(Alert.AlertType.WARNING, "Please select a flight to delete.").showAndWait();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(flightsTable, deleteFlightButton);

        Scene scene = new Scene(layout);
        flightsStage.setScene(scene);
        flightsStage.showAndWait();
    }

    @SuppressWarnings("unchecked")
	private void setupFlightsTable() {
        TableColumn<Flight, Integer> flightIdColumn = new TableColumn<>("Flight ID");
        flightIdColumn.setCellValueFactory(new PropertyValueFactory<>("flightId"));

        TableColumn<Flight, String> fromCityColumn = new TableColumn<>("From City");
        fromCityColumn.setCellValueFactory(new PropertyValueFactory<>("fromCity"));

        TableColumn<Flight, String> toCityColumn = new TableColumn<>("To City");
        toCityColumn.setCellValueFactory(new PropertyValueFactory<>("toCity"));

        TableColumn<Flight, String> departureTimeColumn = new TableColumn<>("Departure Time");
        departureTimeColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));

        TableColumn<Flight, String> arrivalTimeColumn = new TableColumn<>("Arrival Time");
        arrivalTimeColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));

        flightsTable.getColumns().addAll(flightIdColumn, fromCityColumn, toCityColumn, departureTimeColumn, arrivalTimeColumn);
        flightsTable.setItems(userFlights);
    }


    private void populateUserFlightsTable() {
        int userId = SessionManager.getCurrentUserId();
        List<Flight> flights = userFlightsDAO.getUserFlights(userId);
        userFlights.setAll(flights);
    }
}
