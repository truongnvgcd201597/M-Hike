package com.example.project_mobile.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.project_mobile.R;
import com.example.project_mobile.adapters.HikeAdapter;
import com.example.project_mobile.database.MyDatabaseHelper;
import com.example.project_mobile.models.Hike;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerView;
    private MyDatabaseHelper myDB;
    private List<Hike> hikes = new ArrayList<>();
    private HikeAdapter hikeAdapter;
    private ImageView clear_data;
    private AlertDialog.Builder alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setInit();
        setNavigationView();
        myDB = new MyDatabaseHelper(MainActivity.this);
        displayHike();
        initializeRecyclerView();
        alertDialog = new AlertDialog.Builder(this);
        clear_data.setOnClickListener(v -> showClearDataDialog());
    }

    private void initializeRecyclerView() {
        hikeAdapter = new HikeAdapter(MainActivity.this, this, hikes);
        recyclerView.setAdapter(hikeAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
    }

    public void displayHike() {
        try {
            Cursor cursor = myDB.readAllHikes();
            if (cursor.getCount() == 0) {
                Toast.makeText(MainActivity.this, "No data", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    hikes.add(new Hike(Integer.parseInt(cursor.getString(0)),
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5),
                            cursor.getString(6),
                            cursor.getString(7)));
                }
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setInit() {
        recyclerView = findViewById(R.id.recyclerView);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.home_page);
        clear_data = findViewById(R.id.remove_buttons);
    }

    private void setNavigationView() {
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.home_page:
                    return true;
                case R.id.add_item:
                    startActivity(new Intent(getApplicationContext(), AddHikeActivity.class));
                    return true;
                case R.id.search_item:
                    startActivity(new Intent(getApplicationContext(), SearchHikeActivity.class));
                    return true;
            }
            return false;
        });
    }

    private void showClearDataDialog() {
        alertDialog.setTitle("All data will be deleted")
                .setMessage("Are you sure you want to delete all items?")
                .setCancelable(true)
                .setPositiveButton("Yes", (dialogInterface, i) -> clearAllData())
                .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.cancel())
                .show();
    }

    private void clearAllData() {
        myDB.deleteAll();
        hikes.clear();
        hikeAdapter.notifyDataSetChanged();
        Toast.makeText(MainActivity.this, "All data cleared", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myDB.close();
    }
}