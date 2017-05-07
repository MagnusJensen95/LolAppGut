package com.lolapp.magnus.lolapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

public class LoginScreen extends AppCompatActivity {
    EditText password,email;
    AppCompatButton button;
    RestConnection conn;
    private boolean correctCredentials = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        conn = new RestConnection();
        email = (EditText)findViewById(R.id.input_email);
        email.setHint("Brugernavn");

        password = (EditText)findViewById(R.id.input_password);
        password.setHint("Password");


        button = (AppCompatButton) findViewById(R.id.btn_login);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    correctCredentials = conn.hentBruger(email.getText().toString(), password.getText().toString());
                }
                catch (InterruptedException e){
                    e.printStackTrace();

                }
                if(correctCredentials){

                    Intent intent = new Intent(LoginScreen.this, MainMenuActivity.class);
                    intent.putExtra("Brugernavn",  email.getText().toString());

                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Forkert Brugernavn eller Adgangskode", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }
}