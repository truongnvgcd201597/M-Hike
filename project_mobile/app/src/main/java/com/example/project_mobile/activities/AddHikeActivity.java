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
        name_hike = findViewById(R.id.name_hike_detail);
        location_hike = findViewById(R.id.location_hike);
        des_hike = findViewById(R.id.des_hike);
        length_hike = findViewById(R.id.length_hike);

        radioGroup = findViewById(R.id.radioGroup);
        radio_yes = findViewById(R.id.radio_yes);
        radio_no = findViewById(R.id.radio_no);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.add_item);
        dateButton = findViewById(R.id.datePickerButton);
        dateButton = findViewById(R.id.datePickerButton);
        dateButton.setText(getDate());

        alertDialog = new AlertDialog.Builder(this);

        spinner_level = findViewById(R.id.spinner_level_hike);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.level_hike, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
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
        MyDatabaseHelper myDB = new MyDatabaseHelper(AddHikeActivity    .this);
        Hike hike = new Hike();
        int requireGroup = radioGroup.getCheckedRadioButtonId();
        RadioButton radioGroup = findViewById(requireGroup);
        hike.setHike_name(name_hike.getText().toString().trim());
        hike.setLocation_hike(location_hike.getText().toString().trim());
        hike.setParking_available(radioGroup.getText().toString().trim());
        hike.setDate_hike(dateButton.getText().toString().trim());
        hike.setHike_length(length_hike.getText().toString().trim());
        hike.setHike_level(spinner_level.getSelectedItem().toString().trim());
        hike.setHike_description(des_hike.getText().toString().trim());
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
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.search_item:
                        startActivity(new Intent(getApplicationContext(), SearchHikeActivity.class));
                        overridePendingTransition(0,0);
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
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return dateToString(day,month,year);
    }

    public void openDatePiker(View view) {
        datePickerDialog.show();
    }

    private Boolean isValidAddHikeDetails(){
        if(name_hike.getText().toString().trim().isEmpty()){
            showToast("Enter name of hike");
            return false;
        }else if(location_hike.getText().toString().trim().isEmpty()){
            showToast("Enter location of hike");
            return false;
        }
        else if(radioGroup.getCheckedRadioButtonId() == -1){
            showToast("Please tick parking available");
            return false;
        }
        else if(length_hike.getText().toString().trim().isEmpty()){
            showToast("Enter length of hike");
            return false;
        }
        else if(des_hike.getText().toString().trim().isEmpty()){
            showToast("Enter description of hike");
            return false;
        }
        return true;
    }
}