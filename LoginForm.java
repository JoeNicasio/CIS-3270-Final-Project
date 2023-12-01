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

    public void display(Window main) {
        Stage window = new Stage();

        // UI setup for login form
        GridPane grid = createLoginFormGrid();
        Scene scene = new Scene(grid, 300, 250);
        window.initModality(Modality.APPLICATION_MODAL);
        window.initOwner(main);
        window.setTitle("Login");
        window.setScene(scene);
        window.showAndWait();
    }

    private GridPane createLoginFormGrid() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setVgap(10);
        grid.setHgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Label usernameLabel = new Label("Username:");
        TextField usernameInput = new TextField();
        Label passwordLabel = new Label("Password:");
        PasswordField passwordInput = new PasswordField();

        Button loginButton = new Button("Login");
        loginButton.setOnAction(e -> attemptLogin(usernameInput.getText(), passwordInput.getText(), (Stage) loginButton.getScene().getWindow()));

        Button forgotPasswordButton = new Button("Forgot Password?");
        forgotPasswordButton.setOnAction(e -> displaySecurityQuestion(usernameInput.getText()));

        grid.add(usernameLabel, 0, 0);
        grid.add(usernameInput, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordInput, 1, 1);
        grid.add(loginButton, 1, 2);
        grid.add(forgotPasswordButton, 1, 3);

        return grid;
    }

    private void attemptLogin(String username, String password, Stage loginStage) {
        User user = authenticate(username, password);
        if (user != null) {
            SessionManager.login(user); // Log in the user
            loginStage.close(); // Close the login window
            UserDashboard userDashboard = new UserDashboard();
            userDashboard.display(new Stage()); // Open the user dashboard
        } else {
            showAlert(Alert.AlertType.ERROR, loginStage, "Login Error", "Invalid username or password");
        }
    }

    private User authenticate(String username, String password) {
        // Implement database query to check credentials
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?")) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String firstName = resultSet.getString("first_name");
                // ... other user attributes as needed
                return new User(id, firstName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; // User not found or error occurred
    }
    private void displaySecurityQuestion(String username) {
        // Set the default security question
        String securityQuestion = "What is your street?";
        
        Stage questionStage = new Stage();
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));
        layout.setAlignment(Pos.CENTER);

        Label questionLabel = new Label(securityQuestion);
        TextField answerInput = new TextField();
        Button submitButton = new Button("Submit Answer");

        submitButton.setOnAction(e -> {
            boolean answerCorrect = checkSecurityAnswer(username, answerInput.getText(), questionStage);
            if (answerCorrect) {
                questionStage.close(); // Close the security question window if the answer is correct
            } else {
                showAlert(Alert.AlertType.ERROR, questionStage, "Security Error", "Incorrect answer to security question.");
            }
        });

        layout.getChildren().addAll(questionLabel, answerInput, submitButton);

        Scene scene = new Scene(layout, 300, 150);
        questionStage.initModality(Modality.APPLICATION_MODAL);
        questionStage.setTitle("Security Question");
        questionStage.setScene(scene);
        questionStage.showAndWait();
    }

    private boolean checkSecurityAnswer(String username, String answer, Stage owner) {
        String sql = "SELECT security_answer, password FROM users WHERE username = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                String correctAnswer = resultSet.getString("security_answer");
                if (correctAnswer.equals(answer)) {
                    // User answered security question correctly
                    String password = resultSet.getString("password");
                    showAlert(Alert.AlertType.INFORMATION, owner, "Password Recovery", "Your password is: " + password);
                    return true;
                } else {
                    showAlert(Alert.AlertType.ERROR, owner, "Security Error", "Incorrect answer to security question.");
                    return false;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }


    private void showAlert(Alert.AlertType alertType, Stage owner, String title, String message) {
        Alert alert = new Alert(alertType);
        if (owner != null) {
            alert.initOwner(owner);
        }
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

