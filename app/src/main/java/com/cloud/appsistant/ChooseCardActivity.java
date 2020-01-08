package com.cloud.appsistant;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChooseCardActivity extends AppCompatActivity {

    private List<Card> cards = new ArrayList<>();
    private ListView listView;
    private String chosenDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_card);

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
        chosenDate = intent.getExtras().getString("date");

        cards = ViewPagerAdapter.currentF.cardList;

        listView = findViewById(R.id.listChooseCard);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ChooseCardActivity.this, ChooseTimeActivity.class);
                Bundle extra = new Bundle();
                extra.putString("cardId", cards.get(position).getCardId());
                extra.putString("date", chosenDate);
                intent.putExtras(extra);
                startActivity(intent);
                finish();
            }
        });

        CardsList adapter = new CardsList(this, cards);
        listView.setAdapter(adapter);
    }
}
