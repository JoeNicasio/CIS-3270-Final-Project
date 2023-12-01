package application;

import java.time.LocalDate;
import java.time.LocalTime;

public class Flight {
    private int flightId;
    private String fromCity;
    private String toCity;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private LocalDate arrivalDate;
    private LocalTime arrivalTime;
    private int capacity;

    public Flight(int flightId, String fromCity, String toCity, LocalDate departureDate,
                  LocalTime departureTime, LocalDate arrivalDate, LocalTime arrivalTime, int capacity) {
        this.flightId = flightId;
        this.fromCity = fromCity;
        this.toCity = toCity;
        this.departureDate = departureDate;
        this.departureTime = departureTime;
        this.arrivalDate = arrivalDate;
        this.arrivalTime = arrivalTime;
        this.capacity = capacity;
    }

    // Getters
    public int getFlightId() {
        return flightId;
    }

    public String getFromCity() {
        return fromCity;
    }

    public String getToCity() {
        return toCity;
    }

    public LocalDate getDepartureDate() {
        return departureDate;
    }

    public LocalTime getDepartureTime() {
        return departureTime;
    }

    public LocalDate getArrivalDate() {
        return arrivalDate;
    }

    public LocalTime getArrivalTime() {
        return arrivalTime;
    }

    public int getCapacity() {
        return capacity;
    }

    // Setters and other methods can be added as necessary
}
