package com.example.bmw_dalership_spring.service;

import com.example.bmw_dalership_spring.model.CarPart;
import com.example.bmw_dalership_spring.repository.CarPartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarPartService{
    private final CarPartRepository carPartRepository;

    @Autowired
    public CarPartService(CarPartRepository carPartRepository){
        this.carPartRepository = carPartRepository;
    }

    public List<CarPart> getAllParts() {
        return carPartRepository.findAll();
    }

    public CarPart getPartByName(String partName) {
        return carPartRepository.findCarPartByPartName(partName);
    }
    public void savePart(CarPart carPart) {
        carPartRepository.save(carPart);
    }
    private void addCarParts() {

    }

    }
