package com.cwd.widgetlibs.widgets.globalmsg;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.cwd.widgetlibs.R;


/**
 * @author cwd
 */
public class GlobalMsgView extends LinearLayout implements View.OnClickListener {

    private View rootView;
    private TextView tvContent;
    private ImageView ivClose;
    private TextView tvReply;
    private TextView tvName;
    private ImageView ivAvatar;
    private LinearLayout llView;

    private OnChildClickListener listener;

    private Activity currentActivity;
    private GlobalMsg msg;

    public interface OnChildClickListener {
        void onCloseClicked();
        void onReplyClicked();
    }

    public void setOnChildClickListener(OnChildClickListener listener){
        this.listener = listener;
    }

    public GlobalMsgView(Context context) {
        super(context);
        init(context);
    }

    public GlobalMsgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public GlobalMsgView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        rootView = LayoutInflater.from(context).inflate(R.layout.global_msg_view,null);
        tvContent = rootView.findViewById(R.id.tv_content);
        ivClose = rootView.findViewById(R.id.iv_close);
        tvReply = rootView.findViewById(R.id.tv_reply);
        tvName = rootView.findViewById(R.id.tv_name);
        ivAvatar = rootView.findViewById(R.id.iv_avatar);
        llView = rootView.findViewById(R.id.ll_view);
        ivClose.setOnClickListener(this);
        tvReply.setOnClickListener(this);
        llView.setOnClickListener(this);
        addView(rootView);
    }

    public void setMsg(GlobalMsg msg){
        if(msg != null){
            this.msg = msg;
            tvName.setText(msg.getName());
            tvContent.setText(msg.getContent());
            Glide.with(currentActivity).load(msg.getAvatar()).placeholder(R.mipmap.ic_launcher_round).into(ivAvatar);
            invalidate();
        }
    }

    public void updateCurrentActivity(Activity activity){
        this.currentActivity = activity;
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            if (v.getId() == R.id.iv_close) {
                listener.onCloseClicked();
            }else if(v.getId() == R.id.tv_reply){
                listener.onReplyClicked();
            }else if(v.getId() == R.id.ll_view){

            }
        }
    }
}