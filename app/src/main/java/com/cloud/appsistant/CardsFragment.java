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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CardsFragment extends Fragment implements View.OnClickListener {

    private FloatingActionButton createCard;
    ListView listViewCards;
    public List<Card> cardList;
    FirebaseFirestore db;

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

        cardList = new ArrayList<>();
        listViewCards = getView().findViewById(R.id.listViewCards);
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public void onStart() {
        super.onStart();
        updateView();

    }

    public void updateView() {
        db.collection(Main.usersKey).document(Main.uid).collection(Main.cardsKey)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()) {
                            cardList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Card card = new Card(document.getId(), document.get(Main.nameKey).toString(), document.get(Main.surnameKey).toString(), document.get(Main.phoneKey).toString(), document.get(Main.emailKey).toString());
                                cardList.add(card);
                            }
                            CardsList adapter = new CardsList(getActivity(), cardList);
                            listViewCards.setAdapter(adapter);
                        }
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
