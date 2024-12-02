package com.example.planetzeapplication;

public class TransportationCalculation {
    public static double q2Calculation(String selection) {
        if ("Gasoline".equals(selection)) {
            return 0.24;
        } else if ("Diesel".equals(selection)) {
            return 0.27;
        } else if ("Hybrid".equals(selection)) {
            return 0.16;
        } else if ("Electric".equals(selection)) {
            return 0.05;
        } else if ("I don't know".equals(selection)) {
            return 0;
        }
        return 0;
    }

    public static int q3Calculation(String selection) {
        if ("Up to 5,000 km".equals(selection)) {
            return 5000;
        } else if ("5,000-10,000 km".equals(selection)) {
            return 10000;
        } else if ("10,000-15,000 km".equals(selection)) {
            return 15000;
        } else if ("15,000-20,000 km".equals(selection)) {
            return 20000;
        } else if ("20,000-25,000 km".equals(selection)) {
            return 25000;
        } else if ("More than 25,000 km".equals(selection)) {
            return 35000;
        }
        return 0;
    }

    public static int q4_5Calculation(String selection4, String selection5) {

        boolean under1 = "Under 1 hour".equals(selection5);
        boolean oneToThree = "1-3 hours".equals(selection5);
        boolean threeToFive = "3-5 hours".equals(selection5);
        boolean fiveToTen = "5-10 hours".equals(selection5);
        boolean tenPlus = "More than 10 hours".equals(selection5);

        if ("Never".equals(selection4)) {
            return 0;
        } else if ("Occasionally (1-2 times/week)".equals(selection4)) {
            if (under1) {
                return 246;
            } else if (oneToThree) {
                return 819;
            } else if (threeToFive) {
                return 1638;
            } else if (fiveToTen) {
                return 3071;
            } else if (tenPlus) {
                return 4095;
            }
        } else if ("Frequently (3-4 times/week)".equals(selection4)) {
            if (under1) {
                return 573;
            } else if (oneToThree) {
                return 1911;
            } else if (threeToFive) {
                return 3822;
            } else if (fiveToTen) {
                return 7166;
            } else if (tenPlus) {
                return 9555;
            }
        } else if ("Always (5+ times/week)".equals(selection4)) {
            if (under1) {
                return 573;
            } else if (oneToThree) {
                return 1911;
            } else if (threeToFive) {
                return 3822;
            } else if (fiveToTen) {
                return 7166;
            } else if (tenPlus) {
                return 9555;
            }
        }
        return 0;
    }

    public static int q6Calculation(String selection) {
        if ("None".equals(selection)) {
            return 0;
        } else if ("1-2 flights".equals(selection)) {
            return 225;
        } else if ("3-5 flights".equals(selection)) {
            return 600;
        } else if ("6-10 flights".equals(selection)) {
            return 1200;
        } else if ("More than 10 flights".equals(selection)) {
            return 1800;
        }
        return 0;
    }

    public static int q7Calculation(String selection) {
        if ("None".equals(selection)) {
            return 0;
        } else if ("1-2 flights".equals(selection)) {
            return 825;
        } else if ("3-5 flights".equals(selection)) {
            return 2200;
        } else if ("6-10 flights".equals(selection)) {
            return 4400;
        } else if ("More than 10 flights".equals(selection)) {
            return 6600;
        }
        return 0;
    }

    public static double transportEmissionsKG (String carEnergy, String carDistance, String freqPT,
                                               String timePT,String shortFlights, String longFlights) {

        double carEnergySource = q2Calculation(carEnergy);
        double carDistanceEmissions = q3Calculation(carDistance);
        double transitEmissions = q4_5Calculation(freqPT, timePT);
        double shortEmissions = q6Calculation(shortFlights);
        double longEmissions = q7Calculation(longFlights);

        return (carEnergySource * carDistanceEmissions) + transitEmissions + shortEmissions + longEmissions;
    }
}
