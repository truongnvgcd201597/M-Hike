package com.example.project_mobile.activities.observation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project_mobile.R;
import com.example.project_mobile.activities.DetailHikeActivity;
import com.example.project_mobile.adapters.ObservationAdapter;
import com.example.project_mobile.database.MyDatabaseHelper;
import com.example.project_mobile.models.Hike;
import com.example.project_mobile.models.Observation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ObservationActivity extends AppCompatActivity {
    private ImageView back_ic;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private MyDatabaseHelper myDB;
    private List<Observation> observations = new ArrayList<>();
    private ObservationAdapter observationAdapter;
    private String hikeID, date_hike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation);
        setInit();
        setNavigationView();
        hikeID = (String) getIntent().getSerializableExtra("hike_id");
        date_hike = (String) getIntent().getSerializableExtra("hike_date");
        myDB = new MyDatabaseHelper(ObservationActivity.this);
        displayObservation(hikeID);
        String name = (String) getIntent().getSerializableExtra("hike_name");
        String location = (String) getIntent().getSerializableExtra("hike_location");
        String date = (String) getIntent().getSerializableExtra("hike_date");
        String parking_available = (String) getIntent().getSerializableExtra("parking_available");
        String length = (String) getIntent().getSerializableExtra("length_hike");
        String level = (String) getIntent().getSerializableExtra("level_hike");
        String des = (String) getIntent().getSerializableExtra("des_hike");
        Hike hike = new Hike(Integer.parseInt(hikeID), name, location, date_hike, parking_available, length, level, des);
        observationAdapter = new ObservationAdapter(ObservationActivity.this,this, observations, hike);
        recyclerView.setAdapter(observationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ObservationActivity.this));
        setListeners();
    }

    private void setInit(){
        back_ic = findViewById(R.id.back_ic_obs);
        recyclerView = findViewById(R.id.recyclerView);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.home_page);
    }

    private void setListeners(){
        String name = (String) getIntent().getSerializableExtra("hike_name");
        String location = (String) getIntent().getSerializableExtra("hike_location");
        String date = (String) getIntent().getSerializableExtra("hike_date");
        String parking_available = (String) getIntent().getSerializableExtra("parking_available");
        String length = (String) getIntent().getSerializableExtra("length_hike");
        String level = (String) getIntent().getSerializableExtra("level_hike");
        String des = (String) getIntent().getSerializableExtra("des_hike");
        back_ic.setOnClickListener(v -> {
            Intent intent = new Intent(ObservationActivity.this, DetailHikeActivity.class);
            intent.putExtra("hike_id", hikeID);
            intent.putExtra("hike_name", name);
            intent.putExtra("hike_location", location);
            intent.putExtra("hike_date", date);
            intent.putExtra("parking_available", parking_available);
            intent.putExtra("length_hike", length);
            intent.putExtra("level_hike", level);
            intent.putExtra("des_hike", des);
            startActivity(intent);
        });
    }

    public void displayObservation(String hikeID){
        Cursor cursor = myDB.readAllObservation(hikeID);
        if(cursor.getCount() == 0){
            Toast.makeText(ObservationActivity.this, "No data", Toast.LENGTH_SHORT).show();
        }else {
            while (cursor.moveToNext()){
                observations.add(new Observation(Integer.parseInt(cursor.getString(0)),
                        Integer.parseInt(cursor.getString(1)),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4)));
            }
        }
    }

    private void setNavigationView(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                String name = (String) getIntent().getSerializableExtra("hike_name");
                String location = (String) getIntent().getSerializableExtra("hike_location");
                String parking_available = (String) getIntent().getSerializableExtra("parking_available");
                String length = (String) getIntent().getSerializableExtra("length_hike");
                String level = (String) getIntent().getSerializableExtra("level_hike");
                String des = (String) getIntent().getSerializableExtra("des_hike");
                switch (item.getItemId()){
                    case R.id.home_page:
                        return true;
                    case R.id.add_item:
                        Intent intent1 = new Intent(getApplicationContext(), AddObservationActivity.class);
                        intent1.putExtra("hike_id", hikeID);
                        intent1.putExtra("hike_date", date_hike);
                        intent1.putExtra("hike_name", name);
                        intent1.putExtra("hike_location", location);
                        intent1.putExtra("parking_available", parking_available);
                        intent1.putExtra("length_hike", length);
                        intent1.putExtra("level_hike", level);
                        intent1.putExtra("des_hike", des);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.search_item:
                        Intent intent2 = new Intent(getApplicationContext(), SearchObservationActivity.class);
                        intent2.putExtra("hike_id", hikeID);
                        intent2.putExtra("hike_date", date_hike);
                        intent2.putExtra("hike_name", name);
                        intent2.putExtra("hike_location", location);
                        intent2.putExtra("parking_available", parking_available);
                        intent2.putExtra("length_hike", length);
                        intent2.putExtra("level_hike", level);
                        intent2.putExtra("des_hike", des);
                        startActivity(intent2);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

}