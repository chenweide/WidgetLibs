package com.cwd.widgetlibs.widgets.globalmsg;

import java.util.LinkedList;

/**
 * @author cwd
 */
public class GlobalMsgQueue {

    private LinkedList<GlobalMsg> msgList = new LinkedList<>();
    public boolean isRunning;

    public void addMsg(GlobalMsg msg){
        if(msg != null){
            msgList.add(msg);
        }
    }

    public GlobalMsg takeMsg(){
        if(!msgList.isEmpty()){
            return msgList.pollFirst();
        }
        return null;
    }

    public void clearAll(){
        msgList.clear();
    }

    public int size(){
        return msgList.size();
    }
}
