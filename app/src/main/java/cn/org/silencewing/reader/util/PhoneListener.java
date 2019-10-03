package cn.org.silencewing.reader.util;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import cn.org.silencewing.reader.ReadActivity;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-2-25
 * Time: 上午2:28
 * To change this template use File | Settings | File Templates.
 */

public class PhoneListener extends PhoneStateListener {

    @Override
    public void onCallStateChanged(int state, String incomingNumber) {
        switch(state) {
            case TelephonyManager.CALL_STATE_IDLE: //空闲
                break;
            case TelephonyManager.CALL_STATE_RINGING: //来电
                this.context.stop();
                break;
            case TelephonyManager.CALL_STATE_OFFHOOK: //摘机（正在通话中）
                break;
        }
    }

    private ReadActivity context;


    public PhoneListener(ReadActivity context) {

        this.context = context;

    }
}

