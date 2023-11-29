package application;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginForm {

    public void display(Stage primaryStage) {
        Stage window = new Stage();

        // Block input events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.initOwner(primaryStage);
        window.setTitle("Login");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        // Create login form fields
        Label usernameLabel = new Label("Username:");
        TextField usernameInput = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordInput = new PasswordField();

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> {
            String username = usernameInput.getText();
            String password = passwordInput.getText();
            try {
                if (authenticate(username, password)) {
                    // Open the flight search window
                    FlightSearch flightSearch = new FlightSearch();
                    flightSearch.display(primaryStage);
                } else {
                    // Show error message
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Invalid username or password");
                    alert.showAndWait();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // Add components to the grid
        grid.add(usernameLabel, 0, 0);
        grid.add(usernameInput, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordInput, 1, 1);
        grid.add(loginButton, 1, 2);

        Scene scene = new Scene(grid, 300, 200);
        window.setScene(scene);
        window.showAndWait();
    }

    private boolean authenticate(String username, String password) {
        // Implement database query to check credentials
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true; // User found
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false; // User not found or error occurred
    }
}
