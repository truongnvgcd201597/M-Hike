package com.example.project_mobile.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.project_mobile.R;
import com.example.project_mobile.database.MyDatabaseHelper;
import com.example.project_mobile.models.Hike;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Calendar;

public class AddHikeActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private DatePickerDialog datePickerDialog;
    private Button dateButton, save_hike;
    private Spinner spinner_level;
    private EditText name_hike, location_hike, des_hike, length_hike;
    private RadioGroup radioGroup;
    private RadioButton radio_yes, radio_no;
    private AlertDialog.Builder alertDialog;

    private static final String NAME_HINT = "Enter name of hike";
    private static final String LOCATION_HINT = "Enter location of hike";
    private static final String PARKING_HINT = "Please tick parking available";
    private static final String LENGTH_HINT = "Enter length of hike";
    private static final String DESCRIPTION_HINT = "Enter description of hike";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hike);
        setInit();
        setListener();
        setNavigationView();
    }

    private void setInit(){
        save_hike = findViewById(R.id.save_hike);
        name_hike = findViewById(R.id.hike_name_field);
        location_hike = findViewById(R.id.hike_location_field);
        des_hike = findViewById(R.id.hike_description_field);
        length_hike = findViewById(R.id.length_hike_field);

        radioGroup = findViewById(R.id.radioGroup);
        radio_yes = findViewById(R.id.yes_option);
        radio_no = findViewById(R.id.no_option);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.add_item);
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getDate());

        alertDialog = new AlertDialog.Builder(this);

        spinner_level = findViewById(R.id.hike_difficulty_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.hike_level, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinner_level.setAdapter(adapter);
    }

    private void setListener(){
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, year, month, day) -> {
            month = month + 1;
            String dateStr = dateToString(day, month, year);
            dateButton.setText(dateStr);
        };
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this, style,dateSetListener,year,month,day);

        save_hike.setOnClickListener(v -> {
            if(isValidAddHikeDetails()){
                int requireGroup = radioGroup.getCheckedRadioButtonId();
                RadioButton radioGroup = findViewById(requireGroup);
                alertDialog.setTitle("Confirmation")
                        .setMessage("Name: " + name_hike.getText().toString().trim() +"\n"+
                                "Location: "+ location_hike.getText().toString().trim()+"\n"+
                                "Date: "+ dateButton.getText().toString().trim()+"\n"+
                                "Parking available: "+ radioGroup.getText().toString().trim()+"\n"+
                                "Length: "+ length_hike.getText().toString().trim()+"\n"+
                                "Level: "+ spinner_level.getSelectedItem().toString().trim()+"\n"+
                                "Description: "+ des_hike.getText().toString().trim())
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                addNewHike();
                                startActivity(new Intent(AddHikeActivity.this, MainActivity.class));
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

    private void addNewHike(){
        MyDatabaseHelper myDB = new MyDatabaseHelper(AddHikeActivity.this);
        Hike hike = new Hike();
        int requireGroup = radioGroup.getCheckedRadioButtonId();
        RadioButton radioGroup = findViewById(requireGroup);
        hike.setHikeName(name_hike.getText().toString().trim());
        hike.setHikeLocation(location_hike.getText().toString().trim());
        hike.setParkingAvailable(radioGroup.getText().toString().trim());
        hike.setDateHike(dateButton.getText().toString().trim());
        hike.setHikeLength(length_hike.getText().toString().trim());
        hike.setHikeLevel(spinner_level.getSelectedItem().toString().trim());
        hike.setHikeDescription(des_hike.getText().toString().trim());
        myDB.addNewHike(hike);
    }

    private void setNavigationView(){
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.add_item:
                        return true;
                    case R.id.home_page:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        return true;
                    case R.id.search_item:
                        startActivity(new Intent(getApplicationContext(), SearchHikeActivity.class));
                        return true;
                }
                return false;
            }
        });
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private String dateToString(int day, int month, int year) {
        return String.format("%02d/%02d/%d", month, day, year);
    }

    private String getDate() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        month = month + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        return dateToString(day,month,year);
    }

    public void openDatePiker(View view) {
        datePickerDialog.show();
    }

    private Boolean isValidAddHikeDetails(){
        if(name_hike.getText().toString().trim().isEmpty()){
            showToast(NAME_HINT);
            return false;
        }else if(location_hike.getText().toString().trim().isEmpty()){
            showToast(LOCATION_HINT);
            return false;
        }
        else if(radioGroup.getCheckedRadioButtonId() == -1){
            showToast(PARKING_HINT);
            return false;
        }
        else if(length_hike.getText().toString().trim().isEmpty()){
            showToast(LENGTH_HINT);
            return false;
        }
        else if(des_hike.getText().toString().trim().isEmpty()){
            showToast(DESCRIPTION_HINT);
            return false;
        }
        return true;
    }
}