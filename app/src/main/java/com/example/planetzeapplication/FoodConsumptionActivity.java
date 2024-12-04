package com.example.planetzeapplication;

public class FoodConsumptionActivity extends Activity {

    public FoodConsumptionActivity(String activityName, String category, String subtype, double magnitude) {
        super(activityName, category, subtype, magnitude);
    }

    @Override
    public long assignId() {
        switch (subtype.toLowerCase()) {
            case "beef": return 2001;
            case "pork": return 2002;
            case "chicken": return 2003;
            case "fish": return 2004;
            case "plant-based": return 2005;
            default: throw new IllegalArgumentException("Unknown meal subtype");
        }
    }

    @Override
    public double computeBaseEmission() {
        switch (subtype.toLowerCase()) {
            case "beef": return 27.0;
            case "pork": return 12.1;
            case "chicken": return 6.9;
            case "fish": return 6.1;
            case "plant-based": return 2.0;
            default: throw new IllegalArgumentException("Unknown meal subtype");
        }
    }

    @Override
    public double computeSessionEmission() {
        switch (subtype.toLowerCase()) {
            case "beef":
            case "pork":
            case "chicken":
            case "fish":
            case "plant-based":
                return (getBaseEmission() * magnitude);
            default: throw new IllegalArgumentException("Unknown meal subtype");
        }
    }
}
