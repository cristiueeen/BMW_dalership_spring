package com.example.bmw_dalership_spring.model;
import jakarta.persistence.*;
@Entity
@Table(name="models")
public class Model {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String model;
    private double referencePrice;

    @ManyToOne
    @JoinColumn(name = "make_id")
    private Make make;


    public Model(String model, double referecePrice) {
        this.model = model;
        this.referencePrice = referecePrice;
    }

    public Model() {}

    @Override
    public String toString() {
        return this.model;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getModel() {
        return model;
    }
    public void setModel(String model) {
        this.model = model;
    }
    public double getReferencePrice() {
        return referencePrice;
    }
    public void setReferencePrice(double referencePrice) {
        this.referencePrice = referencePrice;
    }
    public Make getMake() {
        return make;
    }

    public void setMake(Make make) {
        this.make = make;
    }

}
