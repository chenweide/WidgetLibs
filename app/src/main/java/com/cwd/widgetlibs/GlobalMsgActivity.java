package com.cwd.widgetlibs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.cwd.widgetlibs.widgets.globalmsg.GlobalMsg;
import com.cwd.widgetlibs.widgets.globalmsg.GlobalMsgManager;

import java.util.Random;

public class GlobalMsgActivity extends AppCompatActivity {

    private String[] names = {"Kobe","James","Wade"};
    private String[] contents = {"Hello~","What's up!","Good Morning!"};
    private String[] avatars = {"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1605635510927&di=b89754aad522d9f5e973f46bfcf649b0&imgtype=0&src=http%3A%2F%2Fn.sinaimg.cn%2Fsinacn09%2F794%2Fw477h317%2F20180811%2F82b5-hhnunsr2441856.jpg",
    "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1605635559578&di=fd8b06eb9f490eed68af2c751e815a6f&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fimages%2F20180205%2F20675caf7e8a49a880b627b8b5e84ee3.jpeg",
    "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=3966931011,520454147&fm=15&gp=0.jpg"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_msg);
        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GlobalMsg msg = new GlobalMsg();
                msg.setName(names[new Random().nextInt(3)]);
                msg.setContent(contents[new Random().nextInt(3)]);
                msg.setAvatar(avatars[new Random().nextInt(3)]);
                GlobalMsgManager.getInstance(GlobalMsgActivity.this).addMsg(msg);
            }
        });
    }
}