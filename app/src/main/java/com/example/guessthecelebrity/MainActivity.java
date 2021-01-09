package com.example.guessthecelebrity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    ArrayList<String> celebrityURL = new ArrayList<>();
    ArrayList<String> celebrityName = new ArrayList<>();
    ImageView imageView;
    Random random = new Random();
    Button button;
    Button button1;
    Button button2;
    Button button3;
    Button button4;
    int locationOfCorrectAnswer;
    ArrayList<Integer> result = new ArrayList<>();
    int randomImageNumber;
    boolean isDataLoading = false;
    int correctAnswer = 0;
    int totalAnswer = 0;
    TextView textView;
    ArrayList<Integer> celebrityAlreadyFound = new ArrayList<>();

    public  void resetScore(View view){
        correctAnswer=0;
        totalAnswer=0;
        result.clear();
        updateUI();

    }

    public void updateUI() {

        textView = findViewById(R.id.textView);
        textView.setText(correctAnswer + " / " + totalAnswer);

        button1 = findViewById(R.id.button);
        button2 = findViewById(R.id.button2);
        button3 = findViewById(R.id.button3);
        button4 = findViewById(R.id.button4);
        randomImageNumber = random.nextInt(100) + 1;

        while (celebrityAlreadyFound.contains(randomImageNumber)) {
            randomImageNumber = random.nextInt(100) + 1;
        }

        locationOfCorrectAnswer = random.nextInt(4);
        for (int i = 0; i < 4; i++) {
            if (i == locationOfCorrectAnswer) {
                result.add(randomImageNumber);
                celebrityAlreadyFound.add(randomImageNumber);

            } else {
                int wrongAnswer = random.nextInt(100) + 1;
                if (randomImageNumber == wrongAnswer) {
                    wrongAnswer = random.nextInt(100) + 1;
                }
                result.add(wrongAnswer);

            }
        }

        Picasso.with(this).load(celebrityURL.get(randomImageNumber)).into(imageView);
        try {
            button1.setText(celebrityName.get(result.get(0)));
            button2.setText(celebrityName.get(result.get(1)));
            button3.setText(celebrityName.get(result.get(2)));
            button4.setText(celebrityName.get(result.get(3)));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void selectCelebrity(View view) {

        button = (Button) view;
        try {
            if (button.getText().equals(celebrityName.get(randomImageNumber))) {
                Toast toast = Toast.makeText(this, "You got it right!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();
                correctAnswer++;
            } else {

                Toast toast = Toast.makeText(this, "Correct answer is " + celebrityName.get(randomImageNumber), Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.show();

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        totalAnswer++;
        result.clear();
        try {
            updateUI();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

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
            result = task.execute("https://www.imdb.com/list/ls052283250/").get();
            Log.i("result", result);


        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        pattern = Pattern.compile("src=\"(.*?)\"");
        matcher = pattern.matcher(result);
        while (matcher.find()) {

            celebrityURL.add(matcher.group(1));
        }

        pattern = Pattern.compile("<img alt=\"(.*?)\"");
        matcher = pattern.matcher(result);
        while (matcher.find()) {

            celebrityName.add(matcher.group(1));
        }


        celebrityURL.remove(0);
        celebrityURL.remove(1);
        celebrityURL.remove(2);
        celebrityURL.remove(3);
        celebrityURL.remove(4);


        updateUI();


    }

    class DownloadWebContent extends AsyncTask<String, Void, String> {





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