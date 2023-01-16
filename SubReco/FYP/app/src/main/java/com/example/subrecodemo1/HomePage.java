package com.example.subrecodemo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.RangeSlider;

import java.util.Arrays;
import java.util.Collections;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class HomePage extends AppCompatActivity {
    private ArrayList<String> uniDetail = new ArrayList<>();
    private ArrayList<String> distanceList = new ArrayList<>();

    Spinner spinner;
    String distanceCode;
    String rentalCode;
    String nightTransportCode;
    String gymCode;
    String bankCode;
    String restaurantCode;
    String url = "https://subreco-ml-app.herokuapp.com/predict";

//    List <Integer> rentRange;
    double lat1 = 0, long1 = 0, lat2 = 0, long2 = 0, distanceDiff = 0;
    String uniCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        // initialise variables
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.action_bar_layout);
        CheckBox Rrent_low = (CheckBox) findViewById(R.id.checkBox_low);
        CheckBox Rrent_medium = (CheckBox)findViewById(R.id.checkBox_medium);
        CheckBox Rrent_high = (CheckBox)findViewById(R.id.checkBox_high);
        CheckBox Ddistance_near = (CheckBox)findViewById(R.id.checkBox_near);
        CheckBox Ddistance_far = (CheckBox)findViewById(R.id.checkBox_far);
        CheckBox NnightTransport_yes = (CheckBox)findViewById(R.id.checkBox_yes);
        CheckBox NnightTransport_no = (CheckBox)findViewById(R.id.checkBox_no);
        CheckBox Ggym_1 = (CheckBox)findViewById(R.id.checkBox);
        CheckBox Ggym_2 = (CheckBox)findViewById(R.id.checkBox2);
        CheckBox Ggym_3 = (CheckBox)findViewById(R.id.checkBox3);
        CheckBox Bbank_1 = (CheckBox)findViewById(R.id.checkBox4);
        CheckBox Bbank_2 = (CheckBox)findViewById(R.id.checkBox5);
        CheckBox Bbank_3 = (CheckBox)findViewById(R.id.checkBox6);
        CheckBox Rrestaurant_1 = (CheckBox)findViewById(R.id.checkBox7);
        CheckBox Rrestaurant_2 = (CheckBox)findViewById(R.id.checkBox8);
        CheckBox Rrestaurant_3 = (CheckBox)findViewById(R.id.checkBox9);


//      set the '$' symbol for slider


        spinner = findViewById(R.id.spinnerUni);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterSpinner = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.uniName, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapterSpinner);


        // disable checkbox
        Rrent_low.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Rrent_medium.setEnabled(false);
                    Rrent_high.setEnabled(false);
                }
                else{
                    Rrent_medium.setEnabled(true);
                    Rrent_high.setEnabled(true);

                }

            }
        });

        Rrent_medium.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Rrent_low.setEnabled(false);
                    Rrent_high.setEnabled(false);
                }
                else{
                    Rrent_low.setEnabled(true);
                    Rrent_high.setEnabled(true);

                }

            }
        });

        Rrent_high.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Rrent_low.setEnabled(false);
                    Rrent_medium.setEnabled(false);
                }
                else{
                    Rrent_low.setEnabled(true);
                    Rrent_medium.setEnabled(true);

                }

            }
        });

        Ddistance_near.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Ddistance_far.setEnabled(false);
                }
                else{
                    Ddistance_far.setEnabled(true);

                }

            }
        });

        Ddistance_far.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Ddistance_near.setEnabled(false);
                }
                else{
                    Ddistance_near.setEnabled(true);

                }

            }
        });


        NnightTransport_yes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    NnightTransport_no.setEnabled(false);
                }
                else{
                    NnightTransport_no.setEnabled(true);

                }

            }
        });

        NnightTransport_no.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    NnightTransport_yes.setEnabled(false);
                }
                else{
                    NnightTransport_yes.setEnabled(true);

                }

            }
        });

        Ggym_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Bbank_1.setEnabled(false);
                    Rrestaurant_1.setEnabled(false);
                    Ggym_2.setEnabled(false);
                    Ggym_3.setEnabled(false);
                }
                else{
                    Bbank_1.setEnabled(true);
                    Rrestaurant_1.setEnabled(true);
                    Ggym_2.setEnabled(true);
                    Ggym_3.setEnabled(true);

                }

            }
        });

        Ggym_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Bbank_2.setEnabled(false);
                    Rrestaurant_2.setEnabled(false);
                    Ggym_1.setEnabled(false);
                    Ggym_3.setEnabled(false);
                }
                else{
                    Bbank_2.setEnabled(true);
                    Rrestaurant_2.setEnabled(true);
                    Ggym_1.setEnabled(true);
                    Ggym_3.setEnabled(true);

                }

            }
        });

        Ggym_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Bbank_3.setEnabled(false);
                    Rrestaurant_3.setEnabled(false);
                    Ggym_1.setEnabled(false);
                    Ggym_2.setEnabled(false);
                }
                else{
                    Bbank_3.setEnabled(true);
                    Rrestaurant_3.setEnabled(true);
                    Ggym_1.setEnabled(true);
                    Ggym_2.setEnabled(true);

                }

            }
        });

        Bbank_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Ggym_1.setEnabled(false);
                    Rrestaurant_1.setEnabled(false);
                    Bbank_2.setEnabled(false);
                    Bbank_3.setEnabled(false);
                }
                else{
                    Ggym_1.setEnabled(true);
                    Rrestaurant_1.setEnabled(true);
                    Bbank_2.setEnabled(true);
                    Bbank_3.setEnabled(true);

                }

            }
        });

        Bbank_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Ggym_2.setEnabled(false);
                    Rrestaurant_2.setEnabled(false);
                    Bbank_1.setEnabled(false);
                    Bbank_3.setEnabled(false);
                }
                else{
                    Ggym_2.setEnabled(true);
                    Rrestaurant_2.setEnabled(true);
                    Bbank_1.setEnabled(true);
                    Bbank_3.setEnabled(true);

                }

            }
        });

        Bbank_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Ggym_3.setEnabled(false);
                    Rrestaurant_3.setEnabled(false);
                    Bbank_2.setEnabled(false);
                    Bbank_1.setEnabled(false);
                }
                else{
                    Ggym_3.setEnabled(true);
                    Rrestaurant_3.setEnabled(true);
                    Bbank_2.setEnabled(true);
                    Bbank_1.setEnabled(true);
                }

            }
        });

        Rrestaurant_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Ggym_1.setEnabled(false);
                    Bbank_1.setEnabled(false);
                    Rrestaurant_2.setEnabled(false);
                    Rrestaurant_3.setEnabled(false);
                }
                else{
                    Ggym_1.setEnabled(true);
                    Bbank_1.setEnabled(true);
                    Rrestaurant_2.setEnabled(true);
                    Rrestaurant_3.setEnabled(true);

                }

            }
        });

        Rrestaurant_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Ggym_2.setEnabled(false);
                    Bbank_2.setEnabled(false);
                    Rrestaurant_1.setEnabled(false);
                    Rrestaurant_3.setEnabled(false);
                }
                else{
                    Ggym_2.setEnabled(true);
                    Bbank_2.setEnabled(true);
                    Rrestaurant_1.setEnabled(true);
                    Rrestaurant_3.setEnabled(true);
                }

            }
        });

        Rrestaurant_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(isChecked){
                    Ggym_3.setEnabled(false);
                    Bbank_3.setEnabled(false);
                    Rrestaurant_2.setEnabled(false);
                    Rrestaurant_1.setEnabled(false);
                }
                else{
                    Ggym_3.setEnabled(true);
                    Bbank_3.setEnabled(true);
                    Rrestaurant_2.setEnabled(true);
                    Rrestaurant_1.setEnabled(true);


                }

            }
        });

    }




    // start next activity when clicking 'SUBMIT' button
    public void nextActivity(View v){
        // initialise variables
        ArrayList<String> arrayList = new ArrayList<>();
        List<String> list = new ArrayList<>();
        List<String> suburbName = new ArrayList<>();
        String [] uniCodes = new String[0];
        String uni = spinner.getSelectedItem().toString();
        String [] uniTokens = new String[0];
        String [] suburbTokens = new String[0];
        List <Integer> rentRange = new ArrayList<>();
        CheckBox rent_low = (CheckBox) findViewById(R.id.checkBox_low);
        CheckBox rent_medium = (CheckBox)findViewById(R.id.checkBox_medium);
        CheckBox rent_high = (CheckBox)findViewById(R.id.checkBox_high);
        CheckBox distance_near = (CheckBox)findViewById(R.id.checkBox_near);
        CheckBox distance_far = (CheckBox)findViewById(R.id.checkBox_far);
        CheckBox nightTransport_yes = (CheckBox)findViewById(R.id.checkBox_yes);
        CheckBox nightTransport_no = (CheckBox)findViewById(R.id.checkBox_no);
        CheckBox gym_1 = (CheckBox)findViewById(R.id.checkBox);
        CheckBox gym_2 = (CheckBox)findViewById(R.id.checkBox2);
        CheckBox gym_3 = (CheckBox)findViewById(R.id.checkBox3);
        CheckBox bank_1 = (CheckBox)findViewById(R.id.checkBox4);
        CheckBox bank_2 = (CheckBox)findViewById(R.id.checkBox5);
        CheckBox bank_3 = (CheckBox)findViewById(R.id.checkBox6);
        CheckBox restaurant_1 = (CheckBox)findViewById(R.id.checkBox7);
        CheckBox restaurant_2 = (CheckBox)findViewById(R.id.checkBox8);
        CheckBox restaurant_3 = (CheckBox)findViewById(R.id.checkBox9);




        // convert rental to code
        if(rent_low.isChecked()){
            rentRange.add(0);
            rentRange.add(350);
            rentalCode = "1";
            Log.i("rental code",rentalCode);
        }
        if(rent_medium.isChecked()){
            rentRange.add(351);
            rentRange.add(500);
            rentalCode = "2";
//            System.out.println(rentRange);
            Log.i("rental code",rentalCode);
        }
        if(rent_high.isChecked()){
            rentRange.add(501);
            rentRange.add(1000);
            rentalCode = "0";
//            System.out.println(rentRange);
            Log.i("rental code",rentalCode);
        }

        // convert distance to code
        if(distance_near.isChecked()){
            distanceCode = "1";
            Log.i("distance code",distanceCode);
        }
        if(distance_far.isChecked()){

            distanceCode = "0";
//            System.out.println(rentRange);
            Log.i("distance code",distanceCode);
        }

        //convert night transport to code
        if(nightTransport_yes.isChecked()){
            nightTransportCode = "1";
            Log.i("night transport code",nightTransportCode);
        }
        if(nightTransport_no.isChecked()){

            nightTransportCode = "0";
//            System.out.println(rentRange);
            Log.i("night transport code",nightTransportCode);
        }

        //convert gym to code
        if(gym_1.isChecked()){
            gymCode = "1";
            Log.i("gym code",gymCode);
        }
        if(gym_2.isChecked()){

            gymCode = "2";
//            System.out.println(rentRange);
            Log.i("gym code",gymCode);
        }
        if(gym_3.isChecked()){

            gymCode = "3";
//            System.out.println(rentRange);
            Log.i("gym code",gymCode);
        }

        //convert bank to code
        if(bank_1.isChecked()){
            bankCode = "1";
            Log.i("bank code",bankCode);
        }
        if(bank_2.isChecked()){

            bankCode = "2";
//            System.out.println(rentRange);
            Log.i("bank code",bankCode);
        }
        if(bank_3.isChecked()){

            bankCode = "3";
//            System.out.println(rentRange);
            Log.i("bank code",bankCode);
        }

        //convert restaurant to code
        if(restaurant_1.isChecked()){
            restaurantCode = "1";
            Log.i("restaurant code",restaurantCode);
        }
        if(restaurant_2.isChecked()){

            restaurantCode = "2";
//            System.out.println(rentRange);
            Log.i("restaurant code",restaurantCode);
        }
        if(restaurant_3.isChecked()){

            restaurantCode = "3";
//            System.out.println(rentRange);
            Log.i("restaurant code",restaurantCode);
        }

        // hit the API -> Volley
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String data = jsonObject.getString("result");
                            data = data.replaceAll("\'", "");
                            data = data.replaceAll("\\[", "");
                            data = data.replaceAll("\\]", "");
                            String[] elements = data.split(", ");
                            List<String> fixedLenghtList = Arrays.asList(elements);
                            ArrayList<String> listOfString = new ArrayList<String>(fixedLenghtList);

                            for (int i=0;i<elements.length;i++){
                                arrayList.add(elements[i]);
                            }
//                            arrayList = data.split(", ");

                            Log.i("ML result",arrayList.toString());
//                            Toast.makeText(HomePage.this,"Recommended Suburb by ML:"+"\n"+data,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Toast.makeText(HomePage.this,"error",Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String,String> getParams(){
                Map<String,String> params = new HashMap<String,String>();
                params.put("uni",uniCode);
                params.put("distance",distanceCode);
                params.put("rental",rentalCode);
                params.put("place_gym",gymCode);
                params.put("place_bank",bankCode);
                params.put("place_restaurant",restaurantCode);
                params.put("transport","1");
                params.put("night_transport",nightTransportCode);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(HomePage.this);
        queue.add(stringRequest);


        // ++++++++++++++++++++( CSV file reader )++++++++++++++++++++
        try {
            String line = ""; //Stores the lines
            int[] files = {R.raw.universities_with_suburb};
            //int[] files = {R.raw.atest, R.raw.abc}; //<Input files here
            for (int i=0; i<files.length; i++) {
                InputStream is = getResources().openRawResource(files[i]); //Retrieves the csv file stored in the app/src/main/res/raw folder
                BufferedReader reader = new BufferedReader( //Reads the file appropriately
                        new InputStreamReader(is, Charset.forName("UTF-8")));
                while ((line = reader.readLine()) != null)  //Reads every line in the csv file
                {
//                    Log.i("Log test", line); // Prints in Logcat
                    uniTokens = line.split(","); // split by ','

                    if(uniTokens[0].equals(uni)){
                        uniDetail.add(uniTokens[0]);
                        uniDetail.add(uniTokens[1]);
                        uniDetail.add(uniTokens[2]);
                        lat1 = Double.parseDouble(uniTokens[2]);
                        long1 = Double.parseDouble(uniTokens[1]);
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // ++++++++++++++++++++( CSV file reader )++++++++++++++++++++


        // ++++++++++++++++++++( CSV file reader )++++++++++++++++++++
        try {
            String line = ""; //Stores the lines
            int[] files = {R.raw.suburb_score};
            //int[] files = {R.raw.atest, R.raw.abc}; //<Input files here
            for (int i=0; i<files.length; i++) {
                InputStream is = getResources().openRawResource(files[i]); //Retrieves the csv file stored in the app/src/main/res/raw folder
                BufferedReader reader = new BufferedReader( //Reads the file appropriately
                        new InputStreamReader(is, Charset.forName("UTF-8")));
                while ((line = reader.readLine()) != null)  //Reads every line in the csv file
                {
//                    Log.i("Log test", line); // Prints in Logcat
                    suburbTokens = line.split(","); // split by ','

                    lat2 = Double.parseDouble(suburbTokens[2]);
                    long2 = Double.parseDouble(suburbTokens[3]);

                    // calculate difference on longitude
                    double longDiff = long1-long2;
                    // calculate distance
                    double distance = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2))
                            + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(longDiff));
                    distance = Math.acos(distance);
                    // convert distance radian to degree
                    distance = rad2deg(distance);
                    // distance in miles
                    distance = distance * 60 * 1.1515;
                    // distance in kilometers
                    distance = distance * 1.609344;
                    // check conditions
                    for (int j=0;j<arrayList.size();i++){
                        if (arrayList.get(j).equals(suburbTokens[0])){
                            String Sdistance = String.format("%.2f", distance);
                            distanceList.add(Sdistance);
                            Collections.sort(arrayList);

                        }else{
                            Log.i("test",arrayList.get(j));
                            Log.i("test",suburbTokens[0]);
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // ++++++++++++++++++++( CSV file reader )++++++++++++++++++++

        // ++++++++++++++++++++( CSV file reader )++++++++++++++++++++
        try {
            String line = ""; //Stores the lines
            int[] files = {R.raw.datacode};
            //int[] files = {R.raw.atest, R.raw.abc}; //<Input files here
            for (int i=0; i<files.length; i++) {
                InputStream is = getResources().openRawResource(files[i]); //Retrieves the csv file stored in the app/src/main/res/raw folder
                BufferedReader reader = new BufferedReader( //Reads the file appropriately
                        new InputStreamReader(is, Charset.forName("UTF-8")));
                while ((line = reader.readLine()) != null)  //Reads every line in the csv file
                {
//                    Log.i("Log test", line); // Prints in Logcat
                    uniCodes = line.split(","); // split by ','
                    // convert uni name to uni code
                    if(uni.equals(uniCodes[0])){
                        uniCode = uniCodes[1];
                        Log.i("university code",uniCode);

                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // ++++++++++++++++++++( CSV file reader )++++++++++++++++++++


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Actions to do after 5 seconds
                Intent i = new Intent(HomePage.this, ResultPage.class);
                i.putExtra("arrayList",arrayList);
                i.putExtra("distanceList",distanceList);
                i.putExtra("uniName",uni);
                i.putExtra("uniLat",String.valueOf(lat1));
                i.putExtra("uniLong",String.valueOf(long1));
                Log.i("distance",distanceList.toString());
                startActivity(i);
            }
        }, 3500);

    }


    // convert radian to degree
    private double rad2deg(double distance) {
        return (distance*180.0/Math.PI);

    }

    // convert degree to radian
    private double deg2rad(double lat1) {
        return (lat1*Math.PI/180.0);
    }
}



