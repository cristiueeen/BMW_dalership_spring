package com.example.bmw_dalership_spring.service;

import com.example.bmw_dalership_spring.configuration.VehicleModelPriceReference;
import com.example.bmw_dalership_spring.model.Marketplace;
import com.example.bmw_dalership_spring.model.Option;
import com.example.bmw_dalership_spring.model.User;
import com.example.bmw_dalership_spring.model.Vehicle;
import com.example.bmw_dalership_spring.repository.MarketplaceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class MarketplaceService {

    private final MarketplaceRepository marketplaceRepository;
    private final EntityManager entityManager;

    @Autowired
    public MarketplaceService(MarketplaceRepository marketplaceRepository, EntityManager entityManager) {
        this.marketplaceRepository = marketplaceRepository;
        this.entityManager = entityManager;
    }
    public void saveMarketplace(Marketplace marketplace) {
        marketplaceRepository.save(marketplace);
    }
    public Marketplace getMarketplaceById(Long id) {
       return marketplaceRepository.findById(id).orElse(null);
    }
    public Boolean isPresentMarketplace(Marketplace marketplace) {
        return marketplaceRepository.findById(marketplace.getId()).isPresent();
    }

    @Transactional
    public List<Marketplace> getSortedMarketplaces(List<String> make, List<String> model, List<String> fuel,
                                                   Integer fromYear, Integer toYear,
                                                   Integer fromPower, Integer toPower,
                                                   Integer fromPrice, Integer toPrice,
                                                   Integer fromMileage, Integer toMileage) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Marketplace> query = cb.createQuery(Marketplace.class);
        Root<Marketplace> root = query.from(Marketplace.class);

        Join<Object, Object> vehicleJoin = root.join("vehicle");
        Join<Marketplace, User> user = root.join("owner");

        List<Predicate> predicates = new ArrayList<>();

        if (make != null) {
            List<Predicate> makePredicates = new ArrayList<>();
            for (String makeOption : make) {
                makePredicates.add(cb.equal(vehicleJoin.get("make"), makeOption));
            }
            predicates.add(cb.or(makePredicates.toArray(new Predicate[0])));
        }
        if (model != null) {
            List<Predicate> modelPredicates = new ArrayList<>();
            for (String modelOption : model) {
                modelPredicates.add(cb.equal(vehicleJoin.get("model"), modelOption));
            }
            predicates.add(cb.or(modelPredicates.toArray(new Predicate[0])));
        }
        if (fuel != null) {
            List<Predicate> makePredicates = new ArrayList<>();
            for(String fuelOption : fuel) {
                makePredicates.add(cb.equal(vehicleJoin.get("fuel"), fuelOption));
            }
                predicates.add(cb.or(makePredicates.toArray(new Predicate[0])));
        }
        if (fromYear != null && fromYear != Integer.MIN_VALUE) {
            predicates.add(cb.greaterThanOrEqualTo(vehicleJoin.get("year"), fromYear));
        }
        if (toYear != null && toYear != Integer.MAX_VALUE) {
            predicates.add(cb.lessThanOrEqualTo(vehicleJoin.get("year"), toYear));
        }
        if (fromPower != null && fromPower != Integer.MIN_VALUE) {
            predicates.add(cb.greaterThanOrEqualTo(vehicleJoin.get("power"), fromPower));
        }
        if (toPower != null && toPower != Integer.MAX_VALUE) {
            predicates.add(cb.lessThanOrEqualTo(vehicleJoin.get("power"), toPower));
        }
        if (fromPrice != null && fromPrice != Integer.MIN_VALUE) {
            predicates.add(cb.greaterThanOrEqualTo(vehicleJoin.get("price"), fromPrice));
        }
        if (toPrice != null && toPrice != Integer.MAX_VALUE) {
            predicates.add(cb.lessThanOrEqualTo(vehicleJoin.get("price"), toPrice));
        }
        if(fromMileage != null && fromMileage != Integer.MIN_VALUE) {
            predicates.add(cb.greaterThanOrEqualTo(vehicleJoin.get("mileage"), fromMileage));
        }
        if (toMileage != null && toMileage != Integer.MAX_VALUE) {
            predicates.add(cb.lessThanOrEqualTo(vehicleJoin.get("mileage"), toMileage));
        }

        query.where(predicates.toArray(new Predicate[0]));

        return entityManager.createQuery(query).getResultList();
    }
    public List<Marketplace> getAllMarketplaces() {
        return marketplaceRepository.findAll();
    }
    public double getPriceEstimation(Marketplace marketplace) {
        double estimatedPrice = 0.0;
        Vehicle vehicle = marketplace.getVehicle();
        Integer returnedBasePrice = VehicleModelPriceReference.MODEL_REFERENCE_PRICES.get(vehicle.getModel());
        if (returnedBasePrice == null) {
            System.out.println("model was not found");
            return 0.0;
        }
        Double basePrice = Double.valueOf(returnedBasePrice);
        double optionsContribution = 0.0;
        for (Option option : vehicle.getOptions()) {
            double optionPriceContribution = option.getPriceContribution(); // Ranges from 0 to 1
            optionsContribution += optionPriceContribution * 2000; // Each 1.0 corresponds to 3000 €
        }
        basePrice += optionsContribution;



        int currentYear = java.time.Year.now().getValue();
        int age = currentYear - vehicle.getYear();

        double depreciationFactor = 1.0;
        if (age > 20) {
            depreciationFactor = 0.03;
        } else if (age > 15) {
            depreciationFactor = 0.1;
        } else if (age > 12){
            depreciationFactor = 0.175;
        } else if (age > 10) {
            depreciationFactor = 0.25;
        } else if (age > 7) {
            depreciationFactor = 0.4;
        } else if (age > 5) {
            depreciationFactor = 0.55;
        } else if (age > 3) {
            depreciationFactor = 0.7;
        } else if (age > 1) {
            depreciationFactor = 0.9;
        }

        double priceAfterDepreciation = basePrice * depreciationFactor;

        double mileageDeduction = vehicle.getMileage() / 200000.0;
        priceAfterDepreciation *= (1 - Math.min(mileageDeduction, 0.4));
        // Ensure minimum value for very old/damaged vehicles
        estimatedPrice = Math.max(priceAfterDepreciation, basePrice * 0.1);
        return estimatedPrice;

    }
    public Marketplace getMarketplaceByVehicle(Vehicle vehicle) {
        return marketplaceRepository.findByVehicle(vehicle);
    }
    public void deleteMarketplace(Marketplace marketplace) {
        marketplaceRepository.delete(marketplace);
    }
    @Transactional
    public List<Marketplace> getListingsByUser(User user) {
        return marketplaceRepository.findByOwner(user);
    }
}
