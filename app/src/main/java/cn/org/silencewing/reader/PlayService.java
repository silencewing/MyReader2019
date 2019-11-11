package cn.org.silencewing.reader;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.util.Log;

import static android.app.PendingIntent.getActivity;

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
	public int onStartCommand(Intent intent, int flags, int startId) {
		 Log.d(TAG, "onStartCommand()");
		 // 在API11之后构建Notification的方式
		 Notification.Builder builder = new Notification.Builder
		  (this.getApplicationContext()); //获取一个Notification构造器
		 Intent nfIntent = new Intent(this, ReadActivity.class);

		 builder.setContentIntent(PendingIntent.
				getActivity(this, 0, nfIntent, 0)) // 设置PendingIntent
	//	  .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
	//					   R.mipmap.ic_large)) // 设置下拉列表中的图标(大图标)
		  .setContentTitle("下拉列表中的Title") // 设置下拉列表里的标题
	//	  .setSmallIcon(R.mipmap.ic_launcher) // 设置状态栏内的小图标
		  .setContentText("要显示的内容") // 设置上下文内容
		  .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

		 Notification notification = builder.build(); // 获取构建好的Notification
		 notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
		startForeground(999, notification);
		return super.onStartCommand(intent,flags,startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
		super.onDestroy();

		/*unregisterRemoteControl();
		unregisterReceiver(commandReceiver);*/
	}

}
