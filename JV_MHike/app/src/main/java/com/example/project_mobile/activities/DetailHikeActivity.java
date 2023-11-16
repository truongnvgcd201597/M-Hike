package com.example.project_mobile.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.project_mobile.R;
import com.example.project_mobile.activities.observation.ObservationActivity;

import java.text.ParseException;

public class DetailHikeActivity extends AppCompatActivity {
    private ImageView back_ic;
    private Button see_observation;
    private TextView back_txt, hike_name_detail, hike_location_detail, hike_date_detail, parking_available_detail, hike_length_detail, hike_level_detail, hike_description_detail;
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
            intentObservation.putExtra("hike_name", hike_name_detail.getText());
            intentObservation.putExtra("hike_location", hike_location_detail.getText());
            intentObservation.putExtra("hike_date", hike_date_detail.getText());
            intentObservation.putExtra("parking_available", parking_available_detail.getText());
            intentObservation.putExtra("length_hike", hike_length_detail.getText());
            intentObservation.putExtra("level_hike", hike_level_detail.getText());
            intentObservation.putExtra("des_hike", hike_description_detail.getText());
            startActivity(intentObservation);
        });
    }

    private void setInit(){
        back_ic = findViewById(R.id.back_icon_button);
        back_txt = findViewById(R.id.back_txt);
        see_observation = findViewById(R.id.see_all_observation);
        hike_name_detail = findViewById(R.id.hike_name_detail);
        hike_location_detail = findViewById(R.id.hike_location_detail);
        hike_date_detail = findViewById(R.id.hike_date_detail);
        parking_available_detail = findViewById(R.id.parking_available_detail);
        hike_length_detail = findViewById(R.id.hike_length_detail);
        hike_level_detail = findViewById(R.id.hike_level_detail);
        hike_description_detail = findViewById(R.id.hike_description_detail);
    }

    private void setListener() {
        Intent intent = new Intent(DetailHikeActivity.this, MainActivity.class);

        View.OnClickListener backClickListener = v -> startActivity(intent);
        back_txt.setOnClickListener(backClickListener);
        back_ic.setOnClickListener(backClickListener);
    }


    public void getAndSetIntentData() throws ParseException {
        if (getIntent().hasExtra("hike_name") &&
                getIntent().hasExtra("hike_location") &&
                getIntent().hasExtra("hike_date") &&
                getIntent().hasExtra("parking_available") &&
                getIntent().hasExtra("length_hike")&&
                getIntent().hasExtra("level_hike")&&
                getIntent().hasExtra("des_hike")){
            hike_name_detail.setText((String) getIntent().getSerializableExtra("hike_name"));
            hike_location_detail.setText((String) getIntent().getSerializableExtra("hike_location"));
            hike_date_detail.setText((String) getIntent().getSerializableExtra("hike_date"));
            parking_available_detail.setText((String) getIntent().getSerializableExtra("parking_available"));
            hike_length_detail.setText((String) getIntent().getSerializableExtra("length_hike"));
            hike_level_detail.setText((String) getIntent().getSerializableExtra("level_hike"));
            hike_description_detail.setText((String) getIntent().getSerializableExtra("des_hike"));
        }
    }
}