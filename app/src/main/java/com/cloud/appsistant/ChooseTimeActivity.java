package com.cloud.appsistant;

import androidx.appcompat.app.AppCompatActivity;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TimePicker;
import android.widget.Toast;

import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

public class ChooseTimeActivity extends AppCompatActivity {

    TimePickerDialog timePickerDialog;
    private String chosenTime;
    private String chosenDate;
    private String cardId;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_time);

        db = FirebaseFirestore.getInstance();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        chosenDate = extras.getString("date");
        cardId = extras.getString("cardId");


        timePickerDialog = new TimePickerDialog(ChooseTimeActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                chosenTime = hourOfDay + ":" + minute;
                try {
                    AddMeeting();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                finish();
            }
        }, 0, 0, true);
        timePickerDialog.show();
    }

    private void AddMeeting() throws ParseException {
        Map<String, Object> meetingData = new HashMap<>();
        meetingData.put(Main.cardIdKey, cardId);
        meetingData.put(Main.dateKey, chosenDate);
        meetingData.put(Main.timeKey, chosenTime);

        if (CalendarFragment.addEvent(chosenDate)) {
            db.collection(Main.usersKey).document(Main.uid).collection(Main.meetingsKey).add(meetingData);
        } else {
            Toast.makeText(this, "Cannot schedule a meeting in the past", Toast.LENGTH_LONG).show();
        }



        ViewPagerAdapter.currentCalendar.updateView();
    }
}
