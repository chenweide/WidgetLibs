package com.cwd.widgetlibs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cwd.widgetlibs.widgets.CountDownView;
import com.cwd.widgetlibs.widgets.HeartView;

public class MainActivity extends AppCompatActivity {

    private CountDownView countDownView;
    private HeartView heartView;
    private boolean isChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initListeners();
    }

    private void initViews(){
        countDownView = findViewById(R.id.count_down_view);
        countDownView.setCountDown(5);
        heartView = findViewById(R.id.heart_view);
    }

    public void initListeners(){
        countDownView.setCountDownListener(new CountDownView.CountDownListener() {
            @Override
            public void onFinish() {
                Toast.makeText(MainActivity.this,"finish",Toast.LENGTH_SHORT).show();
            }
        });
        countDownView.start();
    }

    public void heartViewClick(View view){
        isChecked = !isChecked;
        heartView.setChecked(isChecked);
    }
}
