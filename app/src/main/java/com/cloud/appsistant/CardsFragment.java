package com.cloud.appsistant;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CardsFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton createCard;
    DatabaseReference databaseCards;
    ListView listViewCards;
    List<Card> cardList;

    public CardsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_cards, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        createCard = getView().findViewById(R.id.addCard);
        createCard.setOnClickListener(this);

        cardList = new ArrayList<Card>();
        listViewCards = getView().findViewById(R.id.listViewCards);
        databaseCards = FirebaseDatabase.getInstance().getReference("cards");
    }

    @Override
    public void onStart() {
        super.onStart();
        databaseCards.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                cardList.clear();

                for(DataSnapshot cardSnapshot: dataSnapshot.getChildren()) {
                    Card card = cardSnapshot.getValue(Card.class);

                    cardList.add(card);
                }

                CardsList adapter = new CardsList(getActivity(), cardList);
                listViewCards.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addCard: {
                PopUpForm();
                break;
            }
        }
    }

    private void PopUpForm() {
        Intent i = new Intent(getActivity(), CreateCardActivity.class);
        startActivity(i);
    }
}
