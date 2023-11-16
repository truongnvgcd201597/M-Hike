package com.example.project_mobile.activities.observation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project_mobile.R;
import com.example.project_mobile.adapters.ObservationAdapter;
import com.example.project_mobile.database.MyDatabaseHelper;
import com.example.project_mobile.models.Hike;
import com.example.project_mobile.models.Observation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ObservationSearchActivity extends AppCompatActivity {
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
        setContentView(R.layout.activity_search_observation);
        setInit();
        setNavigationView();
        hikeID = (String) getIntent().getSerializableExtra("hike_id");
        date_hike = (String) getIntent().getSerializableExtra("hike_date");
        myDB = new MyDatabaseHelper(ObservationSearchActivity.this);
        displayObservation(hikeID);
        String name = (String) getIntent().getSerializableExtra("hike_name");
        String location = (String) getIntent().getSerializableExtra("hike_location");
        String date = (String) getIntent().getSerializableExtra("hike_date");
        String parking_available = (String) getIntent().getSerializableExtra("parking_available");
        String length = (String) getIntent().getSerializableExtra("length_hike");
        String level = (String) getIntent().getSerializableExtra("level_hike");
        String des = (String) getIntent().getSerializableExtra("des_hike");
        Hike hike = new Hike(Integer.parseInt(hikeID), name, location, date_hike, parking_available, length, level, des);
        observationAdapter = new ObservationAdapter(ObservationSearchActivity.this,this, observations, hike);
        recyclerView.setAdapter(observationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(ObservationSearchActivity.this));
    }

    private void setInit(){
        back_ic = findViewById(R.id.back_icon_button);
        recyclerView = findViewById(R.id.recyclerView);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.search_item);
    }

    public void displayObservation(String hikeID){
        Cursor cursor = myDB.readAllObservation(hikeID);
        if(cursor.getCount() == 0){
            Toast.makeText(ObservationSearchActivity.this, "No data", Toast.LENGTH_SHORT).show();
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
                    case R.id.search_item:
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
                    case R.id.home_page:

                        Intent intent2 = new Intent(getApplicationContext(), ObservationActivity.class);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_action).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                observationAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                observationAdapter.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

}