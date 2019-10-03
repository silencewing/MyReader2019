package cn.org.silencewing.reader.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import cn.org.silencewing.reader.Intents;
import cn.org.silencewing.reader.ReadActivity;

public class CommandReceiver  extends BroadcastReceiver{

	
	ReadActivity context;
	
	public CommandReceiver(ReadActivity context)
	{
		this.context = context;
	}

	static final String TAG = "CommandReceiver";
	static GlobalConfig config = GlobalConfig.getInstance();
	
	@Override
	public void onReceive(Context context, Intent intent) {
	   // Log.w(TAG, "got a command " + intent);


		try {
			abortBroadcast();
		} catch (Exception e) {
		}
		
		try {
			String action = intent.getAction();
			String cmd = intent.getStringExtra("command");
			
			if(Intents.CMDNEXT.equals(cmd)|| Intents.ACTION_CMD_NEXT.equals(action))
			{
				this.context.playNext(config.getSeekStep());
			}
			if(Intents.CMDPREVIOUS.equals(cmd)|| Intents.ACTION_CMD_PREV.equals(action))
			{
				this.context.playNext(-config.getSeekStep());
			}
			if(Intents.CMDPAUSE.equals(cmd)|| Intents.ACTION_CMD_PAUSE.equals(action))
			{
				this.context.changePlay();
			}
			if(Intents.CMDPLAY.equals(cmd)|| Intents.ACTION_CMD_PLAY.equals(action))
			{
				this.context.changePlay();
			}
			if(Intents.ACTION_CMD_PLAYPAUSE.equals(action))
			{
				this.context.changePlay();
			}
			if(Intents.ACTION_CMD_STOP.equals(action))
			{
				this.context.stop();
			}
		}
		catch(Exception e)
		{
			
		}
	}
}
