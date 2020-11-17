package com.cwd.widgetlibs.widgets.globalmsg;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.cwd.widgetlibs.utils.Util;


/**
 * 全局消息队列管理类
 * @author cwd
 */
public class GlobalMsgManager {

    private static final int ADD_MSG = 0;
    private static final int TAKE_MSG = 1;
    private static final int SHOW_VIEW = 2;
    private static final int HIDE_VIEW = 3;

    private static final int DELAY_TIME = 3 * 1000;

    private static volatile GlobalMsgManager msgManager;

    private static GlobalMsgQueue msgQueue;
    private static MsgHandler msgHandler;
    private static GlobalMsgView curMsgView;
    private static GlobalMsgAnim msgAnim;
    private static Activity curActivity;

    private GlobalMsgManager(final Activity activity){
        msgQueue = new GlobalMsgQueue();
        msgHandler = new MsgHandler();
        curMsgView = new GlobalMsgView(activity);
        msgAnim = new GlobalMsgAnim(activity, curMsgView);
        msgAnim.setOnAnimListener(new GlobalMsgAnim.OnAnimListener() {
            @Override
            public void onShowAnimEnd() {
                //N秒后隐藏MsgView
                msgHandler.sendEmptyMessageDelayed(HIDE_VIEW,DELAY_TIME);
            }

            @Override
            public void onHideAnimEnd() {
                FrameLayout root = ((FrameLayout)curActivity.getWindow().getDecorView());
                if(root.indexOfChild(curMsgView) != -1){
                    root.removeView(curMsgView);
                }
                Message.obtain(msgHandler,TAKE_MSG).sendToTarget();
            }
        });

        curMsgView.setOnChildClickListener(new GlobalMsgView.OnChildClickListener() {
            @Override
            public void onCloseClicked() {
                msgHandler.removeMessages(HIDE_VIEW);
                msgAnim.hide();
            }

            @Override
            public void onReplyClicked() {

            }
        });
    }

    public static GlobalMsgManager getInstance(Activity activity){
        if(msgManager == null){
            synchronized (GlobalMsgManager.class){
                if(msgManager == null){
                    msgManager = new GlobalMsgManager(activity);
                }
            }
        }
        return msgManager;
    }

    public void resume(Activity currentActivity){
        curActivity = currentActivity;
        curMsgView.updateCurrentActivity(currentActivity);
        msgHandler.updateCurrentActivity(currentActivity);
    }

    public void pause(Activity currentActivity){
        FrameLayout root = ((FrameLayout)currentActivity.getWindow().getDecorView());
        if(root.indexOfChild(curMsgView) != -1){
            root.removeView(curMsgView);
        }
    }

    public void addMsg(GlobalMsg msg){
        Message.obtain(msgHandler,ADD_MSG,msg).sendToTarget();
    }

    public void release(){
        msgQueue.clearAll();
        msgQueue = null;
        msgHandler = null;
        curMsgView = null;
        msgAnim.cancel();
        msgAnim = null;
    }

    private static class MsgHandler extends Handler{

        private Activity activity;

        public void updateCurrentActivity(Activity activity){
            this.activity = activity;
        }

        @Override
        public void handleMessage(final Message msg) {
            if(activity != null && !activity.isFinishing()){
                switch (msg.what){
                    case ADD_MSG:
                        Log.d("global_mag","add msg");
                        msgQueue.addMsg((GlobalMsg) msg.obj);
                        if(!msgQueue.isRunning){
                            Message.obtain(msgHandler,TAKE_MSG).sendToTarget();
                        }
                        break;
                    case TAKE_MSG:
                        //执行消息弹出动画
                        GlobalMsg globalMsg = msgQueue.takeMsg();
                        if(globalMsg != null){
                            Log.d("global_mag","take msg --> queue_size:" + msgQueue.size());
                            msgQueue.isRunning = true;
                            curMsgView.setMsg(globalMsg);
                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.gravity = Gravity.BOTTOM;
                            params.leftMargin = Util.dip2px(activity,10);
                            params.bottomMargin = Util.dip2px(activity,60);
                            ((FrameLayout)activity.getWindow().getDecorView()).addView(curMsgView,params);
                            msgAnim.show();
                        }else{
                            msgQueue.isRunning = false;
                        }
                        break;
                    case SHOW_VIEW:

                        break;
                    case HIDE_VIEW:
                        msgAnim.hide();
                        break;
                }
            }
        }
    }
}
