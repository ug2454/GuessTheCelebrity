package com.example.guessthecelebrity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> celebrityURL = new ArrayList<>();
    ArrayList<String> celebrityName = new ArrayList<>();
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        Pattern pattern;
        Matcher matcher;
        DownloadWebContent task = new DownloadWebContent();
        String result = "";
        try {
            result = task.execute("https://www.forbes.com/celebrities/").get();
            Log.i("result",result);



        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        try {
//            result = task.execute("https://www.forbes.com/celebrities/").get();
//            pattern = Pattern.compile("&quot;(.*?)&quot");
//            matcher = pattern.matcher(result);
//            while (matcher.find()){
//                celebrityURL.add(matcher.group(1));
//            }
//            result = task.execute("").get();
//            pattern = Pattern.compile("<span class=\"profile-info__item profile-info__item--name\">(.*?)</span>");
//            matcher = pattern.matcher(result);
//            while (matcher.find()){
//                celebrityName.add(matcher.group(1));
//            }
//
//            System.out.println(celebrityName);
//            System.out.println(celebrityURL);
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }


    }

    static class DownloadWebContent extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            StringBuilder result = new StringBuilder();
            URL url = null;
            try {
                url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                int data = inputStreamReader.read();

                while (data != -1) {
                    char current = (char) data;
                    result.append(current);
                    data = inputStreamReader.read();
                }
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return "Failed";
            } catch (IOException e) {
                e.printStackTrace();
                return "Failed";
            }

        }
    }
}