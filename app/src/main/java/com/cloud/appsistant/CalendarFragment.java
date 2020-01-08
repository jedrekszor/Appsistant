package com.cloud.appsistant;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.util.Calendar;

import android.widget.ListView;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment implements View.OnClickListener {

    public static CompactCalendarView calendar;
    private FloatingActionButton createMeeting;
    private String chosenDate;
    public List<Meeting> meetingsList;
    ListView listViewMeetings;
    FirebaseFirestore db;
    CollectionReference cardsRef;
    Card tempCard;
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat monthFormatText = new SimpleDateFormat("MMMM");
    SimpleDateFormat monthFormat = new SimpleDateFormat("MM");
    TextView monthName;
    String currentMonthText;
    String currentMonth;

    public CalendarFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        createMeeting = getView().findViewById(R.id.addMeeting);
        createMeeting.setOnClickListener(this);

        calendar = getView().findViewById(R.id.calendarView);
        monthName = getView().findViewById(R.id.month);


        meetingsList = new ArrayList<>();
        listViewMeetings = getView().findViewById(R.id.listMeetings);
        db = FirebaseFirestore.getInstance();
        cardsRef = db.collection(Main.usersKey).document(Main.uid).collection(Main.cardsKey);

        calendar.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                chosenDate = sdf.format(dateClicked);
                updateView();
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                monthName.setText(monthFormatText.format(firstDayOfNewMonth));
                currentMonthText = monthFormatText.format(firstDayOfNewMonth);
                currentMonth = monthFormat.format(firstDayOfNewMonth);
            }
        });
//        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//
//            }
//        });
    }

    @Override
    public void onStart() {
        super.onStart();
        chosenDate = sdf.format(Calendar.getInstance().getTime());
        monthName.setText(monthFormatText.format(Calendar.getInstance().getTime()));
        currentMonthText = monthFormatText.format(Calendar.getInstance().getTime());
        currentMonth = monthFormat.format(Calendar.getInstance().getTime());
        checkForMonthlyEvents();
        updateView();
    }

    public void updateView() {
        meetingsList.clear();
        db.collection(Main.usersKey).document(Main.uid).collection(Main.meetingsKey)
                .whereEqualTo(Main.dateKey, chosenDate)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (final QueryDocumentSnapshot document : task.getResult()) {

                                String id = document.get(Main.cardIdKey).toString();
                                Query query = cardsRef.whereEqualTo(FieldPath.documentId(), id);
                                query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                        if (task.isSuccessful()) {
                                            for (QueryDocumentSnapshot doc : task.getResult()) {

                                                Meeting meeting = new Meeting(document.getId(), document.get(Main.dateKey).toString(), document.get(Main.timeKey).toString(), new Card(doc.getId(), doc.get(Main.nameKey).toString(), doc.get(Main.surnameKey).toString(), doc.get(Main.phoneKey).toString(), doc.get(Main.emailKey).toString()));
                                                if (meeting.getTime().substring(3).equals("0"))
                                                    meeting.setTime(meeting.getTime() + "0");
                                                meetingsList.add(meeting);
                                                MeetingList adapter = new MeetingList(getActivity(), meetingsList);
                                                listViewMeetings.setAdapter(adapter);
                                            }
                                        }
                                    }
                                });
                            }

                        }
                    }
                });
        MeetingList adapter = new MeetingList(getActivity(), meetingsList);
        listViewMeetings.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addMeeting: {
                Intent intent = new Intent(this.getActivity(), ChooseCardActivity.class);
                intent.putExtra("date", chosenDate);
                startActivity(intent);
                break;
            }
        }
    }

    public static boolean addEvent(String date) throws ParseException {
        Date temp = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        if (temp.before(Calendar.getInstance().getTime()))
            return false;
        Event event = new Event(Color.GREEN, temp.getTime());
        calendar.addEvent(event);
        return true;
    }

    void checkForMonthlyEvents() {
        db.collection(Main.usersKey).document(Main.uid).collection(Main.meetingsKey)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (final QueryDocumentSnapshot document : task.getResult()) {
                                String date = document.get(Main.dateKey).toString();
                                try {
                                    if (!addEvent(date))
                                    {
                                        String id = document.getId();
                                        db.collection(Main.usersKey).document(Main.uid).collection(Main.meetingsKey).document(id).delete();
                                    }
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
    }
}
