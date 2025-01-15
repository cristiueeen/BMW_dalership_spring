package com.example.bmw_dalership_spring.model;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class ServiceAppointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate appointmentDate;
    private double repairPrice;
    private int estimatedRepairTime; // In hours
    private String status;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Factory method
    public static ServiceAppointment create(LocalDate date, double price, int repairTime, String status, Vehicle vehicle, User user) {
        ServiceAppointment appointment = new ServiceAppointment();
        appointment.appointmentDate = date;
        appointment.repairPrice = price;
        appointment.estimatedRepairTime = repairTime;
        appointment.status = status;
        appointment.vehicle = vehicle;
        appointment.user = user;
        return appointment;
    }

    // Getters
    public Long getId() { return id; }
    public LocalDate getAppointmentDate() { return appointmentDate; }
    public double getRepairPrice() { return repairPrice; }
    public Vehicle getVehicle() { return vehicle; }

    public String getStatus() {
        return status;
    }

    public void setId(long l) {
        this.id = l;
    }
}