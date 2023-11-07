package com.example.project_mobile.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project_mobile.R;
import com.example.project_mobile.database.MyDatabaseHelper;
import com.example.project_mobile.models.Hike;

import java.text.ParseException;
import java.util.Calendar;

public class UpdateHikeActivity extends AppCompatActivity {
    private DatePickerDialog datePickerDialog;
    private Button dateButton, update_hike;
    private Spinner spinner_level;
    private EditText name_hike, location_hike, des_hike, length_hike;
    private RadioGroup radioGroup;
    private RadioButton radio_yes, radio_no;
    private AlertDialog.Builder alertDialog;
    private ImageView back_ic;
    private TextView back_txt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_hike);
        setInit();
        try {
            getAndSetIntentData();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        initDatePiker();
        setListener();
    }

    private void updateHike(){
        MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateHikeActivity.this);
        Hike hike = new Hike();
        int requireGroup = radioGroup.getCheckedRadioButtonId();
        RadioButton radioGroup = findViewById(requireGroup);
        hike.setHike_id(Integer.parseInt(getIntent().getStringExtra("hike_id")));
        hike.setHike_name(name_hike.getText().toString().trim());
        hike.setLocation_hike(location_hike.getText().toString().trim());
        hike.setParking_available(radioGroup.getText().toString().trim());
        hike.setDate_hike(dateButton.getText().toString().trim());
        hike.setHike_length(length_hike.getText().toString().trim());
        hike.setHike_level(spinner_level.getSelectedItem().toString().trim());
        hike.setHike_description(des_hike.getText().toString().trim());
        myDB.updateHike(hike);
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

    private void showToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void setInit(){
        dateButton = findViewById(R.id.datePickerButton);
        name_hike = findViewById(R.id.name_hike_detail);
        location_hike = findViewById(R.id.location_hike);
        des_hike = findViewById(R.id.des_hike);
        length_hike = findViewById(R.id.length_hike);
        update_hike = findViewById(R.id.update_hike);
        radioGroup = findViewById(R.id.radioGroup);
        radio_yes = findViewById(R.id.radio_yes);
        radio_no = findViewById(R.id.radio_no);
        spinner_level = findViewById(R.id.spinner_level_hike);
        alertDialog = new AlertDialog.Builder(this);
        back_ic = findViewById(R.id.back_icon_button);
        back_txt = findViewById(R.id.back_txt);
    }

    private void setListener(){
        back_txt.setOnClickListener(v -> {
            startActivity(new Intent(UpdateHikeActivity.this, MainActivity.class));
        });
        back_ic.setOnClickListener(v -> {
            startActivity(new Intent(UpdateHikeActivity.this, MainActivity.class));
        });
        update_hike.setOnClickListener(v -> {
            if(isValidAddHikeDetails()){
                int requireGroup = radioGroup.getCheckedRadioButtonId();
                RadioButton radioGroup = findViewById(requireGroup);
                alertDialog.setTitle("Confirmation to Update")
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
                                updateHike();
                                startActivity(new Intent(UpdateHikeActivity.this, MainActivity.class));
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
        if (getIntent().hasExtra("hike_name") &&
                getIntent().hasExtra("hike_location") &&
                getIntent().hasExtra("hike_date") &&
                getIntent().hasExtra("parking_available") &&
                getIntent().hasExtra("length_hike")&&
                getIntent().hasExtra("level_hike")&&
                getIntent().hasExtra("des_hike")){
            name_hike.setText((String) getIntent().getSerializableExtra("hike_name"));
            location_hike.setText((String) getIntent().getSerializableExtra("hike_location"));
            dateButton.setText((String) getIntent().getSerializableExtra("hike_date"));
            String radio = getIntent().getStringExtra("parking_available");
            if (radio.equals("Yes"))
            {
                radio_yes.setChecked(true);
                radio_no.setChecked(false);
            }
            else {
                radio_yes.setChecked(false);
                radio_no.setChecked(true);
            }
            length_hike.setText((String) getIntent().getSerializableExtra("length_hike"));
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.level_hike, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            adapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
            spinner_level.setAdapter(adapter);
            String selection = (String) getIntent().getSerializableExtra("level_hike");
            int spinnerPosition = adapter.getPosition(selection);
            spinner_level.setSelection(spinnerPosition);
            des_hike.setText((String) getIntent().getSerializableExtra("des_hike"));
        }
    }
    private void initDatePiker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = dateToString(day, month, year);
                dateButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;
        datePickerDialog = new DatePickerDialog(this,style,dateSetListener,year,month,day);

    }
    private String dateToString(int day, int month, int year){
        if(day<10){
            return month + "/0" + day + "/"+ year;
        }else if(month <10){
            return "0" + month+ "/" + day + "/"+ year;
        }else if(day<10 && month <10){
            return "0" + month +"/0" + day + "/"+ year;
        }
        return month + "/" + day + "/"+ year;
    }

    public void openDatePiker(View view) {
        datePickerDialog.show();
    }
}