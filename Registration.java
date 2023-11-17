package application;

import javafx.stage.*;
import javafx.scene.layout.GridPane;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.geometry.*;

public class Registration {

    // Fields for the registration form
    private TextField firstNameInput = new TextField();
    private TextField lastNameInput = new TextField();
    private TextField addressInput = new TextField();
    private TextField zipInput = new TextField();
    private TextField stateInput = new TextField();
    private TextField usernameInput = new TextField();
    private PasswordField passwordInput = new PasswordField();
    private TextField emailInput = new TextField();
    private TextField ssnInput = new TextField();
    private TextField securityQuestionInput = new TextField();

    public void display(Stage primaryStage) {
        Stage window = new Stage();

        // Block events to other windows
        window.initModality(Modality.APPLICATION_MODAL);
        window.initOwner(primaryStage);
        window.setTitle("Registration");

        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        // Labels for the registration form
        Label firstNameLabel = new Label("First Name:");
        Label lastNameLabel = new Label("Last Name:");
        Label addressLabel = new Label("Address:");
        Label zipLabel = new Label("ZIP:");
        Label stateLabel = new Label("State:");
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        Label emailLabel = new Label("Email:");
        Label ssnLabel = new Label("SSN:");
        Label securityQuestionLabel = new Label("What is your street name?:");

        // Button for form submission
        Button submitButton = new Button("Register");
        submitButton.setOnAction(e -> onRegisterButtonClick());

        // Adding components to the grid
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

        // Setting the scene
        Scene scene = new Scene(grid, 400, 500);
        window.setScene(scene);
        window.showAndWait();
    }

    // Event handler for registration button click
    private void onRegisterButtonClick() {
        // Collect form data
        String firstName = firstNameInput.getText();
        String lastName = lastNameInput.getText();
        String address = addressInput.getText();
        String zip = zipInput.getText();
        String state = stateInput.getText();
        String username = usernameInput.getText();
        String password = passwordInput.getText(); // Remember to hash the password in a real application
        String email = emailInput.getText();
        String ssn = ssnInput.getText();
        String securityQuestion = securityQuestionInput.getText();

        // Create a new user object with the form data
        User newUser = new User(firstName, lastName, address, zip, state, username, password, email, ssn, securityQuestion, securityQuestion);

        // Use UserDAO to interact with the database
        UserDAO userDAO = new UserDAO();
        boolean isRegistered = userDAO.createUser(newUser);

        if (isRegistered) {
            // Registration successful, proceed with your logic
            System.out.println("Registration successful!");
        } else {
            // Registration failed, notify the user
            System.out.println("Registration failed. Please try again.");
        }
    }
}

