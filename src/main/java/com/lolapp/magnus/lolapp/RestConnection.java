package com.lolapp.magnus.lolapp;

import android.app.DownloadManager;
import android.app.usage.NetworkStatsManager;
import android.os.AsyncTask;
import android.widget.Toast;


import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Magnus on 03-05-2017.
 */

public class RestConnection {


    String responseValue = "Nothing to show";
    OkHttpClient client;
    Request request;
    Call call;
    static String started = "false";
    static int score = 0;
    static long time;


    public String baseURL = "http://ec2-35-165-42-120.us-west-2.compute.amazonaws.com:8080/LolRest/webresources/lolsoapaccess";


    public CountDownLatch latch;

    public RestConnection() {


        client = new OkHttpClient();
        latch = new CountDownLatch(1);


    }

    public String findGames() throws InterruptedException {

        request = new Request.Builder().url(baseURL + "/findgames").build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                latch.countDown();
                return;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseValue = response.body().string();
                latch.countDown();
            }
        });


        latch.await();
        latch = new CountDownLatch(1);
        return responseValue;
    }

    public boolean hentBruger(String username, String password) throws InterruptedException {

        request = new Request.Builder().url(baseURL + "/hentbruger/" +password+"/"+username).build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                latch.countDown();
                return;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseValue = response.body().string();
                latch.countDown();
            }
        });


        latch.await();
        latch = new CountDownLatch(1);
        if(responseValue.equals("true")){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean guessChamp(String guess, String username) throws InterruptedException {

        request = new Request.Builder().url(baseURL + "/guesschamp/" +guess+"/"+username).build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                latch.countDown();
                return;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseValue = response.body().string();
                latch.countDown();
            }
        });


        latch.await();
        latch = new CountDownLatch(1);
        if(responseValue.equals("true")){
            return true;
        }
        else{
            return false;
        }
    }

    public String createGame(String username) throws InterruptedException {

        request = new Request.Builder().url(baseURL + "/creategame/" +username).build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                latch.countDown();
                return;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseValue = response.body().string();
                latch.countDown();
            }
        });


        latch.await();
        latch = new CountDownLatch(1);
       return responseValue;
    }

    public void skip(String username) throws InterruptedException {

        request = new Request.Builder().url(baseURL + "/skip/" +username).build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                latch.countDown();
                return;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                latch.countDown();
            }
        });


        latch.await();
        latch = new CountDownLatch(1);

    }

    public void joinGame(String gameID, String username) throws InterruptedException {

        request = new Request.Builder().url(baseURL + "/joingame/" +gameID + "/"+username).build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                latch.countDown();
                return;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                latch.countDown();
            }
        });


        latch.await();
        latch = new CountDownLatch(1);

    }

    public String getChampPic(String username) throws InterruptedException {

        request = new Request.Builder().url(baseURL + "/getchamppic/" +username).build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                latch.countDown();
                return;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseValue = response.body().string();
                latch.countDown();
            }
        });


        latch.await();
        latch = new CountDownLatch(1);
        return responseValue;
    }

    public void startGame(String username) throws InterruptedException {

        request = new Request.Builder().url(baseURL + "/startgame/" +username).build();

        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                latch.countDown();
                return;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                responseValue = response.body().string();
                latch.countDown();
            }
        });


        latch.await();
        latch = new CountDownLatch(1);

    }

    public ArrayList<String> getUsernames(String gameID)  throws InterruptedException{
        request = new Request.Builder().url(baseURL + "/getplayers/" +gameID).build();
          final ArrayList<String> players = new ArrayList<>();
       final Gson g = new Gson();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                latch.countDown();
                return;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONArray jsonA  = new JSONArray(response.body().string());
                    for (int i = 0; i < jsonA.length(); i++) {

                        players.add(jsonA.get(i).toString());

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                latch.countDown();
            }
        });

        latch.await();
        latch = new CountDownLatch(1);
        return players;
    }

    public boolean isGameStarted(String username)  throws InterruptedException{
        request = new Request.Builder().url(baseURL + "/isgamestarted/" +username).build();



        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                latch.countDown();
                return;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                    started =  response.body().string();

                latch.countDown();
            }
        });

        latch.await();
        latch = new CountDownLatch(1);
        if(started.equals("false")){
            return false;
        }
        else{
            return true;
        }


    }

    public int getScore(String gameKey, String username)  throws InterruptedException{
        request = new Request.Builder().url(baseURL + "/getscore/" +gameKey+"/"+username).build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                latch.countDown();
                return;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                score = Integer.parseInt(response.body().string());

                latch.countDown();
            }
        });

        latch.await();
        latch = new CountDownLatch(1);

        return score;

    }

    public boolean playerDoneGuessing(String gameKey, String username)  throws InterruptedException{
        request = new Request.Builder().url(baseURL + "/playerdoneguessing/" +gameKey+"/"+username).build();



        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                latch.countDown();
                return;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                started =  response.body().string();

                latch.countDown();
            }
        });

        latch.await();
        latch = new CountDownLatch(1);
        if(started.equals("false")){
            return false;
        }
        else{
            return true;
        }


    }

    public long getTimeTaken(String gameKey, String username)  throws InterruptedException{
        request = new Request.Builder().url(baseURL + "/gettimetaken/" +gameKey+"/"+username).build();


        call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                latch.countDown();
                return;
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                time = Long.parseLong(response.body().string());

                latch.countDown();
            }
        });

        latch.await();
        latch = new CountDownLatch(1);

        return time;

    }
}