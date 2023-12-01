package application;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class UserFlightsDAO {

    // Method to retrieve all flights booked by a specific user
    public List<Flight> getUserFlights(int userId) {
        List<Flight> flights = new ArrayList<>();
        String sql = "SELECT f.* FROM user_flights uf INNER JOIN flights f ON uf.flight_id = f.flight_id WHERE uf.user_id = ?";
        
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
             
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                int flightId = rs.getInt("flight_id");
                String fromCity = rs.getString("from_city");
                String toCity = rs.getString("to_city");
                LocalDate departureDate = rs.getDate("departure_date").toLocalDate(); // Assuming the column name is 'departure_date'
                LocalTime departureTime = rs.getTime("departure_time").toLocalTime(); // Assuming the column name is 'departure_time'
                LocalDate arrivalDate = rs.getDate("arrival_date").toLocalDate(); // Assuming the column name is 'arrival_date'
                LocalTime arrivalTime = rs.getTime("arrival_time").toLocalTime(); // Assuming the column name is 'arrival_time'
                int capacity = rs.getInt("capacity"); // Assuming the column name is 'capacity'

                Flight flight = new Flight(
                    flightId,
                    fromCity,
                    toCity,
                    departureDate,
                    departureTime,
                    arrivalDate,
                    arrivalTime,
                    capacity
                );
                flights.add(flight);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return flights;
    }

    // Method to delete a flight that a user has booked
    public boolean deleteUserFlight(int userId, int flightId) {
        String sql = "DELETE FROM user_flights WHERE user_id = ? AND flight_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement pstmt = connection.prepareStatement(sql)) {
             
            pstmt.setInt(1, userId);
            pstmt.setInt(2, flightId);
            
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
