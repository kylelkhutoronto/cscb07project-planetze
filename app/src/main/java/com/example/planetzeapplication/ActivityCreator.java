package com.example.planetzeapplication;

public class ActivityCreator {

    public static Activity newActivity(String activityName, String category, String subtype, double magnitude) {
        switch (category.toLowerCase()) {
            case "transportation": return new TransportationActivity(activityName, category, subtype, magnitude);
            case "food consumption": return new FoodConsumptionActivity(activityName, category, subtype, magnitude);
            case "shopping consumption": return new ShoppingConsumptionActivity(activityName, category, subtype, magnitude);
            case "bill": return new BillActivity(activityName, category, subtype, magnitude);
            default: throw new IllegalArgumentException("Unknown category");
        }
    }
}
