package com.example.project_mobile.activities.observation;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.project_mobile.R;
import com.example.project_mobile.activities.DetailHikeActivity;
import com.example.project_mobile.activities.MainActivity;
import com.example.project_mobile.activities.UpdateHikeActivity;
import com.example.project_mobile.database.MyDatabaseHelper;
import com.example.project_mobile.models.Hike;
import com.example.project_mobile.models.Observation;

import java.text.ParseException;
import java.util.Calendar;

public class UpdateObservationActivity extends AppCompatActivity {
    private ImageView back_ic;
    private TextView title_actionbar;
    private Button update_obs;
    private EditText name_obs, comments_obs;
    private Button time_obs;
    private AlertDialog.Builder alertDialog;
    private String hike_id, date_hike;
    private TimePickerDialog timePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_observation);
        hike_id = (String) getIntent().getSerializableExtra("hike_id");
        date_hike = (String) getIntent().getSerializableExtra("hike_date");
        setInit();
        try {
            getAndSetIntentData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setListeners();
    }

    private void updateObservation(){
        MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateObservationActivity.this);
        Observation observation = new Observation();
        observation.setHikeId(Integer.valueOf(hike_id));
        observation.setObsId(Integer.valueOf((String) getIntent().getSerializableExtra("obs_id")));
        observation.setObsName(name_obs.getText().toString().trim());
        observation.setObsTime(time_obs.getText().toString().trim());
        observation.setObsComment(comments_obs.getText().toString().trim());
        myDB.updateObservation(observation);
    }

    private void setInit(){
        back_ic = findViewById(R.id.back_icon_button);
        title_actionbar = findViewById(R.id.observation_title);
        title_actionbar.setText("Update Observation");
        name_obs = findViewById(R.id.name_obs_update);
        comments_obs = findViewById(R.id.comments_obs_update);
        update_obs = findViewById(R.id.update_obs);
        time_obs = findViewById(R.id.datePickerButton);
        alertDialog = new AlertDialog.Builder(this);
    }

    private void setListeners(){
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hours, int minutes) {
                time_obs.setText((hours >= 10 ? hours : ("0" + String.valueOf(hours))) + ":" + (minutes >= 10 ? minutes : ("0" + String.valueOf(minutes))) + " - " + date_hike);
            }
        };
        Calendar cal = Calendar.getInstance();
        int hours = cal.get(Calendar.HOUR_OF_DAY);
        int minutes = cal.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(this,timeSetListener,hours,minutes,true);

        String name = (String) getIntent().getSerializableExtra("hike_name");
        String location = (String) getIntent().getSerializableExtra("hike_location");
        String date = (String) getIntent().getSerializableExtra("hike_date");
        String parking_available = (String) getIntent().getSerializableExtra("parking_available");
        String length = (String) getIntent().getSerializableExtra("length_hike");
        String level = (String) getIntent().getSerializableExtra("level_hike");
        String des = (String) getIntent().getSerializableExtra("des_hike");
        back_ic.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateObservationActivity.this, ObservationActivity.class);
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
        update_obs.setOnClickListener(v -> {
            if(isValidAddHikeDetails()){
                alertDialog.setTitle("Confirmation to Update")
                        .setMessage("Name: " + name_obs.getText().toString().trim() +"\n"+
                                "Time: "+ time_obs.getText().toString().trim() +"\n"+
                                "Comments: "+ comments_obs.getText().toString().trim())
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                updateObservation();
                                startActivity(new Intent(UpdateObservationActivity.this, ObservationActivity.class));
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
            name_obs.setText((String) getIntent().getSerializableExtra("obs_name"));
            time_obs.setText((String) getIntent().getSerializableExtra("obs_time"));
            comments_obs.setText((String) getIntent().getSerializableExtra("obs_comment"));
        }
    }

    private Boolean isValidAddHikeDetails(){
        if(name_obs.getText().toString().trim().isEmpty()){
            showToast("Enter name of observation");
            return false;
        }else if(comments_obs.getText().toString().trim().isEmpty()){
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