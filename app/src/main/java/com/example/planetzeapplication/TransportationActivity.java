package com.example.planetzeapplication;

public class TransportationActivity extends Activity {

    public TransportationActivity(String activityName, String category, String subtype, double magnitude) {
        super(activityName, category, subtype, magnitude);
    }

    @Override
    public long assignId() {
        switch (subtype.toLowerCase()) {
            case "gasoline": return 1001;
            case "diesel": return 1002;
            case "hybrid": return 1003;
            case "electric": return 1004;
            case "bus": return 1101;
            case "train": return 1102;
            case "subway": return 1103;
            case "short_haul_flight": return 1201;
            case "long_haul_flight": return 1202;
            case "cycling": return 1301;
            case "walking": return 1302;
            default: throw new IllegalArgumentException("Unknown transportation subtype");
        }
    }

    @Override
    public double computeBaseEmission() {
        switch (subtype.toLowerCase()) {
            case "gasoline": return 0.24;
            case "diesel": return 0.27;
            case "hybrid": return 0.16;
            case "electric": return 0.05;
            case "bus":
            case "subway":
            case "train":
                return 590.4; //obtained from taking the average of every value from this subtype in formula sheet
            case "short_haul_flight": return 112.5;
            case "long_haul_flight": return 412.5;
            case "cycling":
            case "walking":
                return 0.0;
            default: throw new IllegalArgumentException("Unknown transportation subtype");
        }
    }

    @Override
    public double computeSessionEmission() {
        switch (subtype.toLowerCase()) {
            case "gasoline":
            case "diesel":
            case "hybrid":
            case "electric":
            case "bus":
            case "subway":
            case "train":
            case "short_haul_flight":
            case "long_haul_flight":
                return (getBaseEmission() * magnitude);
            case "cycling":
            case "walking":
                return 0.0;
            default: throw new IllegalArgumentException("Unknown transportation subtype");
        }
    }
}
