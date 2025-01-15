package com.example.bmw_dalership_spring.service;

import com.example.bmw_dalership_spring.model.Option;
import com.example.bmw_dalership_spring.repository.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OptionService {
    @Autowired
    private OptionRepository optionRepository;
    @Autowired
    public OptionService(OptionRepository optionRepository) {
        this.optionRepository = optionRepository;
    }

    public Set<Option> getOptionList(List<String> optionStrings) {
        Set<Option> optionList = new HashSet<>();
        System.out.println("these are the options found in the db from the selected ones");
        for (String optionString : optionStrings) {
            Option foundOption = optionRepository.findByName(optionString);
            if (foundOption != null) {
                optionList.add(foundOption);
            }
        }
        return optionList;
    }
    public List<String> getAllOptionsString(){
        List<String> optionStrings = new ArrayList<>();
        for(Option option : optionRepository.findAll()){
            optionStrings.add(option.getName());
        }
        return optionStrings;
    }
}
