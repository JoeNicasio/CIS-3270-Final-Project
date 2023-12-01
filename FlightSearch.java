package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class FlightSearch {

    private ObservableList<Flight> flights = FXCollections.observableArrayList();
    private TableView<Flight> table = new TableView<>();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss");

    private TextField fromCityInput = new TextField();
    private TextField toCityInput = new TextField();
    private DatePicker datePicker = new DatePicker();
    private TextField timeInput = new TextField();
    private Button searchButton = new Button("Search");
    private Button bookFlightButton = new Button("Book Flight");
    private Label messageLabel = new Label();

    private int currentUserId; // Assume this is set somewhere after user logs in

    public FlightSearch() {
        setupTableColumns();
        setupBookFlightButton();
        currentUserId = getCurrentUserId(); // Replace with actual user ID retrieval logic
    }

    private int getCurrentUserId() {
    	return SessionManager.getCurrentUserId();
    }

    private void setupBookFlightButton() {
        bookFlightButton.setOnAction(e -> {
            Flight selectedFlight = table.getSelectionModel().getSelectedItem();
            if (selectedFlight != null) {
                bookFlightForUser(selectedFlight.getFlightId(), currentUserId, SessionManager.getCurrentUserFirstName());
            } else {
                messageLabel.setText("Please select a flight to book.");
            }
        });
    }

    private void bookFlightForUser(int flightId, int userId, String first_name) {
    	if (isFlightFull(flightId)) {
            messageLabel.setText("This flight is already fully booked.");
            return;
        }
        // Check if the user has already booked this flight.
        if (userHasBookedFlight(flightId, userId)) {
            messageLabel.setText("You have already booked this flight.");
            return;
        }

        // Get the selected flight's departure date and time.
        Flight selectedFlight = table.getSelectionModel().getSelectedItem();
        LocalDate departureDate = selectedFlight.getDepartureDate();
        LocalTime departureTime = selectedFlight.getDepartureTime();

        // Check if the user has booked any flight that overlaps with this one.
        if (userHasOverlappingFlights(departureDate, departureTime, userId)) {
            messageLabel.setText("You have already booked a flight at this date and time.");
            return;
        }

        // Proceed with booking the flight.
        String sql = "INSERT INTO user_flights (user_id, flight_id, first_name, booking_date) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, flightId);
            pstmt.setString(3, first_name);
            pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                messageLabel.setText("Flight booked successfully.");
            } else {
                messageLabel.setText("Failed to book the flight.");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            messageLabel.setText("Error booking the flight.");
        }
    }

	private boolean isFlightFull(int flightId) {
        String sql = "SELECT capacity, COUNT(user_flights.id) AS bookings FROM flights " +
                     "LEFT JOIN user_flights ON flights.flight_id = user_flights.flight_id " +
                     "WHERE flights.flight_id = ? GROUP BY flights.flight_id";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, flightId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int capacity = rs.getInt("capacity");
                int bookings = rs.getInt("bookings");
                return bookings >= capacity;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean userHasBookedFlight(int flightId, int userId) {
        String sql = "SELECT COUNT(*) FROM user_flights WHERE user_id = ? AND flight_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setInt(2, flightId);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // If count is greater than 0, user has booked this flight.
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    private boolean userHasOverlappingFlights(LocalDate departureDate, LocalTime departureTime, int userId) {
        String sql = "SELECT COUNT(*) FROM user_flights uf " +
                     "JOIN flights f ON uf.flight_id = f.flight_id " +
                     "WHERE uf.user_id = ? AND f.departure_date = ? AND f.departure_time = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setDate(2, java.sql.Date.valueOf(departureDate));
            pstmt.setTime(3, java.sql.Time.valueOf(departureTime));

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // If count is greater than 0, there is an overlapping flight.
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


    public void display(Stage dashboardStage) {
        Stage searchStage = new Stage();

        // Scene and stage setup
        VBox layout = new VBox(10);
        layout.getChildren().addAll(
            new Label("From City:"), fromCityInput,
            new Label("To City:"), toCityInput,
            new Label("Date (yyyy-mm-dd):"), datePicker,
            new Label("Time (HH:MM):"), timeInput,
            searchButton, bookFlightButton, messageLabel, table
        );

        searchButton.setOnAction(e -> {
            String fromCity = fromCityInput.getText();
            String toCity = toCityInput.getText();
            LocalDate date = datePicker.getValue();
            LocalTime time = null;
            if (!timeInput.getText().isEmpty()) {
                time = LocalTime.parse(timeInput.getText(), TIME_FORMATTER);
            }
            searchFlights(fromCity, toCity, date, time);
        });

        Scene scene = new Scene(layout, 600, 400); // Adjust size as needed
        searchStage.setScene(scene);
        searchStage.setTitle("Flight Search");

        searchStage.setOnHidden(e -> dashboardStage.show()); // Show the dashboard when the search window is closed
        dashboardStage.hide(); // Hide the dashboard stage
        searchStage.showAndWait(); // Show the flight search stage and wait until it is closed
    }

    private void setupTableColumns() {
        TableColumn<Flight, Integer> flightIdColumn = new TableColumn<>("Flight ID");
        flightIdColumn.setCellValueFactory(new PropertyValueFactory<>("flightId"));

        TableColumn<Flight, String> fromCityColumn = new TableColumn<>("From City");
        fromCityColumn.setCellValueFactory(new PropertyValueFactory<>("fromCity"));

        TableColumn<Flight, String> toCityColumn = new TableColumn<>("To City");
        toCityColumn.setCellValueFactory(new PropertyValueFactory<>("toCity"));

        TableColumn<Flight, LocalDate> departureDateColumn = new TableColumn<>("Departure Date");
        departureDateColumn.setCellValueFactory(new PropertyValueFactory<>("departureDate"));

        TableColumn<Flight, LocalTime> departureTimeColumn = new TableColumn<>("Departure Time");
        departureTimeColumn.setCellValueFactory(new PropertyValueFactory<>("departureTime"));

        TableColumn<Flight, LocalDate> arrivalDateColumn = new TableColumn<>("Arrival Date");
        arrivalDateColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalDate"));

        TableColumn<Flight, LocalTime> arrivalTimeColumn = new TableColumn<>("Arrival Time");
        arrivalTimeColumn.setCellValueFactory(new PropertyValueFactory<>("arrivalTime"));

        TableColumn<Flight, Integer> capacityColumn = new TableColumn<>("Capacity");
        capacityColumn.setCellValueFactory(new PropertyValueFactory<>("capacity"));

        table.getColumns().addAll(flightIdColumn, fromCityColumn, toCityColumn,
                departureDateColumn, departureTimeColumn,
                arrivalDateColumn, arrivalTimeColumn, capacityColumn);
    }

    private void searchFlights(String fromCity, String toCity, LocalDate date, LocalTime time) {
        ObservableList<Flight> searchResults = FXCollections.observableArrayList();

        StringBuilder sqlBuilder = new StringBuilder("SELECT * FROM flights WHERE from_city = ? AND to_city = ? AND departure_date = ?");
        if (time != null) {
            sqlBuilder.append(" AND departure_time >= ?");
        }

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sqlBuilder.toString())) {

            stmt.setString(1, fromCity);
            stmt.setString(2, toCity);
            stmt.setDate(3, java.sql.Date.valueOf(date));
            if (time != null) {
                stmt.setTime(4, java.sql.Time.valueOf(time));
            }

            ResultSet resultSet = stmt.executeQuery();

            while (resultSet.next()) {
                Flight flight = new Flight(
                        resultSet.getInt("flight_id"),
                        resultSet.getString("from_city"),
                        resultSet.getString("to_city"),
                        resultSet.getDate("departure_date").toLocalDate(),
                        resultSet.getTime("departure_time").toLocalTime(),
                        resultSet.getDate("arrival_date").toLocalDate(),
                        resultSet.getTime("arrival_time").toLocalTime(),
                        resultSet.getInt("capacity")
                );
                searchResults.add(flight);
            }

            if (searchResults.isEmpty()) {
                messageLabel.setText("No flights available for the selected criteria.");
            } else {
                messageLabel.setText(""); // Clear the message if flights are found
            }

            table.setItems(searchResults);
        } catch (Exception ex) {
            ex.printStackTrace();
            // Handle exceptions
        }
    }
}
