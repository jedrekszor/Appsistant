package com.cloud.appsistant;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class CardsList extends ArrayAdapter<Card> {
    private Activity context;
    private List<Card> cardsList;

    public CardsList(Activity context, List<Card> cardsList) {
        super(context, R.layout.list_layout, cardsList);
        this.context = context;
        this.cardsList = cardsList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        View listViewItem = inflater.inflate(R.layout.list_layout, null, true);
        TextView name = listViewItem.findViewById(R.id.textViewName);
        TextView surname = listViewItem.findViewById(R.id.textViewSurname);
        TextView phone = listViewItem.findViewById(R.id.textViewPhone);
        TextView email = listViewItem.findViewById(R.id.textViewEmail);

        Card card = cardsList.get(position);

        name.setText(card.getName());
        surname.setText(card.getSurname());
        phone.setText(card.getPhone());
        email.setText(card.getEmail());

        return listViewItem;
    }
}
