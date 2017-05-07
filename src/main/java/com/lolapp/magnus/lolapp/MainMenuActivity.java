package com.lolapp.magnus.lolapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainMenuActivity extends AppCompatActivity {

    private Button createGameButton, joinGameButton;
    private String username;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Bundle b = getIntent().getExtras();

        username = b.getString("Brugernavn");

        createGameButton = (Button) findViewById(R.id.createGameButton);
        joinGameButton = (Button) findViewById(R.id.joinGameButton);

        joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), JoinGameActivity.class);
                i.putExtra("Brugernavn", username);
                startActivity(i);

            }
        });

        createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(getApplicationContext(), LobbyActivity.class);
                i.putExtra("Created", true);
                i.putExtra("Brugernavn", username);
                startActivity(i);
            }
        });





    }
}
