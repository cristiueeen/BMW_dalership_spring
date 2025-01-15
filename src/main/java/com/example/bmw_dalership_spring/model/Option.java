package com.example.bmw_dalership_spring.model;

import jakarta.persistence.*;

@Entity
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private double priceContribution;

    public Option(String name, double priceContribution) {
        this.name = name;
        this.priceContribution = priceContribution;
    }

    public Option() {
    }

    // Getters
    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPriceContribution() {
        return priceContribution;
    }

    public void setPriceContribution(double v) {
        this.priceContribution = v;
    }

    public void setName(String optionName) {
        this.name = optionName;
    }
}