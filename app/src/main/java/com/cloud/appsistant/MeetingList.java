package com.cloud.appsistant;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class MeetingList extends ArrayAdapter<Meeting> {
    private Activity context;
    private List<Meeting> meetingsList;

    public MeetingList(Activity context, List<Meeting> meetingsList) {
        super(context, R.layout.meeting_layout, meetingsList);
        this.context = context;
        this.meetingsList = meetingsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.meeting_layout, null, true);
        TextView name = listViewItem.findViewById(R.id.textViewName);
        TextView surname = listViewItem.findViewById(R.id.textViewSurname);
        TextView phone = listViewItem.findViewById(R.id.textViewPhone);
        TextView email = listViewItem.findViewById(R.id.textViewEmail);
        TextView time = listViewItem.findViewById(R.id.textViewTime);

        Meeting meeting = meetingsList.get(position);

        time.setText(meeting.getTime());
        name.setText(meeting.getCard().getName());
        surname.setText(meeting.getCard().getSurname());
        phone.setText(meeting.getCard().getPhone());
        email.setText(meeting.getCard().getEmail());


        return listViewItem;
    }
}
