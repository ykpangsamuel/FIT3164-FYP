package com.example.subrecodemo1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailPage extends AppCompatActivity {
    TextView textName;
    TextView textRent;
    TextView textDistance;
    TextView textNightTransport;
    TextView textPublicTransportTrain;
    TextView textPublicTransportTram;
    TextView textPublicTransportBus;
    TextView textPublicTransportVline;
    Button searchBtn;
    private ArrayList<String> arrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page);
        // set title for this screen
        setTitle("Suburb Detail");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        // initialise all variables
        String uniName = intent.getStringExtra("uniName");
        String suburbName = intent.getStringExtra("subName");
        String suburbRent = intent.getStringExtra("subRent");
        String suburbDistance = intent.getStringExtra("subDistance");
        String suburbNightTransport = intent.getStringExtra("subNightTransport");
        String suburbPublicTransportTrain = intent.getStringExtra("subPublicTransportTrain");
        String suburbPublicTransportTram = intent.getStringExtra("subPublicTransportTram");
        String suburbPublicTransportBus = intent.getStringExtra("subPublicTransportBus");
        String suburbPublicTransportVline = intent.getStringExtra("subPublicTransportVline");
        arrayList = intent.getStringArrayListExtra("arrayList");
        Log.i("test_v2",arrayList.toString());
        // set data inside each textview
        textName = findViewById(R.id.s_name);
        textName.setText(suburbName);
        textRent = findViewById(R.id.s_rent);
        textRent.setText("$"+suburbRent+" /Week");
        textDistance = findViewById(R.id.s_distance);
        textDistance.setText(suburbDistance + " km");
        searchBtn = findViewById(R.id.searchButton);
        textNightTransport = findViewById(R.id.s_nightTransport);
        if (suburbNightTransport.equals("TRUE")) {
            textNightTransport.setText("Available");
        }
        else{
            textNightTransport.setText("Unavailable");
        }
//        textNightTransport.setText(suburbNightTransport);
        textPublicTransportTrain = findViewById(R.id.s_transportTrain);
        textPublicTransportTram = findViewById(R.id.s_transportTram);
        textPublicTransportBus = findViewById(R.id.s_transportBus);
        textPublicTransportVline = findViewById(R.id.s_transportVline);

        // convert value from o and 1 to yes to no in detail page
        if (suburbPublicTransportTrain.equals("1")){
            textPublicTransportTrain.setText("Train: ✓");
        }else{
            textPublicTransportTrain.setText("Train: x");
        }
        if (suburbPublicTransportTram.equals("1")){
            textPublicTransportTram.setText("Tram: ✓");
        }else{
            textPublicTransportTram.setText("Tram: x");
        }
        if (suburbPublicTransportBus.equals("1")){
            textPublicTransportBus.setText("Bus: ✓");
        }else{
            textPublicTransportBus.setText("Bus: x");
        }
        if (suburbPublicTransportVline.equals("1")){
            textPublicTransportVline.setText("V/Line: ✓");
        }else{
            textPublicTransportVline.setText("V/Line: x");
        }
        // jump to google map from the view button
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse("https://www.google.co.in/maps/dir/"+suburbName+",VIC"+"/"+uniName);
                Intent intent = new Intent(Intent.ACTION_VIEW,uri);
//             Intent intent = new Intent(MainActivity3.this,MainActivity4.class);
                intent.setPackage("com.google.android.apps.maps");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("subName",suburbName);
                intent.putExtra("uniName",uniName);
                startActivity(intent);


            }
        });


    }
    // create a new page
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.statistic_manu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    // pass the data to statistics page
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.statistic_button:
                Intent intent = getIntent();
                String uniName = intent.getStringExtra("uniName");
                String suburbName = intent.getStringExtra("subName");
                Intent i = new Intent(this, StatisticsPage.class);
                i.putExtra("uniName",uniName);
                i.putExtra("subName",suburbName);
                i.putExtra("arrayList",arrayList);



                startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }
}