package com.example.planetzeapplication;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HousingCalculation {

    public static int housingJSONReader(Context context, String houseType, String sqftRange,
                                        String billingAmount, String numOfOccupants,
                                        String energySource) {
        try {
            InputStream is = context.getResources().openRawResource(R.raw.housingdata);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String jsonData = new String(buffer);

            JSONObject jsonObject = new JSONObject(jsonData);

            JSONObject houses = jsonObject.getJSONObject("houses");
            JSONObject houseCategory = houses.getJSONObject(houseType);
            JSONObject sqftCategory = houseCategory.getJSONObject(sqftRange);
            JSONObject billAmountCategory = sqftCategory.getJSONObject(billingAmount);
            JSONObject occupantsCategory = billAmountCategory.getJSONObject(numOfOccupants);

            return occupantsCategory.getInt(energySource);

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return -1; // Return -1 if there's an error
        }
    }

    public static int sourceCalculation(String heatingEnergy, String waterHeatingEnergy) {
        if (!(heatingEnergy.equals(waterHeatingEnergy))
                && (waterHeatingEnergy.equals("Electricity") || waterHeatingEnergy.equals("Solar")))
            return -233;
        else if (!(heatingEnergy.equals(waterHeatingEnergy)))
            return 233;
        return 0;
    }

    public static int q17Calculation(String selection) {
        if ("Yes, primarily (more than 50% of energy use)".equals(selection))
            return -6000;
        else if ("Yes, partially (less than 50% of energy use)".equals(selection))
            return -4000;
        return 0;
    }

    public static double housingEmissionsKG (Context context, String houseType, String sqftRange,
                                             String billingAmount, String numOfOccupants,
                                             String heatSource, String waterSource,
                                             String renewable) {

        double houseEmissions = housingJSONReader(context, houseType, sqftRange, billingAmount,
                numOfOccupants, heatSource);
        double sourceReductions = sourceCalculation(heatSource, waterSource);
        double renewableReductions = q17Calculation(renewable);

        return houseEmissions + sourceReductions + renewableReductions;
    }


}

