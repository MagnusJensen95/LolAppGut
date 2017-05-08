package com.lolapp.magnus.lolapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class ScoresActivity extends AppCompatActivity {

    ListView list;
    RestConnection connection;
    JSONArray array = null;
    ArrayList<String> games;
    String username;
    Button back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scores);

        games = new ArrayList<>();
        list = (ListView) findViewById(R.id.scoresList);
        Bundle b = getIntent().getExtras();
        username = b.getString("Brugernavn");
        back = (Button) findViewById(R.id.backButton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
                i.putExtra("Brugernavn", username);
                startActivity(i);
            }
        });

        connection = new RestConnection();
        try {
            array = new JSONArray(connection.findplayersgames(username));
            for(int i = 0; i < array.length(); i++){

                games.add(array.get(i).toString());
            }

        }
        catch (InterruptedException e){
            e.printStackTrace();
            e.printStackTrace();
        } catch (JSONException e) {
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getApplicationContext(),   android.R.layout.simple_list_item_1,  games);

        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder dialog = new AlertDialog.Builder(ScoresActivity.this);
                try {
                    String winner = connection.getWinner(games.get(position));
                    if (winner.equals("")) {
                        winner = "Ingen vinder i dette spil";
                    }
                    dialog.setMessage("Vinder: " + winner);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                dialog.create().show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
        i.putExtra("Brugernavn", username);
        startActivity(i);
    }
}
