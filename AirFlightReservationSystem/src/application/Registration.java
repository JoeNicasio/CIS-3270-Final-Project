package application;

import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class Registration{

    public void display(Stage primaryStage) {
        Stage window = new Stage();

        // Block input events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.initOwner(primaryStage);
        window.setTitle("Registration");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        // Create registration form fields
        Label firstNameLabel = new Label("First Name:");
        TextField firstNameInput = new TextField();
        Label lastNameLabel = new Label("Last Name:");
        TextField lastNameInput = new TextField();
        Label addressLabel = new Label("Address:");
        TextField addressInput = new TextField();
        Label zipLabel = new Label("ZIP:");
        TextField zipInput = new TextField();
        Label stateLabel = new Label("State:");
        TextField stateInput = new TextField();
        Label usernameLabel = new Label("Username:");
        TextField usernameInput = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordInput = new PasswordField();
        Label emailLabel = new Label("Email:");
        TextField emailInput = new TextField();
        Label ssnLabel = new Label("SSN:");
        TextField ssnInput = new TextField();
        Label securityQuestionLabel = new Label("Security Question:");
        TextField securityQuestionInput = new TextField();

        Button submitButton = new Button("Register");
        // Add logic for when the registration button is clicked (store info, etc.)
        // submitButton.setOnAction(e -> { /* Handle registration logic */ });

        // Add components to the grid
        grid.add(firstNameLabel, 0, 0);
        grid.add(firstNameInput, 1, 0);
        grid.add(lastNameLabel, 0, 1);
        grid.add(lastNameInput, 1, 1);
        grid.add(addressLabel, 0, 2);
        grid.add(addressInput, 1, 2);
        grid.add(zipLabel, 0, 3);
        grid.add(zipInput, 1, 3);
        grid.add(stateLabel, 0, 4);
        grid.add(stateInput, 1, 4);
        grid.add(usernameLabel, 0, 5);
        grid.add(usernameInput, 1, 5);
        grid.add(passwordLabel, 0, 6);
        grid.add(passwordInput, 1, 6);
        grid.add(emailLabel, 0, 7);
        grid.add(emailInput, 1, 7);
        grid.add(ssnLabel, 0, 8);
        grid.add(ssnInput, 1, 8);
        grid.add(securityQuestionLabel, 0, 9);
        grid.add(securityQuestionInput, 1, 9);
        grid.add(submitButton, 1, 10);

        Scene scene = new Scene(grid, 400, 500);
        window.setScene(scene);
        window.showAndWait();
    }
}

