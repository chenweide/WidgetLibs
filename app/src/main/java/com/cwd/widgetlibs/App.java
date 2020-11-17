package com.cwd.widgetlibs;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.cwd.widgetlibs.widgets.globalmsg.GlobalMsgManager;

public class App extends Application implements Application.ActivityLifecycleCallbacks {

    private Activity curActivity;

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(this);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {

    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        curActivity = activity;
        GlobalMsgManager.getInstance(activity).resume(activity);
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        GlobalMsgManager.getInstance(activity).pause(curActivity);
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {

    }
}
