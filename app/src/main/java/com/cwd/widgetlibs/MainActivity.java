package com.cwd.widgetlibs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cwd.widgetlibs.widgets.CountDownView;

public class MainActivity extends AppCompatActivity {

    private CountDownView countDownView;

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
}
