package com.cloud.appsistant;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;


public class CreateCardActivity extends AppCompatActivity implements View.OnClickListener {

    Button addCardButton;
    EditText nameText;
    EditText surnameText;
    EditText phoneText;
    EditText emailText;
    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);

        db = FirebaseFirestore.getInstance();

        nameText = findViewById(R.id.nameText);
        surnameText = findViewById(R.id.surnameText);
        phoneText = findViewById(R.id.phoneText);
        emailText = findViewById(R.id.emailText);

        addCardButton = findViewById(R.id.addButton);
        addCardButton.setOnClickListener(this);

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
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.addButton: {
                AddCard();
            }
        }
    }

    private void AddCard() {
        String name = nameText.getText().toString().trim();
        String surname = surnameText.getText().toString().trim();
        String phone = phoneText.getText().toString().trim();
        String email = emailText.getText().toString().trim();

        Map<String, Object> cardData = new HashMap<>();
        cardData.put(Main.nameKey, name);
        cardData.put(Main.surnameKey, surname);
        cardData.put(Main.phoneKey, phone);
        cardData.put(Main.emailKey, email);

        if(TextUtils.isEmpty(name) && TextUtils.isEmpty(surname) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(email)) {
            Toast.makeText(this, "You have to fill something", Toast.LENGTH_LONG).show();
        } else {
            db.collection(Main.usersKey).document(Main.uid).collection(Main.cardsKey).add(cardData);
            ViewPagerAdapter.currentF.updateView();
            finish();
        }
    }
}
