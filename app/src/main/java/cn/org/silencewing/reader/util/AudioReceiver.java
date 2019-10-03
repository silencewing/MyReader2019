package cn.org.silencewing.reader.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.view.KeyEvent;
import cn.org.silencewing.reader.Intents;
import cn.org.silencewing.reader.ReadActivity;


public class AudioReceiver extends BroadcastReceiver  {
	

	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		// 获得Action 
        String intentAction = intent.getAction(); 
        // 获得KeyEvent对象 
        KeyEvent keyEvent = (KeyEvent) intent.getParcelableExtra(Intent.EXTRA_KEY_EVENT); 
 
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intentAction)) {
	    	ReadActivity.broadcastCommand(context, Intents.ACTION_CMD_STOP);
	        }
	 
	} 

}
