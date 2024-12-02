package com.example.planetzeapplication;

public class ConsumptionCalculation {

    // Calculates emissions of buying new clothes intersected with second-hand products which reduces consumption emissions
    public static double q18_19Calculation(String newClothesFreq, String secondHandFreq) {

        double newClothes = 0.0;
        double secondHand = 0.0;

        if ("Monthly".equals(newClothesFreq))
            newClothes = 360.0;
        else if ("Quarterly".equals(newClothesFreq))
            newClothes = 120.0;
        else if ("Annually".equals(newClothesFreq))
            newClothes = 100.0;
        else if ("Rarely".equals(newClothesFreq))
            newClothes = 5.0;

        // reduction percentage is in decimal form, i.e. 30% is 0.30
        if ("Yes, regularly".equals(secondHandFreq))
            secondHand = 0.5;
        else if ("Yes, occasionally".equals(secondHandFreq))
            secondHand = 0.3;
        else if ("No".equals(secondHandFreq))
            secondHand = 0.0;

        /*
        the formula is the emissions of the new clothes times the reduction of buying second hand
        second hand reduction percentage is calculated by the percentage of reduction in decimal
        form minus one to get the remaining percentage left
        */
        return newClothes * (1.0 - secondHand);
    }

    // Returns the device emissions produced based on how many devices the user purchased in the past year
    // For 3 or more, I took the average of 3 devices and 4 or more devices
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
    /*
    Returns the reduction amount of clothing emissions (after second-hand reduction) in a negative
    value taking account for how often they recycle.
    For Quarterly and Occasionally recycling I chose a reduction around the in between point of
    monthly and annually for Occasionally recycling. For quarterly buyers that recycle I
    noticed that from frequently to occasionally, the differance is equal to the occasional
    reduction, so frequently reduction is equal to occasionally reductions times 2. And then
    for Quarterly and always recycling I chose a reduction who's difference from always to
    Frequently was in ratio with the other buyFreq differences.
     */
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
    /*
    Returns the reduction amount of device emissions in a negative value
    taking account for how often they recycle
    For 3 or more, I took the average of 3 devices and 4 or more devices in the specific category
    */

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

    /*
    calls all the methods to calculate each question and then returns the sum of all the returned
    values in kg CO2e (emissions and the necessary general reductions)
    */
    public static double consumptionEmissionsKG(String newClothes, String secondClothes,
                                                String numOfNewDevices, String recycleFreq) {

        double clothingEmissions = q18_19Calculation(newClothes, secondClothes);
        double deviceEmissions = q20Calculation(numOfNewDevices);
        double buyerReduction = clothingAndRecycle(newClothes, recycleFreq);
        double deviceReduction = deviceAndRecycle(numOfNewDevices, recycleFreq);

        // sums as reduction are returned as negative values
        return clothingEmissions + deviceEmissions + buyerReduction + deviceReduction;
    }
}
