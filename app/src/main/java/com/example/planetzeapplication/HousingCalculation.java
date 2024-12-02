package com.example.planetzeapplication;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class HousingCalculation {
    /*
    Function reads the .JSON file in the raw file in which has all the housing data
    So the user's answers for housing is saved aas the key name in the .JSON file
    So then the user's answers are used to traverse through the file
    */
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
            Log.e("Housing Calculations", "The data from the .JSON file could not be retrieved.");

            // average of all the emissions data returned if the specific data point can't be reached/retrieved
            return 4326;
        }
    }

    /*
    Returns reduction or addition to emissions
    If the sources of heating water was different from home add 233 kg to calculations
    If the source of heating for the house and water is the same, the calculations will remain unchanged.
    However, if they are different and the source of water heating is electricity or solar,
    the value of 233 will be reduced. Otherwise, it will be added.
     */
    public static int sourceCalculation(String heatingEnergy, String waterHeatingEnergy) {
        if (!(heatingEnergy.equals(waterHeatingEnergy))
                && (waterHeatingEnergy.equals("electricity") || waterHeatingEnergy.equals("solar")))
            return -233;
        else if (!(heatingEnergy.equals(waterHeatingEnergy)))
            return 233;
        return 0;
    }
    /*
    returns emissions reduction based on if user uses renewable energy sources for electricity or
    heating and how much it makes up of their overall energy usage
     */
    public static int q17Calculation(String selection) {
        if ("Yes, primarily (more than 50% of energy use)".equals(selection))
            return -6000;
        else if ("Yes, partially (less than 50% of energy use)".equals(selection))
            return -4000;
        return 0;
    }
    /*
    Calls all housing calculation methods and returns the sum in kg CO2e (so total housing emissions)
    The final housing emission total can be negative
    */
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

