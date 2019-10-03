package cn.org.silencewing.reader.util;


import cn.org.silencewing.reader.ReadActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.view.KeyEvent;

public class AudioListener implements OnAudioFocusChangeListener {
	
	
	ReadActivity context;
	
	public AudioListener(ReadActivity context)
	{
		this.context = context;
	}
	@Override
	public void onAudioFocusChange(int focusChange) {
		
		switch(focusChange)
		{
		case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
			context.unRegisterMediaButtonEventReceiver();
			break;
		case AudioManager.AUDIOFOCUS_LOSS:
		  
			context.unRegisterMediaButtonEventReceiver();
			break;
		case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
			context.unRegisterMediaButtonEventReceiver();
			break;
		case AudioManager.AUDIOFOCUS_GAIN:
			context.registerMediaButtonEventReceiver();
			break;
		}
	   
	}
}
