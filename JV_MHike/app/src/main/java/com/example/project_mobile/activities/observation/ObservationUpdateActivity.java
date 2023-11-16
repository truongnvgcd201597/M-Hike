package com.example.project_mobile.activities.observation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.project_mobile.R;
import com.example.project_mobile.database.MyDatabaseHelper;
import com.example.project_mobile.models.Observation;

import java.text.ParseException;
import java.util.Calendar;

public class ObservationUpdateActivity extends AppCompatActivity {
    private ImageView back_ic;
    private TextView title_actionbar;
    private Button updateObs;
    private EditText nameObs, commentsObs;
    private Button timeObs;
    private AlertDialog.Builder alertDialog;
    private String hikeId, dateHike;
    private TimePickerDialog timePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_observation);
        hikeId = (String) getIntent().getSerializableExtra("hike_id");
        dateHike = (String) getIntent().getSerializableExtra("hike_date");
        setInit();
        try {
            getAndSetIntentData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setListeners();
    }

    private void updateObservation(){
        MyDatabaseHelper databaseHelper = new MyDatabaseHelper(ObservationUpdateActivity.this);
        Observation observation = new Observation();
        observation.setHikeId(Integer.valueOf(hikeId));
        observation.setObsId(Integer.valueOf((String) getIntent().getSerializableExtra("obs_id")));
        observation.setObsName(nameObs.getText().toString().trim());
        observation.setObsTime(timeObs.getText().toString().trim());
        observation.setObsComment(commentsObs.getText().toString().trim());
        databaseHelper.updateObservation(observation);
    }

    private void setInit(){
        back_ic = findViewById(R.id.back_icon_button);
        title_actionbar = findViewById(R.id.observation_title);
        title_actionbar.setText("Update Observation");
        nameObs = findViewById(R.id.name_obs_update);
        commentsObs = findViewById(R.id.comments_obs_update);
        updateObs = findViewById(R.id.update_obs);
        timeObs = findViewById(R.id.datePickerButton);
        alertDialog = new AlertDialog.Builder(this);
    }

    private void setListeners(){
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hours, int minutes) {
                timeObs.setText((hours >= 10 ? hours : ("0" + String.valueOf(hours))) + ":" + (minutes >= 10 ? minutes : ("0" + String.valueOf(minutes))) + " - " + dateHike);
            }
        };
        Calendar cal = Calendar.getInstance();
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this,timeSetListener,hours,minutes,true);

        String hike_name = (String) getIntent().getSerializableExtra("hike_name");
        String hike_location = (String) getIntent().getSerializableExtra("hike_location");
        String hike_date = (String) getIntent().getSerializableExtra("hike_date");
        String parking_available = (String) getIntent().getSerializableExtra("parking_available");
        String length_hike = (String) getIntent().getSerializableExtra("length_hike");
        String level_hike = (String) getIntent().getSerializableExtra("level_hike");
        String des_hike = (String) getIntent().getSerializableExtra("des_hike");
        back_ic.setOnClickListener(v -> {
            Intent intent = new Intent(ObservationUpdateActivity.this, ObservationActivity.class);
            intent.putExtra("hike_id", hikeId);
            intent.putExtra("hike_name", hike_name);
            intent.putExtra("hike_location", hike_location);
            intent.putExtra("hike_date", hike_date);
            intent.putExtra("parking_available", parking_available);
            intent.putExtra("length_hike", length_hike);
            intent.putExtra("level_hike", level_hike);
            intent.putExtra("des_hike", des_hike);
            startActivity(intent);
        });
        updateObs.setOnClickListener(v -> {
            if(isValidAddHikeDetails()){
                alertDialog.setTitle("Confirmation to Update")
                        .setMessage("Name: " + nameObs.getText().toString().trim() +"\n"+
                                "Time: "+ timeObs.getText().toString().trim() +"\n"+
                                "Comments: "+ commentsObs.getText().toString().trim())
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updateObservation();
                                startActivity(new Intent(ObservationUpdateActivity.this, ObservationActivity.class));
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

    public void getAndSetIntentData() throws ParseException {
        if (getIntent().hasExtra("obs_name") &&
                getIntent().hasExtra("obs_time") &&
                getIntent().hasExtra("obs_comment")){
            nameObs.setText((String) getIntent().getSerializableExtra("obs_name"));
            timeObs.setText((String) getIntent().getSerializableExtra("obs_time"));
            commentsObs.setText((String) getIntent().getSerializableExtra("obs_comment"));
        }
    }

    private Boolean isValidAddHikeDetails(){
        if(nameObs.getText().toString().trim().isEmpty()){
            showToast("Enter name of observation");
            return false;
        }else if(commentsObs.getText().toString().trim().isEmpty()){
            showToast("Enter comments of observation");
            return false;
        }
        return true;
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    public void openTimePiker(View view) {
        timePickerDialog.show();
    }

}