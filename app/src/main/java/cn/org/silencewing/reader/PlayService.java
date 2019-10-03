package cn.org.silencewing.reader;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class PlayService extends Service {


	public static final String CMDNAME = "command";
	public static final String CMDTOGGLEPAUSE = "togglepause";
	public static final String CMDSTOP = "stop";
	public static final String CMDPAUSE = "pause";
	public static final String CMDPREVIOUS = "previous";
	public static final String CMDNEXT = "next";
	
	public static class Intents {
		/*public static final String ACTION_QUICKPLAY = "org.jinzora.action.QUICKPLAY";
		public static final String ACTION_CONNECT_JUKEBOX = "org.jinzora.action.CONNECT_JUKEBOX";
		public static final String ACTION_DISCONNECT_JUKEBOX = "org.jinzora.action.DISCONNECT_JUKEBOX";

		public static final String ACTION_PLAYLIST = "cn.org.silencewing.PLAYLIST";
		public static final String ACTION_PLAYLIST_SYNC_REQUEST = "cn.org.silencewing.PLAYLIST_SYNC_REQUEST";
		public static final String ACTION_PLAYLIST_SYNC_RESPONSE = "cn.org.silencewing.PLAYLIST_SYNC_RESPONSE";*/
		public static final String ACTION_CMD_PLAY = "cn.org.silencewing.cmd.PLAY";
		public static final String ACTION_CMD_PAUSE = "cn.org.silencewing.cmd.PAUSE";
		public static final String ACTION_CMD_NEXT = "cn.org.silencewing.cmd.NEXT";
		public static final String ACTION_CMD_PREV = "cn.org.silencewing.cmd.PREV";
		public static final String ACTION_CMD_STOP = "cn.org.silencewing.cmd.STOP";
		public static final String ACTION_CMD_CLEAR = "cn.org.silencewing.cmd.CLEAR";
		public static final String ACTION_CMD_JUMPTO = "cn.org.silencewing.cmd.JUMPTO";
		public static final String ACTION_CMD_PLAYPAUSE = "cn.org.silencewing.cmd.PLAYPAUSE";

		public static final String CATEGORY_REMOTABLE = "junction.remoteintent.REMOTABLE";
	}
	
	static PlayService instance;
	final static String TAG = "PlayService";
	public static PlayService getInstance() {
		if (instance == null) {
			Log.w(TAG, "PlaybackService.instance should not be null");
			instance = new PlayService();
		}
		return instance;
	}

	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "PlaybackService::onCreate called");

	/*	mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		mRemoteControlResponder = new ComponentName(this, RemoteControlReceiver.class);

		/** Playback commands (internal use) **/
	/*	IntentFilter commandFilter = new IntentFilter();
		commandFilter.addAction(SERVICECMD);
		commandFilter.addAction(Intents.ACTION_CMD_PLAYPAUSE);
		commandFilter.addAction(Intents.ACTION_CMD_PAUSE);
		commandFilter.addAction(Intents.ACTION_CMD_PLAY);
		commandFilter.addAction(Intents.ACTION_CMD_PREV);
		commandFilter.addAction(Intents.ACTION_CMD_NEXT);
		commandFilter.addAction(Intents.ACTION_CMD_STOP);
		commandFilter.addAction(Intents.ACTION_CMD_CLEAR);
		commandFilter.addAction(Intents.ACTION_CMD_JUMPTO);

		commandFilter.addCategory(Intents.CATEGORY_REMOTABLE);
		registerReceiver(commandReceiver, commandFilter);*/
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		/*unregisterRemoteControl();
		unregisterReceiver(commandReceiver);*/
	}

}
