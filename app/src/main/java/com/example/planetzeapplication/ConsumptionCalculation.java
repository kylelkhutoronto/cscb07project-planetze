package com.example.planetzeapplication;

public class ConsumptionCalculation {

    public static double q18_19Calculation(String selection18, String selection19) {

        double newClothes = 0.0;
        double secondHand = 0.0;

        if ("Monthly".equals(selection18))
            newClothes = 360.0;
        else if ("Quarterly".equals(selection18))
            newClothes = 120.0;
        else if ("Annually".equals(selection18))
            newClothes = 100.0;
        else if ("Rarely".equals(selection18))
            newClothes = 5.0;

        if ("Yes, regularly".equals(selection19))
            secondHand = 0.5;
        else if ("Yes, occasionally".equals(selection19))
            secondHand = 0.3;
        else if ("No".equals(selection19))
            secondHand = 0.0;

        return newClothes * (1.0 - secondHand);
    }

    public static int q20Calculation(String selection) {
        if ("None".equals(selection))
            return 0;
        else if ("1".equals(selection))
            return 300;
        else if ("2".equals(selection))
            return 600;
        else if ("3 or more".equals(selection))
            return 1050;
        return 0;
    }

    public static double clothingAndRecycle(String buyFreq, String recycleFreq) {

        if ("Monthly".equals(buyFreq)){
            if ("Occasionally".equals(recycleFreq))
                return -54.0;
            else if ("Frequently".equals(recycleFreq))
                return -108.0;
            else if ("Always".equals(recycleFreq))
                return -180.0;
        } else if ("Quarterly".equals(buyFreq)) {
            if ("Occasionally".equals(recycleFreq))
                return -36.0;
            else if ("Frequently".equals(recycleFreq))
                return -72.0;
            else if ("Always".equals(recycleFreq))
                return -118.0;
        } else if ("Annually".equals(buyFreq)) {
            if ("Occasionally".equals(recycleFreq))
                return -15.0;
            else if ("Frequently".equals(recycleFreq))
                return -30.0;
            else if ("Always".equals(recycleFreq))
                return -50.0;
        } else if ("Rarely".equals(buyFreq)) {
            if ("Occasionally".equals(recycleFreq))
                return -0.75;
            else if ("Frequently".equals(recycleFreq))
                return -1.5;
            else if ("Always".equals(recycleFreq))
                return -2.5;
        }
        return -0.0;
    }

    public static double deviceAndRecycle(String deviceFreq, String recycleFreq) {

        if ("None".equals(deviceFreq)){
            if ("Occasionally".equals(recycleFreq))
                return -0.0;
            else if ("Frequently".equals(recycleFreq))
                return -0.0;
            else if ("Always".equals(recycleFreq))
                return -0.0;
        } else if ("1".equals(deviceFreq)) {
            if ("Occasionally".equals(recycleFreq))
                return -45.0;
            else if ("Frequently".equals(recycleFreq))
                return -60.0;
            else if ("Always".equals(recycleFreq))
                return -90.0;
        } else if ("2".equals(deviceFreq)) {
            if ("Occasionally".equals(recycleFreq))
                return -60.0;
            else if ("Frequently".equals(recycleFreq))
                return -120.0;
            else if ("Always".equals(recycleFreq))
                return -180.0;
        } else if ("3 or more".equals(deviceFreq)) {
            if ("Occasionally".equals(recycleFreq))
                return -105.0;
            else if ("Frequently".equals(recycleFreq))
                return -210.0;
            else if ("Always".equals(recycleFreq))
                return -315.0;
        }
        return -0.0;
    }

    public static double consumptionEmissionsKG(String newClothes, String secondClothes,
                                                String numOfNewDevices, String recycleFreq) {

        double clothingEmissions = q18_19Calculation(newClothes, secondClothes);
        double deviceEmissions = q20Calculation(numOfNewDevices);
        double buyerReduction = clothingAndRecycle(newClothes, recycleFreq);
        double deviceReduction = deviceAndRecycle(numOfNewDevices, recycleFreq);

        return clothingEmissions + deviceEmissions + buyerReduction + deviceReduction;
    }
}
