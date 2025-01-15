package com.example.bmw_dalership_spring.model;
import jakarta.persistence.*;

@Entity
public class Marketplace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String condition;
    private String location;
    private String description;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;



    public Marketplace (String condition, String location, Vehicle vehicle, User owner, String description )
    {
        this.condition = condition;
        this.location = location;
        this.vehicle = vehicle;
        this.owner = owner;
        this.description = description;
    }
    public Marketplace() {}

    // Getters
    public Long getId() { return id; }
    public String getCondition() { return condition; }
    public Vehicle getVehicle() { return vehicle; }
    public User getOwner() { return owner; }
    public String getLocation() { return location; }
    public String getDescription() { return description; }
    public void setCondition(String condition) { this.condition = condition; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
    public void setOwner(User owner) { this.owner = owner; }
    public void setLocation(String location) { this.location = location; }
    public void setDescription(String description) { this.description = description; }


    public void setUser(User user) {
        this.owner = user;
    }

    public void setId(long l) {
        this.id = l;
    }
}
