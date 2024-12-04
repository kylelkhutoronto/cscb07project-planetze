package com.example.planetzeapplication;

public class ShoppingConsumptionActivity extends Activity {

    public ShoppingConsumptionActivity(String activityName, String category, String subtype, double magnitude) {
        super(activityName, category, subtype, magnitude);
    }

    @Override
    public long assignId() {
        switch (subtype.toLowerCase()) {
            case "clothes": return 3001;
            case "smartphone": return 3101;
            case "laptop": return 3102;
            case "tv": return 3103;
            case "furniture": return 3201;
            case "other appliance": return 3202;
            default: throw new IllegalArgumentException("Unknown item subtype");
        }
    }

    @Override
    public double computeBaseEmission() {
        switch (subtype.toLowerCase()) {
            case "clothes": return 41.25;
            case "smartphone":
            case "laptop":
            case "tv":
                return 300;
            case "furniture": return 500;
            case "other appliance": return 600;
            default: throw new IllegalArgumentException("Unknown item subtype");
        }
    }

    @Override
    public double computeSessionEmission() {
        switch (subtype.toLowerCase()) {
            case "clothes":
            case "smartphone":
            case "laptop":
            case "tv":
            case "furniture":
            case "other appliance":
                return (getBaseEmission() * magnitude);
            default: throw new IllegalArgumentException("Unknown item subtype");
        }
    }
}
