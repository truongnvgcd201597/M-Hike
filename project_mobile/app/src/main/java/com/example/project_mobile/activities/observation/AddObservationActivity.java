package com.example.project_mobile.activities.observation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.project_mobile.R;
import com.example.project_mobile.activities.AddHikeActivity;
import com.example.project_mobile.activities.MainActivity;
import com.example.project_mobile.database.MyDatabaseHelper;
import com.example.project_mobile.models.Hike;
import com.example.project_mobile.models.Observation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class AddObservationActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private Button save_obs, date_time_btn;
    private EditText name_obs, comments;
    private TimePickerDialog timePickerDialog;
    private AlertDialog.Builder alertDialog;
    private ImageView back_ic;
    private TextView title_action;
    private String hikeID, date_hike;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_observation);
        hikeID = (String) getIntent().getStringExtra("hike_id");
        date_hike = (String) getIntent().getStringExtra("hike_date");
        setInit();
        setListener();
        setNavigationView();
    }

    private void setInit(){
        back_ic = findViewById(R.id.back_icon_button);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.add_item);
        title_action = findViewById(R.id.observation_title);
        title_action.setText("Add Observation");
        save_obs = findViewById(R.id.save_obs);
        name_obs = findViewById(R.id.name_obs);
        comments = findViewById(R.id.comment_ob);
        alertDialog = new AlertDialog.Builder(this);
        date_time_btn = findViewById(R.id.datePickerButton);
        date_time_btn.setText(getTime() + " - " + date_hike);
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hours, int minutes) {
                date_time_btn.setText((hours >= 10 ? hours : ("0" + String.valueOf(hours))) + ":" + (minutes >= 10 ? hours : ("0" + String.valueOf(minutes))) + " - " + date_hike);
            }
        };
        Calendar cal = Calendar.getInstance();
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this,timeSetListener,hours,minutes,true);
    }

    private void setListener(){
        back_ic.setOnClickListener(v -> {
            Intent intent = new Intent(AddObservationActivity.this, ObservationActivity.class);
            intent.putExtra("hike_id", hikeID);
            startActivity(intent);
            overridePendingTransition(0,0);
        });
        save_obs.setOnClickListener(v -> {
            if(isValidAddHikeDetails()){
                alertDialog.setTitle("Confirmation")
                        .setMessage("Name: " + name_obs.getText().toString().trim() +"\n"+
                                "Time: "+ date_time_btn.getText().toString().trim()+"\n"+
                                "Comment: "+ comments.getText().toString().trim())
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                addNewObservation();
                                startActivity(new Intent(AddObservationActivity.this, ObservationActivity.class));
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        }).show();
            }
        });
    }

    private void addNewObservation(){
        MyDatabaseHelper myDB = new MyDatabaseHelper(AddObservationActivity.this);
        Observation observation = new Observation();
        observation.setObs_name(name_obs.getText().toString().trim());
        observation.setObs_comment(comments.getText().toString().trim());
        observation.setHike_id(Integer.valueOf(hikeID));
        observation.setObs_time(date_time_btn.getText().toString().trim());
        myDB.addNewObservation(observation);
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
                    case R.id.add_item:
                        return true;
                    case R.id.home_page:
                        Intent intent1 = new Intent(getApplicationContext(), ObservationActivity.class);
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

    public void openTimePiker(View view) {
        timePickerDialog.show();
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidAddHikeDetails(){
        if(name_obs.getText().toString().trim().isEmpty()){
            showToast("Enter name of observation");
            return false;
        }else if(comments.getText().toString().trim().isEmpty()){
            showToast("Enter location of hike");
            return false;
        }
        return true;
    }

    private String getTime() {
        Calendar cal = Calendar.getInstance();
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        return (hours >= 10 ? hours : ("0" + String.valueOf(hours))) + ":" + (minutes >= 10 ? minutes : ("0" + String.valueOf(minutes))) ;
    }
}