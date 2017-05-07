package com.lolapp.magnus.lolapp;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.UiThread;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.CountDownLatch;

import static android.R.attr.bitmap;

public class GameActivity extends AppCompatActivity {

    private Button guessChamp, skipChamp;
    private static  ImageView champPic;
    private EditText champText;
    private String key, username;
    RestConnection connection;
    static Bitmap draw;
    public CountDownLatch latch;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        latch = new CountDownLatch(1);

        Bundle b = getIntent().getExtras();
        username = b.getString("Brugernavn");
        key = b.getString("gameKey");
        Toast.makeText(getApplicationContext(), key, Toast.LENGTH_LONG).show();

        guessChamp = (Button) findViewById(R.id.guessButton);
        skipChamp = (Button) findViewById(R.id.skipButton);
        champPic = (ImageView) findViewById(R.id.champPicId);
        champText = (EditText) findViewById(R.id.guessText);

        champText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                champText.setText("");
            }
        });

        connection = new RestConnection();



        setImage();


        guessChamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String guess = champText.getText().toString();
                if (guess.equals("")){
                    guess = "noguess";
                }
                champText.setText("");
                try {

                    if(connection.guessChamp(guess, username)){
                        Toast.makeText(getApplicationContext(), "Korrekt!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Forkert!", Toast.LENGTH_SHORT).show();
                    }


                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                try {
                    checkGameDone();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                setImage();
            }
        });

        skipChamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                champText.setText("");
                try {
                    Toast.makeText(getApplicationContext(), "Finder næste champion...", Toast.LENGTH_SHORT).show();
                    connection.skip(username);
                    checkGameDone();
                    setImage();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setImage(){

        AsyncTask task = new AsyncTask() {
            @Override
            protected Object doInBackground(Object[] params) {
                try {
                    URL url = new URL(connection.getChampPic(username));
                    Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                    draw = bmp;

                } catch (MalformedURLException e) {
                    e.printStackTrace();

                } catch (IOException e) {



                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                latch.countDown();
                return null;
            }

            @Override
            protected void onPostExecute(Object o) {
                latch.countDown();
                super.onPostExecute(o);
                try {
                    latch.await();
                    latch = new CountDownLatch(1);
                    champPic.setImageBitmap(draw);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }.execute();



    }

    private void checkGameDone() throws InterruptedException {

        boolean done = connection.playerDoneGuessing(key, username);
       // Toast.makeText(getApplicationContext(), "Not done (not in if)", Toast.LENGTH_SHORT).show();
        if(done){
            Toast.makeText(getApplicationContext(), "done", Toast.LENGTH_SHORT).show();
            String doneText = "";
            doneText += "Game Over! \n";
            doneText += ("Score : "+ connection.getScore(key, username) + "\n");
            doneText += ("Time : "+ connection.getTimeTaken(key, username));

            AlertDialog.Builder dialog = new AlertDialog.Builder(GameActivity.this);
            dialog.setMessage(doneText);
            dialog.setPositiveButton("Main Menu", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent i = new Intent(getApplicationContext(), LoginScreen.class);
                    startActivity(i);
                }
            });
            dialog.create().show();

        }

    }


}
