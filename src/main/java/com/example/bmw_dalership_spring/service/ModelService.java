package com.example.bmw_dalership_spring.service;

import com.example.bmw_dalership_spring.model.Make;
import com.example.bmw_dalership_spring.model.Model;
import com.example.bmw_dalership_spring.repository.ModelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ModelService {
    private final ModelRepository modelRepository;
    @Autowired
    public ModelService(ModelRepository modelRepository) {
        this.modelRepository = modelRepository;
    }
    public List<Model> getModelsByMake(Make make){
        return modelRepository.findModelsByMake(make);
    }
    public void saveModel(Model model){
        modelRepository.save(model);
    }
    public Model getModelByName(String modelName){
        return modelRepository.findModelByModel(modelName);
    }

}
