package com.example.exercise;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    MediaPlayer mediaPlayer;

    public final String EXERCISE = "Exercise (seconds) : ";
    public final String BREAK = "Break (seconds) : ";

    int exerciseTime = 45;
    int breakTime = 15;

    long timeLeftMillSecond = 0;

    CountDownTimer countDownTimer;

    boolean isExercise = true;

    private Button startbtn, stopbtn, changebtn;
    private TextView exercisetv, breaktv;
    private TextView timertv;

    private Dialog dialogWithMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        timertv = findViewById(R.id.countdown_tv);
        exercisetv = findViewById(R.id.exercise_tv);
        breaktv = findViewById(R.id.break_tv);
        startbtn = findViewById(R.id.start_button);
        stopbtn = findViewById(R.id.stop_button);
        changebtn = findViewById(R.id.change_button);
        setupView();
    }

    public void setupView(){
        updateView();
        releaseMediaPlayer();
        mediaPlayer= MediaPlayer.create(this, R.raw.sound_file_1); // file name should not include space or capital letters
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });

        stopbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopTimer();
            }
        });

        changebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change();
            }
        });
    }

    public void startTimer(){
        if (isExercise){
            timeLeftMillSecond = exerciseTime*1000;
            isExercise = false;
        }else{
            timeLeftMillSecond = breakTime*1000;
            isExercise = true;
        }
        countDownTimer = new CountDownTimer(timeLeftMillSecond,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftMillSecond = millisUntilFinished;
                updateTimerView();
            }

            @Override
            public void onFinish() {
                mediaPlayer.start();
                startTimer();
            }
        }.start();
    }

    public void stopTimer(){
        isExercise = true;
        timeLeftMillSecond = 0;
        updateTimerView();
        countDownTimer.cancel();
    }

    public void updateTimerView(){
        int minute = (int)timeLeftMillSecond/60000;
        int second = (int)timeLeftMillSecond%60000/1000;

        String timeLeftText="";
        if (minute<10) timeLeftText = "0";
        timeLeftText +=minute+"";
        timeLeftText += " : ";
        if (second<10) timeLeftText += "0";
        timeLeftText += second+"";

        timertv.setText(timeLeftText);
    }

    public void change(){
        dialogWithMessage = new Dialog(this);
        dialogWithMessage.setContentView(R.layout.floating_dialog);

        final EditText exerciseEt, breakEt;
        Button doneBtn;

        exerciseEt = dialogWithMessage.findViewById(R.id.exercise_et);
        breakEt = dialogWithMessage.findViewById(R.id.break_et);
        doneBtn = dialogWithMessage.findViewById(R.id.done_btn);

        exerciseEt.setText(exerciseTime+"");
        breakEt.setText(breakTime+"");

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String exerciseString = exerciseEt.getText().toString();
                String breakString = breakEt.getText().toString();
                if (exerciseString.length()!=0 && breakString.length() != 0){
                    try{
                        exerciseTime = Integer.parseInt(exerciseString);
                        breakTime = Integer.parseInt(breakString);
                        updateView();
                        dialogWithMessage.dismiss();
                    }catch (NumberFormatException e){
                    }
                }
            }
        });

        dialogWithMessage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialogWithMessage.show();
    }

    public void updateView(){
        exercisetv.setText(EXERCISE+exerciseTime);
        breaktv.setText(BREAK+breakTime);
    }

    //what does this do, idk
    public void releaseMediaPlayer(){
        if (mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
