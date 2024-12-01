package com.example.planetzeapplication;

public class FoodCalculation {

    public static int q8Calculation(String selection) {
        if ("Vegetarian".equals(selection)) {
            return 1000;
        } else if ("Vegan".equals(selection)) {
            return 500;
        } else if ("Pescatarian (fish/seafood)".equals(selection)) {
            return 1500;
        }
        return 0;
    }

    public static int q9BeefCalculation(String beefSelection) {
        if ("Daily".equals(beefSelection)) {
            return 2500;
        } else if ("Frequently (3-5 times/week)".equals(beefSelection)) {
            return 1900;
        } else if ("Occasionally (1-2 times/week)".equals(beefSelection)) {
            return 1300;
        } else if ("Never".equals(beefSelection)) {
            return 0;
        }
        return 0;
    }
    public static int q9PorkCalculation(String PorkSelection) {
        if ("Daily".equals(PorkSelection)) {
            return 1450;
        } else if ("Frequently (3-5 times/week)".equals(PorkSelection)) {
            return 860;
        } else if ("Occasionally (1-2 times/week)".equals(PorkSelection)) {
            return 450;
        } else if ("Never".equals(PorkSelection)) {
            return 0;
        }
        return 0;
    }

    public static int q9ChickenCalculation(String ChickenSelection) {
        if ("Daily".equals(ChickenSelection)) {
            return 950;
        } else if ("Frequently (3-5 times/week)".equals(ChickenSelection)) {
            return 600;
        } else if ("Occasionally (1-2 times/week)".equals(ChickenSelection)) {
            return 200;
        } else if ("Never".equals(ChickenSelection)) {
            return 0;
        }
        return 0;
    }

    public static int q9FishCalculation(String FishSelection) {
        if ("Daily".equals(FishSelection)) {
            return 800;
        } else if ("Frequently (3-5 times/week)".equals(FishSelection)) {
            return 500;
        } else if ("Occasionally (1-2 times/week)".equals(FishSelection)) {
            return 150;
        } else if ("Never".equals(FishSelection)) {
            return 0;
        }
        return 0;
    }

    public static double q10Calculation(String selection) {
        if ("Never".equals(selection)) {
            return 0.0;
        } else if ("Rarely".equals(selection)) {
            return 23.4;
        } else if ("Occasionally".equals(selection)) {
            return 70.2;
        } else if ("Frequently".equals(selection)) {
            return 140.4;
        }
        return 0;
    }

    public static double foodEmissionsKG(String dietType, String beef, String pork,
                                              String chicken, String fish, String leftovers) {

        double dietEmissions = q8Calculation(dietType);
        double beefEmissions = q9BeefCalculation(beef);
        double porkEmissions = q9PorkCalculation(pork);
        double chickenEmissions = q9ChickenCalculation(chicken);
        double fishEmissions = q9FishCalculation(fish);
        double leftoversEmissions = q10Calculation(leftovers);

        return dietEmissions + beefEmissions + porkEmissions + chickenEmissions + fishEmissions + leftoversEmissions;
    }
}
