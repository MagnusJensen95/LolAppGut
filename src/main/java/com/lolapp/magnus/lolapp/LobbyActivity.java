package com.lolapp.magnus.lolapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class LobbyActivity extends AppCompatActivity {

    private TextView playersText;
    private Button startGameButton;

    private RestConnection connection;
    String key, username;

    boolean created = false;
    static AsyncTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_lobby);


        startGameButton = (Button) findViewById(R.id.startGameButton);


       // startGameButton.setClickable(false);

        playersText = (TextView) findViewById(R.id.playersText);

        connection = new RestConnection();

        Bundle b = getIntent().getExtras();
        key = b.getString("gameKey");
        username = b.getString("Brugernavn");
        created = b.getBoolean("Created");

        //Created Game
        if (created) {

            try {
                key = connection.createGame(username);
                connection.joinGame(key, username);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
             task = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {

                    int count = 0;

                    while (!task.isCancelled()) {
                        try {
                            Thread.currentThread().sleep(2000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        updateText(key);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            count++;

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);

                }


            }.execute();


        }

        //Joined game
        else {
            try {
                connection.joinGame(key, username);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startGameButton.setVisibility(View.INVISIBLE);
             task = new AsyncTask() {
                @Override
                protected Object doInBackground(Object[] params) {

                    int count = 0;
                    while (!task.isCancelled()) {
                        try {
                            Thread.currentThread().sleep(2000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        updateText(key);

                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                            count++;

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                    try {
                        if(connection.isGameStarted(username)){
                            Intent i = new Intent(getApplicationContext(), GameActivity.class);
                            i.putExtra("gameKey", key);
                            i.putExtra("Brugernavn", username);
                            startActivity(i);
                            task.cancel(true);


                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(Object o) {
                    super.onPostExecute(o);

                }


            }.execute();



        }

        startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), GameActivity.class);
                try {
                    connection.startGame(username);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                task.cancel(true);

                i.putExtra("gameKey", key);
                i.putExtra("Brugernavn", username);
                startActivity(i);

            }
        });

    }
    private void updateText(String gameKey) throws InterruptedException {

        String players = "";
        ArrayList<String> list = connection.getUsernames(gameKey);

        if (list.size() > 1) {
            startGameButton.setClickable(true);


        } else {
           // startGameButton.setClickable(false);
        }

        for (int i = 0; i < list.size(); i++) {

            players += list.get(i);
            players += "\n";

        }
        playersText.setText(players);

    }

}