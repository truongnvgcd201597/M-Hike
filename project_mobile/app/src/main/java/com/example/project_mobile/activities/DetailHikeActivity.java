package com.example.project_mobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_mobile.R;
import com.example.project_mobile.activities.observation.ObservationActivity;

import java.text.ParseException;

public class DetailHikeActivity extends AppCompatActivity {
    private ImageView back_ic;
    private Button see_observation;
    private TextView back_txt, name_hike_detail, location_hike_detail, date_hike_detail, parking_available_detail, length_hike_detail, level_hike_detail, des_hike_detail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_hike);
        setInit();
        setListener();
        try {
            getAndSetIntentData();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        see_observation.setOnClickListener(v -> {
            String hikeID = (String) getIntent().getSerializableExtra("hike_id");
            Intent intentObservation = new Intent(DetailHikeActivity.this, ObservationActivity.class);
            intentObservation.putExtra("hike_id", hikeID);
            intentObservation.putExtra("hike_name", name_hike_detail.getText());
            intentObservation.putExtra("hike_location", location_hike_detail.getText());
            intentObservation.putExtra("hike_date", date_hike_detail.getText());
            intentObservation.putExtra("parking_available", parking_available_detail.getText());
            intentObservation.putExtra("length_hike", length_hike_detail.getText());
            intentObservation.putExtra("level_hike", level_hike_detail.getText());
            intentObservation.putExtra("des_hike", des_hike_detail.getText());
            startActivity(intentObservation);
        });
    }

    private void setInit(){
        back_ic = findViewById(R.id.back_icon);
        back_txt = findViewById(R.id.back_txt);
        see_observation = findViewById(R.id.see_all_observation);
        name_hike_detail = findViewById(R.id.name_hike_detail);
        location_hike_detail = findViewById(R.id.location_hike_detail);
        date_hike_detail = findViewById(R.id.date_hike_detail);
        parking_available_detail = findViewById(R.id.parking_available_detail);
        length_hike_detail = findViewById(R.id.length_hike_detail);
        level_hike_detail = findViewById(R.id.level_hike_detail);
        des_hike_detail = findViewById(R.id.des_hike_detail);
    }

    private void setListener(){
        Intent intent = new Intent(DetailHikeActivity.this, MainActivity.class);
        back_txt.setOnClickListener(v -> {
            startActivity(intent);
        });
        back_ic.setOnClickListener(v -> {
            startActivity(intent);
        });
    }

    public void getAndSetIntentData() throws ParseException {
        if (getIntent().hasExtra("hike_name") &&
                getIntent().hasExtra("hike_location") &&
                getIntent().hasExtra("hike_date") &&
                getIntent().hasExtra("parking_available") &&
                getIntent().hasExtra("length_hike")&&
                getIntent().hasExtra("level_hike")&&
                getIntent().hasExtra("des_hike")){
            name_hike_detail.setText((String) getIntent().getSerializableExtra("hike_name"));
            location_hike_detail.setText((String) getIntent().getSerializableExtra("hike_location"));
            date_hike_detail.setText((String) getIntent().getSerializableExtra("hike_date"));
            parking_available_detail.setText((String) getIntent().getSerializableExtra("parking_available"));
            length_hike_detail.setText((String) getIntent().getSerializableExtra("length_hike"));
            level_hike_detail.setText((String) getIntent().getSerializableExtra("level_hike"));
            des_hike_detail.setText((String) getIntent().getSerializableExtra("des_hike"));
        }
    }
}