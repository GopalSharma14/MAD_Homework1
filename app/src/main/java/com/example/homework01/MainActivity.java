package com.example.homework01;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


// Assignment Homework01_Group27
// Submitted by Gopal Sharma and Priyaank Chhadwa

public class MainActivity extends AppCompatActivity {

    private TextView textViewMin;
    private TextView textViewMax;
    private TextView textViewAvg;
    private TextView tv_compl;

    private ProgressBar progressbar;

    private SeekBar seekBar;

    private Button calc_btn;

    ExecutorService threadPool;
    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewMin = findViewById(R.id.textViewMin);
        textViewMax = findViewById(R.id.textViewMax);

        textViewAvg = findViewById(R.id.textViewAvg);

        tv_compl = findViewById(R.id.compl_tv);

        progressbar = findViewById(R.id.progressbar);

        seekBar = findViewById(R.id.seekbar);
        tv_compl.setText(Integer.toString(seekBar.getProgress()));

        calc_btn = findViewById(R.id.btn_calc);
        threadPool = Executors.newFixedThreadPool(2);

        
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tv_compl.setText(String.valueOf(progress));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        calc_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressbar.setVisibility(ProgressBar.VISIBLE);
                threadPool.execute(new GetNumbers(seekBar.getProgress()));
            }
        });

        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(@NonNull Message msg) {

                textViewMin.setText(String.valueOf(msg.getData().getDouble(GetNumbers.MINIMUM)));
                textViewMax.setText(String.valueOf(msg.getData().getDouble(GetNumbers.MAXIMUM)));

                textViewAvg.setText(String.valueOf(msg.getData().getDouble(GetNumbers.AVERAGE)));
                progressbar.setVisibility(ProgressBar.INVISIBLE);

                return true;
            }
        });

    }

    class GetNumbers implements Runnable {

        int value;

        static final String MINIMUM ="Minimum";
        static final String MAXIMUM ="Maximum";

        static final String AVERAGE ="Average";


        public GetNumbers(int value) {
            this.value = value;
        }


        @Override
        public void run() {

            ArrayList<Double> result;
            result = HeavyWork.getArrayNumbers(value);
            Collections.sort(result);

            double min = result.get(0);

            double max = result.get(result.size()-1);

            double sum = 0;
            for(int i = 0; i < result.size(); i++)
                sum += result.get(i);

            double avg = sum / result.size();
            Message msg = new Message();

            Bundle bundle = new Bundle();
            bundle.putDouble(MINIMUM, min);
            bundle.putDouble(MAXIMUM, max);

            bundle.putDouble(AVERAGE, avg);

            msg.setData(bundle);

            handler.sendMessage(msg);


        }
    }


}



