package com.example.project_mobile.activities.observation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project_mobile.R;
import com.example.project_mobile.activities.DetailHikeActivity;

import java.text.ParseException;

public class ObservationDetailActivity extends AppCompatActivity {
    private ImageView back_ic;
    private String hike_id;
    private TextView title_txt, name_obs, time_obs, comments_obs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_observation_detail);
        hike_id = (String) getIntent().getSerializableExtra("hike_id");
        setInit();
        setListeners();
        try {
            getAndSetIntentData();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void setInit(){
        back_ic = findViewById(R.id.back_icon_button);
        title_txt = findViewById(R.id.observation_title);
        title_txt.setText("Observation Detail");
        name_obs = findViewById(R.id.obs_name_detail);
        time_obs = findViewById(R.id.obs_time_detail);
        comments_obs = findViewById(R.id.obs_comment_detail);
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
            Intent intent = new Intent(ObservationDetailActivity.this, ObservationActivity.class);
            intent.putExtra("hike_id", hike_id);
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

    public void getAndSetIntentData() throws ParseException {
        if (getIntent().hasExtra("obs_name") &&
                getIntent().hasExtra("obs_time") &&
                getIntent().hasExtra("obs_comment")){
            name_obs.setText((String) getIntent().getSerializableExtra("obs_name"));
            time_obs.setText((String) getIntent().getSerializableExtra("obs_time"));
            comments_obs.setText((String) getIntent().getSerializableExtra("obs_comment"));
        }
    }
}