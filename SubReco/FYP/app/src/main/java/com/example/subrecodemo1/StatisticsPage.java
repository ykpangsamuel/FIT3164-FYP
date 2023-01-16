package com.example.subrecodemo1;

import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class StatisticsPage extends AppCompatActivity {
    BarChart barChart;
    LineChart lineChart;
    String[] suburbTokens = new String[0];
    String[] rentalTokens = new String[0];
    private ArrayList<String> crimeRate = new ArrayList<>();
    private ArrayList<String> subDetail1 = new ArrayList<>();
    private ArrayList<String> subDetail2 = new ArrayList<>();
    private ArrayList<String> subDetail3 = new ArrayList<>();
    private ArrayList<String> subDetail4 = new ArrayList<>();
    private ArrayList<String> subDetail5 = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.statistics_page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        ArrayList<String> suburbList = intent.getStringArrayListExtra("arrayList");
        String suburbName = intent.getStringExtra("subName");
        setTitle("Statistics for " + suburbName);
        System.out.println(suburbList);

        //Assign variables
        barChart = findViewById(R.id.bar_chart);
        lineChart = findViewById(R.id.line_chart);

        // Initialise array list
        ArrayList<BarEntry> barEntries = new ArrayList<>();
        ArrayList<Entry> lineEntries = new ArrayList<>();
        ArrayList<Entry> lineEntries2 = new ArrayList<>();

        // ++++++++++++++++++++( CSV file reader )++++++++++++++++++++
        try {
            String line = ""; //Stores the lines
            int[] files = {R.raw.crime_rate_final};

            for (int i = 0; i < files.length; i++) {
                InputStream is = getResources().openRawResource(files[i]); //Retrieves the csv file stored in the app/src/main/res/raw folder
                BufferedReader reader = new BufferedReader( //Reads the file appropriately
                        new InputStreamReader(is, Charset.forName("UTF-8")));
                while ((line = reader.readLine()) != null)  //Reads every line in the csv file
                {

                    suburbTokens = line.split(","); // split by ','


                    if (suburbTokens[1].equals(suburbName)) {
//                        System.out.println(suburbTokens[3]);
                        Entry lineEntry = new Entry(Integer.parseInt(suburbTokens[0]), Float.parseFloat(suburbTokens[2]));
                        i++;
                        lineEntries.add(lineEntry);

//                        BarEntry barEntry = new BarEntry(Float.parseFloat(suburbTokens[0]),Float.parseFloat(suburbTokens[3]));
//                        barEntries.add(barEntry);
                    }
                    ;

                    if (suburbTokens[1].equals("average")) {
                        System.out.println(suburbTokens[0]);
                        Entry lineEntry_avg = new Entry(Integer.parseInt(suburbTokens[0]), Float.parseFloat(suburbTokens[2]));
                        i++;
                        lineEntries2.add(lineEntry_avg);

                    }
                    ;
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // ++++++++++++++++++++( CSV file reader )++++++++++++++++++++


        // ++++++++++++++++++++( CSV file reader )++++++++++++++++++++
        try {
            String line = ""; //Stores the lines
            int[] files = {R.raw.rental_rate_for_vis};

            for (int i = 0; i < files.length; i++) {
                InputStream is = getResources().openRawResource(files[i]); //Retrieves the csv file stored in the app/src/main/res/raw folder
                BufferedReader reader = new BufferedReader( //Reads the file appropriately
                        new InputStreamReader(is, Charset.forName("UTF-8")));
                while ((line = reader.readLine()) != null)  //Reads every line in the csv file
                {

                    rentalTokens = line.split(","); // split by ','


                    if (rentalTokens[0].equals(suburbName)) {
//                        System.out.println(suburbTokens[3]);
                        for (int j = 1; j < rentalTokens.length; j++) {
                            BarEntry barEntry = new BarEntry((1999 + j), Float.parseFloat(rentalTokens[j]));
                            barEntries.add(barEntry);
                        }

                    }
                    ;

                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // ++++++++++++++++++++( CSV file reader )++++++++++++++++++++


        //Initialise bar data set
        BarDataSet barDataSet = new BarDataSet(barEntries, suburbName);
        LineDataSet lineDataSet = new LineDataSet(lineEntries, suburbName);
        LineDataSet lineDataSet2 = new LineDataSet(lineEntries2, "Average of all suburbs");
        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        // combine two line data set together
        dataSets.add(lineDataSet);
        dataSets.add(lineDataSet2);
        LineData data = new LineData(dataSets);

        //hide draw value
        barDataSet.setDrawValues(false);

        // set color
        lineDataSet.setColors(Color.BLUE);
        lineDataSet2.setColors(Color.RED);
        barDataSet.setColors(Color.BLUE);

        //insert data into charts
        barChart.setData(new BarData(barDataSet));
        lineChart.setData(data);
        lineChart.invalidate();
        
        // set text size in chart
        lineDataSet.setValueTextSize(12);
        lineDataSet2.setValueTextSize(12);
        XAxis x = lineChart.getXAxis();
        x.setDrawGridLines(false);
        x.setPosition(XAxis.XAxisPosition.BOTTOM);
        x.setTextSize(12);

        Legend legend = lineChart.getLegend();
        legend.setFormSize(12);
        legend.setTextSize(12);
        Legend legend2 = barChart.getLegend();
        legend2.setFormSize(12);
        legend2.setTextSize(12);


        XAxis b_x = barChart.getXAxis();
        b_x.setDrawGridLines(false);
        b_x.setPosition(XAxis.XAxisPosition.BOTTOM);
        b_x.setTextSize(12);

        // remove the thousand separator
        final ValueFormatter format = new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
//                return super.getFormattedValue(value);

                return String.valueOf(value).replace(".0","");
            }
        };
        x.setValueFormatter(format);
        b_x.setValueFormatter(format);
        // configuration for line chart
        lineChart.getDescription().setText("Crime Rate by Year");
        lineChart.getDescription().setTextSize(18);
        lineChart.getDescription().setPosition(700, 40);
        lineChart.animateY(2000);
        lineChart.setBackgroundColor(Color.WHITE);
        lineChart.getDescription().setTextColor(Color.BLACK);
        lineChart.getAxisRight().setEnabled(false);
        // configuration for bar chart
        barChart.animateY(2000);
        barChart.getDescription().setText("Rental over Time");
        barChart.getDescription().setTextSize(18);
        barChart.setBackgroundColor(Color.WHITE);
        barChart.getDescription().setPosition(700, 60);
        barChart.getDescription().setTextColor(Color.BLACK);
        barChart.getAxisRight().setEnabled(false);



    }
}