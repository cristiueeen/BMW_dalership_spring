package com.example.bmw_dalership_spring.service;

import com.example.bmw_dalership_spring.model.Make;
import com.example.bmw_dalership_spring.repository.MakeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MakeService {
    private final MakeRepository makeRepository;
    @Autowired
    public MakeService(MakeRepository makeRepository) {
        this.makeRepository = makeRepository;
    }
    public List<Make> getAllMakes(){
       return makeRepository.findAll();
    }
    public Make getMakeByName(String name){
        return makeRepository.findByMake(name);
    }

    public void saveMake(Make make) {
        makeRepository.save(make);
    }
}
