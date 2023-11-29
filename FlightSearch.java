package application;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalTime;

public class FlightSearch {

    public void display(Stage primaryStage) {
        Stage window = new Stage();

        // Set up the UI
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Flight Search");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        // Create search form fields
        Label fromCityLabel = new Label("From City:");
        TextField fromCityInput = new TextField();

        Label toCityLabel = new Label("To City:");
        TextField toCityInput = new TextField();

        Label dateLabel = new Label("Date (yyyy-mm-dd):");
        DatePicker datePicker = new DatePicker();

        Label timeLabel = new Label("Time (HH:MM):");
        TextField timeInput = new TextField();

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> {
            String fromCity = fromCityInput.getText();
            String toCity = toCityInput.getText();
            LocalDate date = datePicker.getValue();
            LocalTime time = LocalTime.parse(timeInput.getText()); // Add error handling for time parsing
            searchFlights(fromCity, toCity, date, time);
        });

        // Add components to the grid
        grid.add(fromCityLabel, 0, 0);
        grid.add(fromCityInput, 1, 0);
        grid.add(toCityLabel, 0, 1);
        grid.add(toCityInput, 1, 1);
        grid.add(dateLabel, 0, 2);
        grid.add(datePicker, 1, 2);
        grid.add(timeLabel, 0, 3);
        grid.add(timeInput, 1, 3);
        grid.add(searchButton, 1, 4);

        Scene scene = new Scene(grid, 400, 300);
        window.setScene(scene);
        window.showAndWait();
    }

    private void searchFlights(String fromCity, String toCity, LocalDate date, LocalTime time) {
        // Implement the database query and result display logic
        try (Connection connection = DBConnection.getConnection();
             // Adjust the SQL query to match the actual column names and the desired search criteria
             PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM flights WHERE from_city = ? AND to_city = ? AND departure_date = ? AND departure_time >= ?")) {
            statement.setString(1, fromCity);
            statement.setString(2, toCity);
            statement.setDate(3, java.sql.Date.valueOf(date));
            // Assuming that you want to find flights after a certain time
            statement.setTime(4, java.sql.Time.valueOf(time));
            ResultSet resultSet = statement.executeQuery();

            // Process the result set and display the flights
            while (resultSet.next()) {
                // Extract flight details from resultSet and display them
                // Here you can either print out the results or use a UI component like TableView to display them
                System.out.println("Flight ID: " + resultSet.getInt("flight_id"));
                System.out.println("From City: " + resultSet.getString("from_city"));
                System.out.println("To City: " + resultSet.getString("to_city"));
                // ... extract other fields
                System.out.println("Departure Date: " + resultSet.getDate("departure_date"));
                System.out.println("Departure Time: " + resultSet.getTime("departure_time"));
                // ... extract other fields
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
