package cn.org.silencewing.reader.util;

import cn.org.silencewing.reader.Intents;
import cn.org.silencewing.reader.ReadActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.util.Log;
import android.view.KeyEvent;



public class RemoteControlReceiver extends BroadcastReceiver { 
    private static final String  TAG = "ControlReceiver"; 
   // GlobalUtil globalUtil = GlobalUtil.getInstance();
    
    
 

 // android:priority="2147483646"
  
    public void changePlay(Context context)
    {
    	ReadActivity.broadcastCommand(context,Intents.ACTION_CMD_PLAYPAUSE);
	    //getTts(context).changePlay();
    	//Log.i(TAG, "changePlay"); 
    }
    public void playNext(Context context)
    {
    	ReadActivity.broadcastCommand(context, Intents.ACTION_CMD_NEXT);
    }
    public void playPre(Context context)
    {
    	ReadActivity.broadcastCommand(context, Intents.ACTION_CMD_PREV);
    }
   /* public MediaButtonReceiver(TtsActivity activity)
    {
    	this.activity = activity;
    }*/
    
    static GlobalConfig config = GlobalConfig.getInstance();
    @Override
    public void onReceive(Context context, Intent intent)
    {
        // 获得Action 
        String intentAction = intent.getAction(); 
        // 获得KeyEvent对象 
        KeyEvent keyEvent = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT); 
 
//        Log.i(TAG, "Action ---->" + intentAction + "  KeyEvent----->"+ keyEvent.toString());
        

        
       
        if (Intent.ACTION_MEDIA_BUTTON.equals(intentAction)) { 
            // 获得按键字节码 
            int keyCode = keyEvent.getKeyCode(); 
            // 按下 / 松开 按钮 
            int keyAction = keyEvent.getAction(); 
            if(keyAction != KeyEvent.ACTION_DOWN)
            	return;
 
            
            switch(keyCode)
            {
            case KeyEvent.KEYCODE_MEDIA_NEXT :
            	playNext(context);break;
           
            case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
            	playPre(context);break;


            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                 playNext(context);break;
            case KeyEvent.KEYCODE_HEADSETHOOK:
            	changePlay(context);break;
            case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
            	changePlay(context);break;
            case KeyEvent.KEYCODE_MEDIA_PLAY:
            	changePlay(context);break;
            case KeyEvent.KEYCODE_MEDIA_PAUSE:
            	changePlay(context);break;
            	

            }
           
        }


       /* {
            // 获得按键字节码
            int keyCode = keyEvent.getKeyCode();
            // 按下 / 松开 按钮
            int keyAction = keyEvent.getAction();
            if (keyAction != KeyEvent.ACTION_DOWN)
                return;


            switch (keyCode) {
                case KeyEvent.KEYCODE_VOLUME_DOWN:
                    if (config.isVolumeToCtrl())
                        playNext(context);
                    break;

                case KeyEvent.KEYCODE_VOLUME_UP:
                    if (config.isVolumeToCtrl())
                        playPre(context);
                    break;
            }
        }*/
     }
	
} 