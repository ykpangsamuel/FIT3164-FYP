package com.example.subrecodemo1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;


public class ResultPage extends AppCompatActivity {
    private CardView cardView;
    ListView listView;
    String [] uniTokens = new String[0];
    private ArrayList<String> uniDetail = new ArrayList<>();
    private ArrayList<String> subDetail = new ArrayList<>();
    private ArrayList<String> distanceList = new ArrayList<>();
    double lat1 = 0, long1 = 0, lat2 = 0, long2 = 0, distanceDiff = 0;
    String [] suburbTokens = new String[0];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_page);
        setTitle("Recommended Suburbs");

        // set back button for second activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle bundle = getIntent().getExtras();
        ArrayList<String>arrayList = bundle.getStringArrayList("arrayList");
        String uni = bundle.getString("uniName");
        System.out.println(uni);
        Log.i("ML",arrayList.toString());


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
                        System.out.println(lat1);
                        System.out.println(long1);
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
            int[] files = {R.raw.suburb_details};
            for (int i=0; i<files.length; i++) {
                InputStream is = getResources().openRawResource(files[i]); //Retrieves the csv file stored in the app/src/main/res/raw folder
                BufferedReader reader = new BufferedReader( //Reads the file appropriately
                        new InputStreamReader(is, Charset.forName("UTF-8")));
                while ((line = reader.readLine()) != null)  //Reads every line in the csv file
                {
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
//                    System.out.println(arrayList);
//                    System.out.println(suburbTokens[0]);
                    for (int j=0;j<arrayList.size();j++){
                        if (arrayList.get(j).equals(suburbTokens[0])){
                            String Sdistance = String.format("%.2f", distance);
                            distanceList.add(Sdistance);
                        }

                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // ++++++++++++++++++++( CSV file reader )++++++++++++++++++++



        listView = (ListView) findViewById(R.id.listView);
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayList);
        listView.setAdapter(arrayAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?>parent,View view, int position,long id) {
                String [] suburbTokens = new String[0];
                Intent intent = new Intent(ResultPage.this, DetailPage.class);
                intent.putExtra("uni",uni);
//                Log.i("Log test",uni);

                // ++++++++++++++++++++( CSV file reader )++++++++++++++++++++
                try {
                    String line = ""; //Stores the lines
                    int[] files = {R.raw.suburb_details};
                    //int[] files = {R.raw.atest, R.raw.abc}; //<Input files here
                    for (int i=0; i<files.length; i++) {
                        InputStream is = getResources().openRawResource(files[i]); //Retrieves the csv file stored in the app/src/main/res/raw folder
                        BufferedReader reader = new BufferedReader( //Reads the file appropriately
                                new InputStreamReader(is, Charset.forName("UTF-8")));
                        while ((line = reader.readLine()) != null)  //Reads every line in the csv file
                        {

                            suburbTokens = line.split(","); // split by ','

                            if(suburbTokens[0].equals(arrayList.get(position))){
                                System.out.println(distanceList);


//                                startActivity(new Intent(MainActivity2.this,MainActivity3.class));

//                                Intent intent = new Intent(MainActivity2.this,MainActivity3.class);
                                intent.putExtra("subName",suburbTokens[0]);
                                intent.putExtra("subRent",suburbTokens[1]);
                                intent.putExtra("subDistance",distanceList.get(position));
                                intent.putExtra("subPublicTransportTrain",suburbTokens[8]);
                                intent.putExtra("subPublicTransportTram",suburbTokens[9]);
                                intent.putExtra("subPublicTransportBus",suburbTokens[10]);
                                intent.putExtra("subPublicTransportVline",suburbTokens[11]);
                                intent.putExtra("subNightTransport",suburbTokens[12]);
                                intent.putExtra("arrayList",arrayList);



                                intent.putExtra("uniName",uni);
                                startActivity(intent);
                                finish();
                            }




//
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // ++++++++++++++++++++( CSV file reader )++++++++++++++++++++

//                startActivity(new Intent(MainActivity2.this,MainActivity3.class));

            }
        });



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