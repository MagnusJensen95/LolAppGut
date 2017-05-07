package com.lolapp.magnus.lolapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JoinGameActivity extends AppCompatActivity {

    ListView list;
    RestConnection connection;
    ArrayList<String> games;
    JSONArray array = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_game);
        games = new ArrayList<>();
        list = (ListView) findViewById(R.id.gamesList);


        Bundle b = getIntent().getExtras();

        final String username = b.getString("Brugernavn");

        connection = new RestConnection();
    try {
         array = new JSONArray(connection.findGames());
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
                Intent i = new Intent(getApplicationContext(), LobbyActivity.class);
                i.putExtra("Created", false);
                i.putExtra("gameKey", games.get(position));
                i.putExtra("Brugernavn", username);
                startActivity(i);
            }
        });






    }
}
