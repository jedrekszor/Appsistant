package com.cloud.appsistant;

import androidx.appcompat.app.AppCompatActivity;

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

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CreateCardActivity extends Activity implements View.OnClickListener {

    Button addCardButton;
    EditText nameText;
    EditText surnameText;
    EditText phoneText;
    EditText emailText;

    DatabaseReference databaseCards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_card);


        databaseCards = FirebaseDatabase.getInstance().getReference("cards");

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

        if(TextUtils.isEmpty(name) && TextUtils.isEmpty(surname) && TextUtils.isEmpty(phone) && TextUtils.isEmpty(email)) {
            Toast.makeText(this, "You have to fill something", Toast.LENGTH_LONG).show();
        } else {
            String id = databaseCards.push().getKey();
            Card card = new Card(id, name, surname, phone, email);
            databaseCards.child(id).setValue(card);
            Toast.makeText(this, "Card added", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}
