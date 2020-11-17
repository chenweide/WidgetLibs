package com.cwd.widgetlibs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.cwd.widgetlibs.widgets.CountDownView;
import com.cwd.widgetlibs.widgets.HeartView;
import com.cwd.widgetlibs.widgets.globalmsg.GlobalMsgManager;

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

        findViewById(R.id.btn_global_msg).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,GlobalMsgActivity.class);
                startActivity(intent);
            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        GlobalMsgManager.getInstance(this).release();
    }
}
