package com.example.bmw_dalership_spring.model;

import jakarta.persistence.*;

@Entity
public class CarPart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String partName;
    private double price;
    private int estimatedRepairTime; // In hours

    public CarPart(String partName, double price, int estimatedRepairTime) {
        this.partName = partName;
        this.price = price;
        this.estimatedRepairTime = estimatedRepairTime;
    }

    public CarPart() {}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarPart part = (CarPart) o;
        return id != null && id.equals(part.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }


    // Getters
    public Long getId() { return id; }
    public String getPartName() { return partName; }
    public double getPrice() { return price; }
    public int getEstimatedRepairTime() { return estimatedRepairTime; }
}
