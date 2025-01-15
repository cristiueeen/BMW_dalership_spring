package com.example.bmw_dalership_spring.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Make {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String make;

    @OneToMany(mappedBy = "make", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Model> models = new ArrayList<>();


    public Make(String make) {
        this.make = make;
    }

    @Override
    public String toString() {
        return this.make;
    }

    public Make() {

    }

    public String getMake() {
        return make;
    }

    public Long getId() {
        return id;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public List<Model> getModels() {
        return models;
    }

    public void setModels(List<Model> models) {
        this.models = models;
    }
}
