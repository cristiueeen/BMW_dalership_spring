package com.example.bmw_dalership_spring.configuration;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class VehicleModelPriceReference {

    public static final Map<String, Integer> MODEL_REFERENCE_PRICES;

    static {
        Map<String, Integer> modelPrices = new HashMap<>();

        // 3 Series (from 320d/i)
        modelPrices.put("320i", 47000);
        modelPrices.put("320d", 51000);
        modelPrices.put("320xi", 49000);
        modelPrices.put("320xd", 53000);
        modelPrices.put("323i", 52000);
        modelPrices.put("323xi", 53000);
        modelPrices.put("328i", 53000);
        modelPrices.put("328xi", 54000);
        modelPrices.put("330i", 56000);
        modelPrices.put("330d", 60000);
        modelPrices.put("330xi", 58000);
        modelPrices.put("330xd", 63000);
        modelPrices.put("335i", 60000);
        modelPrices.put("335d", 61000);
        modelPrices.put("335xi", 61000);
        modelPrices.put("335xd", 62000);

        // 5 Series (all can be xDrive)
        modelPrices.put("520i", 58000);
        modelPrices.put("520d", 60000);
        modelPrices.put("520xi", 61000);
        modelPrices.put("520xd", 63000);
        modelPrices.put("525i", 62000);
        modelPrices.put("525xi", 63000);
        modelPrices.put("530i", 67000);
        modelPrices.put("530d", 69000);
        modelPrices.put("530xi", 72000);
        modelPrices.put("530xd", 75000);
        modelPrices.put("540i", 74000);
        modelPrices.put("540xi", 78000);
        modelPrices.put("550i", 85000);
        modelPrices.put("550xi", 90000);

        // 7 Series (all can be xDrive)
        modelPrices.put("730i", 100000);
        modelPrices.put("730d", 105000);
        modelPrices.put("730xi", 110000);
        modelPrices.put("730xd", 115000);
        modelPrices.put("740i", 120000);
        modelPrices.put("740d", 125000);
        modelPrices.put("740xi", 130000);
        modelPrices.put("740xd", 135000);
        modelPrices.put("750i", 140000);
        modelPrices.put("750xi", 145000);

        // BMW X Series (simplified as "X1", "X3", etc.)
        modelPrices.put("X1", 55000);
        modelPrices.put("X3", 65000);
        modelPrices.put("X4", 70000);
        modelPrices.put("X5", 80000);
        modelPrices.put("X6", 90000);
        modelPrices.put("X7", 95000);

        modelPrices.put("M2", 70000);
        modelPrices.put("M3", 80000);
        modelPrices.put("M4", 90000);
        modelPrices.put("M5", 100000);
        modelPrices.put("M6", 105000);
        modelPrices.put("M8", 110000);

        modelPrices.put("iX", 90000);
        modelPrices.put("i4", 65000);

        // Rolls-Royce Models
        modelPrices.put("Ghost", 320000);
        modelPrices.put("Wraith", 350000);
        modelPrices.put("Dawn", 370000);
        modelPrices.put("Phantom", 450000);
        modelPrices.put("Cullinan", 400000);

        // Mini Models
        modelPrices.put("Cooper", 30000);
        modelPrices.put("Cooper S", 35000);
        modelPrices.put("John Cooper Works", 45000);
        modelPrices.put("Countryman", 40000);
        modelPrices.put("Clubman", 37000);
        modelPrices.put("Electric", 32000);

        MODEL_REFERENCE_PRICES = Collections.unmodifiableMap(modelPrices);
    }

    public static void printAllPrices() {
        MODEL_REFERENCE_PRICES.forEach((model, price) ->
                System.out.printf("Model: %-20s | Price: %d €%n", model, price)
        );
    }
}
