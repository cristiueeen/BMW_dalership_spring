package com.example.bmw_dalership_spring.model;
import jakarta.persistence.*;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "vehicles")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String vin;
    private String make;
    private String model;
    private String color;
    private String engine;
    private String fuel;
    private int power;
    private int torque;
    private int year;
    private int mileage;
    private double price;

    @Lob
    private byte[] image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User owner;

    @ManyToMany
    @JoinTable(
            name = "vehicle_option",
            joinColumns = @JoinColumn(name = "vehicle_id"),
            inverseJoinColumns = @JoinColumn(name = "option_id")
    )
    private Set<Option> options = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "vehicle_part",
            joinColumns = @JoinColumn(name = "vehicle_id"),
            inverseJoinColumns = @JoinColumn(name = "part_id")
    )
    private List<CarPart> compatibleParts;

    public Vehicle (String vin, String make, String model, int mileage,int price, User owner, byte[] image, String color, String engine, String fuel, int power, int torque, int year, Set<Option> options,List<CarPart> compatibleParts) {
        this.vin = vin;
        this.make = make;
        this.model = model;
        this.mileage = mileage;
        this.price = price;
        this.engine = engine;
        this.fuel = fuel;
        this.power = power;
        this.torque = torque;
        this.year = year;
        this.owner = owner;
        this.image = image;
        this.color = color;
        this.options = options;
        this.compatibleParts = compatibleParts;
    }

    public Vehicle() {
    }

    public Vehicle(String vin, String make, String model, int mileage, User owner, byte[] image, String color, String engine, String fuel, int power, int torque, int year, Set<Option> options) {
        this(vin, make, model, mileage, 0, owner, image, color, engine, fuel, power, torque, year, options, new ArrayList<CarPart>());
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vehicle vehicle = (Vehicle) o;
        return id != null && id.equals(vehicle.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


    // Getters
    public Long getId() { return id; }
    public Set<Option> getOptions() { return options; }
    public List<CarPart> getCompatibleParts() { return compatibleParts; }
    public String getVin() { return vin; }
    public String getMake() { return make; }
    public String getModel() { return model; }
    public int getMileage() { return mileage; }
    public double getPrice() { return price; }
    public byte[] getImage() { return image; }
    public User getOwner() { return owner; }
    public String getColor() { return color; }
    public String getEngine() { return engine; }
    public String getFuel() { return fuel; }
    public int getPower() { return power; }
    public int getYear() { return year; }
    public int getTorque() { return torque; }
    public void setPrice(double price) { this.price = price; }

    public void setImage(byte[] imageBytes) {
        this.image = imageBytes;
    }

    public void setMake(String make) {
        this.make = make;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public void setMileage(int mileage) {
        this.mileage = mileage;
    }
    public void setColor(String color) {
        this.color = color;
    }
    public void setEngine(String engine) {
        this.engine = engine;
    }
    public void setFuel(String fuel) {
        this.fuel = fuel;
    }
    public void setPower(int power) {
        this.power = power;
    }
    public void setYear(int year) {
        this.year = year;
    }
    public void setTorque(int torque) {
        this.torque = torque;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public void setFuelType(String fuelType) {
        this.fuel = fuelType;
    }
    public void setOptions(Set<Option> options) {
        this.options.addAll(options);
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public void setId(long l) {
        this.id = l;
    }
}
