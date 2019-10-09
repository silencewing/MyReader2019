package cn.org.silencewing.reader.util;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;
import cn.org.silencewing.reader.ReadActivity;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-2-25
 * Time: 上午2:36
 * To change this template use File | Settings | File Templates.
 */
public class PhoneStatReceiver extends BroadcastReceiver {

    //private static final String TAG = "PhoneStatReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        //如果是拨打电话
        if(intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)){
            this.context.stop();
            //String phoneNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
        }else{
            //如果是来电
            TelephonyManager tm =
                    (TelephonyManager)context.getSystemService(Service.TELEPHONY_SERVICE);

            switch (tm.getCallState()) {
                case TelephonyManager.CALL_STATE_RINGING:
                    this.context.stop();
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    this.context.stop();
                    break;

                case TelephonyManager.CALL_STATE_IDLE:
                	 this.context.stop();
                    break;

            }
        }
    }

    private ReadActivity context;


    public PhoneStatReceiver(ReadActivity context) {

        this.context = context;

    }
}