package com.example.planetzeapplication;

public class BillActivity extends Activity {

    public BillActivity(String activityName, String category, String subtype, double magnitude) {
        super(activityName, category, subtype, magnitude);
    }

    @Override
    public long assignId() {
        switch (subtype.toLowerCase()) {
            case "electricity": return 4001;
            case "gas": return 4002;
            case "water": return 4003;
            default: throw new IllegalArgumentException("Unknown bill subtype");
        }
    }

    @Override
    public double computeBaseEmission() {
        String sub = subtype.toLowerCase();
        if (sub.equals("electricity")) {
            if (magnitude < 50) {
                return 2571.37;
            }
            else if (magnitude >= 50 && magnitude <= 100) {
                return 3579.98;
            }
            else if (magnitude > 100 && magnitude <= 150) {
                return 4383.04;
            }
            else if (magnitude > 150 && magnitude <= 200) {
                return 5014.95;
            }
            else {
                return 6078.38;
            }
        }
        else if (sub.equals("gas")) {
            if (magnitude < 50) {
                return 1500.0;
            }
            else if (magnitude >= 50 && magnitude < 100) {
                return 2500.0;
            }
            else if (magnitude >= 100 && magnitude < 150) {
                return 3500.0;
            }
            else if (magnitude >= 150 && magnitude < 200) {
                return 4500.0;
            }
            else {
                return 5500.0;
            }

        }
        else if (sub.equals("water")) {
            if (magnitude < 50) {
                return 1000.0;
            }
            else if (magnitude >= 50 && magnitude <= 100) {
                return 2000.0;
            }
            else if (magnitude > 100 & magnitude <= 150) {
                return 3000.0;
            }
            else {
                return 4000.0;
            }
        }
        else {
            throw new IllegalArgumentException("Unknown bill subtype");
        }
    }

    @Override
    public double computeSessionEmission() {
        return computeBaseEmission();
    }
}
