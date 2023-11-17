package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class UserDAO {
    
    private DBConnection dbConnection;

    public UserDAO() {
        dbConnection = new DBConnection();
    }

    public boolean createUser(User user) {
        String insertQuery = "INSERT INTO users (first_name, last_name, address, zip, state, username, password, email, ssn, security_question, security_answer) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertQuery)) {
            
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getAddress());
            preparedStatement.setString(4, user.getZip());
            preparedStatement.setString(5, user.getState());
            preparedStatement.setString(6, user.getUsername());
            preparedStatement.setString(7, user.getPassword()); // Remember to hash the password in a real application
            preparedStatement.setString(8, user.getEmail());
            preparedStatement.setString(9, user.getSsn()); // Make sure you handle this securely
            preparedStatement.setString(10, user.getSecurityQuestion());
            preparedStatement.setString(11, user.getSecurityAnswer());
            
            int affectedRows = preparedStatement.executeUpdate();
            
            return affectedRows > 0;
            
        } catch (SQLException e) {
            e.printStackTrace(); // In production, you'd want to handle this more gracefully
            return false;
        }
    }

    // Other CRUD operations (read, update, delete) would go here...

}
