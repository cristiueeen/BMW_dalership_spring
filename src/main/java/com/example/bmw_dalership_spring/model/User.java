package com.example.bmw_dalership_spring.model;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String role;

    @Column(nullable = false)
    @NotBlank(message = "Email is required.")
    @Email(message = "Invalid email format.")
    private String email;

    @Column(nullable = false)
    @NotNull
    @Size(min = 5)
    private String password;

    @Column(nullable = false)
    @NotBlank
    private String passwordResetToken;

    @OneToMany(mappedBy = "owner", cascade = CascadeType.ALL)
    private List<Vehicle> vehicles;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ServiceAppointment> appointments;

    public User (String firstName, String lastName, String email, String password, String role, String passwordResetToken, List<Vehicle> vehicles, List<ServiceAppointment> appointments) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.passwordResetToken = passwordResetToken;
        this.vehicles = vehicles;
        this.vehicles = vehicles != null ? vehicles : new ArrayList<>();
        this.appointments = appointments != null ? appointments : new ArrayList<>();
    }
    public User(String firstName, String lastName, String email, String password, String role, String passwordResetToken) {
        this(firstName, lastName, email, password, role, passwordResetToken, new ArrayList<Vehicle>(), new ArrayList<ServiceAppointment>());
    }
    public User() {}

    public Long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public List<Vehicle> getVehicles() { return vehicles; }
    public List<ServiceAppointment> getAppointments() { return appointments; }
    public String getRole() { return role; }
    public String getPassword() { return password; }
    public String getPasswordResetToken() { return passwordResetToken; }
    public String getEmail() { return email; }

    public void setPassword(String password) { this.password = password; };

    public void setEmail(String email) {
        this.email = email;
    }

}
