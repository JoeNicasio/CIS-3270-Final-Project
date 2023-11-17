package application;

import java.sql.Connection;

import application.Registration;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        // Create buttons
        Button btnLogin = new Button("Login");
        Button btnRegister = new Button("Register");
        
        VBox layout = new VBox(20);  // 20px spacing between elements
        layout.getChildren().addAll(btnLogin, btnRegister);
        
        Scene scene = new Scene(layout, 300, 200);  // 300x200 px window
        
        primaryStage.setTitle("AirFlight Reservation System");
        scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
        
        btnRegister.setOnAction(e -> {
            Registration registration = new Registration();
            registration.display(primaryStage);
        });
        
        btnLogin.setOnAction(e -> {
            LoginForm loginForm = new LoginForm();
            loginForm.display(primaryStage);
        });
    }
}