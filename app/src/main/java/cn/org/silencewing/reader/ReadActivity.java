package cn.org.silencewing.reader;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.admin.DevicePolicyManager;
import android.content.*;
import android.graphics.Rect;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.media.AudioManager;
import android.media.RemoteControlClient;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.StrictMode;
import android.speech.tts.TextToSpeech;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.Layout;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import cn.org.silencewing.reader.R;
import cn.org.silencewing.reader.util.*;
import cn.org.silencewing.reader.widget.TextListView;
import cn.org.silencewing.reader.widget.TextViewer;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;


import static android.view.View.OnTouchListener;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-2
 * Time: 下午10:27
 * To change this template use File | Settings | File Templates.
 */
public class ReadActivity extends Activity {

    private ScrollView scrollView; // 滚动视图
    private TextListView listView; // 滚动视图
    private TextListAdapter listAdapter; // 滚动视图

    GestureDetector gestureDetector; // 手势检测
    private SensorManager sensorManager;//声明一个SensorManager
    private AudioManager audioManager;//声明一个SensorManager
    private AudioListener audioListner;
    private ComponentName eventReceiver;
    //传感器
    private Sensor sensor;
    private PhoneStatReceiver phoneStatReceiver;

    private ListViewListener listViewListner = new ListViewListener(this);

    private boolean isSetting = false;
    //private boolean isPlayScroll = false;
   // private TextViewer content;


    LinearLayout header, footer;
    LinearLayout locateLayout;
    SeekBar listSeek;
    EditText searchKey;
    TextView location;
    
    GlobalUtil globalUtil = GlobalUtil.getInstance();
    private TextUtil textUtil = null;
    //AudioReceiver audioReceiver;

    static MyTextToSpeech tts;
    
    static WindowManager windowManager;
    public static Display display;// = wm.getDefaultDisplay();  //为获取屏幕宽、高
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 填充标题栏
        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,      
                WindowManager.LayoutParams. FLAG_FULLSCREEN);   
     
        setContentView(R.layout.main);

        windowManager = getWindowManager();
        display = windowManager.getDefaultDisplay();
        
        if(tts == null)
        	tts = new MyTextToSpeech();
      
        tts.init(this);
        
        PlayHelper.setTts(tts);
        GestureListener gestureListner = new GestureListener(this);
        gestureDetector = new GestureDetector(this, gestureListner);

    
        
        registerReceiver();
        
        
        //audioManager.registerRemoteControlClient(new RemoteControlClient(new PendingIntent()))
        //this.registerReceiver(new MediaButtonReceiver(), filter)
        
                //获取电话通讯服务
        //TelephonyManager tpm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        //创建一个监听对象，监听电话状态改变事件
        //tpm.listen(new PhoneListener(this),
        //        PhoneStateListener.LISTEN_CALL_STATE);

        header = (LinearLayout) findViewById(R.id.headerLayout); // 获取底部布局
        footer = (LinearLayout) findViewById(R.id.footerLayout); // 获取底部布局

        //scrollView = (ScrollView) findViewById(R.id.scrollView);
        
        listSeek = (SeekBar) findViewById(R.id.listSeek);
        location = (TextView) findViewById(R.id.location);
		 locateLayout = (LinearLayout)findViewById(R.id.locateLayout);
		 searchKey = (EditText) findViewById(R.id.searchKey);

	     listSeek.setOnSeekBarChangeListener(seekListner);

	 	
	     if(policyManager == null)
	      	policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
	     if(componentLockName == null)
	        componentLockName = new ComponentName(this, AdminReceiver.class); 
	        
	     //activeManager();
	     
	     
        if(savedInstanceState != null)
        {
            load("");
            //PlayHelper.
        }
    }

    public void registerReceiver()
    {
    	if(phoneStatReceiver == null)
    	{
	        IntentFilter intentFilter = new IntentFilter();
	        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
//	        intentFilter.addAction(Intent.LISTEN_CALL_STATE);
	        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
//	        intentFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
	        phoneStatReceiver = new PhoneStatReceiver(this);
	        this.registerReceiver(phoneStatReceiver,intentFilter);
//            TelephonyManager manager = (TelephonyManager) this.getSystemService(TELEPHONY_SERVICE);
//            manager.listen(phoneStatReceiver, PhoneStateListener.LISTEN_CALL_STATE);
    	}
		
       
        //onResume可能会好点，但是太损耗性能，算了
        //MediaButtonReceiver mediaButtonReceiver = new MediaButtonReceiver(this);
    	if(audioManager == null)
    		audioManager =(AudioManager)getSystemService(Context.AUDIO_SERVICE); 
    	if(audioListner == null)
    		audioListner = new AudioListener(this);
        int result= audioManager.requestAudioFocus(audioListner,AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if(result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED )
        	registerMediaButtonEventReceiver();
        
    	if(commandReceiver == null)
    	{
	    	IntentFilter commandFilter = new IntentFilter();
			commandFilter.addAction(Intents.SERVICECMD);
			commandFilter.addAction(Intents.ACTION_CMD_PLAYPAUSE);
			commandFilter.addAction(Intents.ACTION_CMD_PAUSE);
			commandFilter.addAction(Intents.ACTION_CMD_PLAY);
			commandFilter.addAction(Intents.ACTION_CMD_PREV);
			commandFilter.addAction(Intents.ACTION_CMD_NEXT);
			commandFilter.addAction(Intents.ACTION_CMD_STOP);
	
			commandFilter.addCategory(Intents.CATEGORY_REMOTABLE);
	
	        commandReceiver = new CommandReceiver(this);
			registerReceiver(commandReceiver, commandFilter);
    	}
    	
    	isRegister = true;
    }
    public void unRegisterReceiver()
    {
    	//if(commandReceiver!=null&& commandReceiver.

    	if(commandReceiver !=null)
    	{
    		try{
    		this.unregisterReceiver(commandReceiver);
    		}finally
    		{}
    		commandReceiver = null;
    	}
    	unRegisterMediaButtonEventReceiver();
        audioManager.abandonAudioFocus(audioListner);
        
        if(phoneStatReceiver!=null)
        {
        	try{
        	this.unregisterReceiver(phoneStatReceiver);
        	}finally{}
        	phoneStatReceiver = null;
        }

        isRegister = false;
    }
    RemoteControlReceiver receiver;
    public void registerMediaButtonEventReceiver()
    {
    	if (android.os.Build.VERSION.SDK_INT >= 8)
    	{
    		//eventReceiver = new ComponentName(getP); 
    	 eventReceiver = new ComponentName(getPackageName(),RemoteControlReceiver.class.getName()); 
         //audioManager.registerMediaButtonEventReceiver(eventReceiver);
         registerRemoteControl();

         //receiver = new ControlReceiver();
         //registerReceiver(receiver, new IntentFilter(Intent.ACTION_MEDIA_BUTTON));

    	}
    }
    
    public void unRegisterMediaButtonEventReceiver()
    {
    	 if(audioManager != null && eventReceiver !=null)
         {
    		 unregisterRemoteControl();
         	//audioManager.unregisterMediaButtonEventReceiver(eventReceiver);
         	eventReceiver = null;
         	//unregisterReceiver(receiver);
         }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (config.isVolumeToCtrl()) {
                    playNext(config.getSeekStep());
                    return true;
                }
                break;

            case KeyEvent.KEYCODE_VOLUME_UP:
                if (config.isVolumeToCtrl()) {
                    playNext(-config.getSeekStep());
                    return true;
                }
                break;
        }
        return super.onKeyDown(keyCode,event);
    }
    /*
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	;
    	switch(keyCode)
    	{
    	case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
    		changePlay();
    		break;
    	case KeyEvent.KEYCODE_MEDIA_PLAY:
    		changePlay();
    		break;
    	case KeyEvent.KEYCODE_MEDIA_PAUSE:
    		changePlay();
    		break;
    	}	
        return super.onKeyDown(keyCode, event);
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       this.changeShow();
        return false;

    }





    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        save();
        //savedInstanceState.putString(, textUtil.getFilePath());

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        //String fileName = savedInstanceState.getString("StrTest");
       // load("");
        //String StrTest = savedInstanceState.getString("StrTest");
        //Log.e(TAG, "onRestoreInstanceState+IntTest="+IntTest+"+StrTest="+StrTest);
    }


   // private boolean isPlay = false;


    public void changePlay(View v) {
        changePlay();
    }

    public void changePlay() {
        if (listView == null)
        {
        	load("");
        	return;
        }
      if(PlayHelper.changePlay())
	     refreshListView();
      
    }

    public static void broadcastCommand(Context c, String cmd) {
		Intent i = new Intent(cmd);
		i.addCategory(Intents.CATEGORY_REMOTABLE);
		Log.d("junction", "broadcasting " + i);
		c.sendOrderedBroadcast(i, null);
	}
    
    
	
	private BroadcastReceiver commandReceiver;
	

    private  boolean isMenuVisible()
    {
       return footer.getVisibility() == View.VISIBLE;
    }
    public void changeShow() {
        //if(isPlay)return;

        if (isMenuVisible()) {
            this.header.setVisibility(View.GONE);
            this.footer.setVisibility(View.GONE);
        } else {
            this.header.setVisibility(View.VISIBLE);
            //progressListSeek();
            this.footer.setVisibility(View.VISIBLE);
        }
    }

  /*  public void progressListSeek() {
        if (listSeek != null && listView != null)
            listSeek.setProgress(listView.getFirstVisiblePosition());
    }*/

    
    
    public void fileBrower(View v) {
        // 跳转到文件浏览Activity
    	Intent intent = new Intent(this, FileBrowserActivity.class);
    	intent.putExtra(FileBrowserActivity.KEY_ROOT,GlobalConfig.getInstance().getRoot());
    	intent.putExtra(FileBrowserActivity.KEY_SORT, FileBrowserActivity.CODE_SORT_FILE_NAME);
    	intent.putExtra(FileBrowserActivity.KEY_SORT_DESC, false);
    	
        startActivityForResult(intent,
                FileBrowserActivity.CODE_FILE_BROWSER);
    }
    
    public void toSetting(View v) {
        // 跳转到文件浏览Activity
    	Intent intent = new Intent(this, SettingActivity.class);
    	//intent.putExtra(FileBrowserActivity.KEY_ROOT,GlobalConfig.getInstance().getRoot());
    	//intent.putExtra(FileBrowserActivity.KEY_SORT, FileBrowserActivity.CODE_SORT_FILE_NAME);
    	//intent.putExtra(FileBrowserActivity.KEY_SORT_DESC, false);
    	
        startActivityForResult(intent,
                SettingActivity.CODE_SETTING);
    }
    
    public void historyBrower(View v) {
        // 跳转到文件浏览Activity
    	Intent intent = new Intent(this, FileBrowserActivity.class);
    	intent.putExtra(FileBrowserActivity.KEY_ROOT,GlobalUtil.getHistoryFilePath(""));
    	intent.putExtra(FileBrowserActivity.KEY_SORT, FileBrowserActivity.CODE_SORT_MODIFY_TIME);
    	intent.putExtra(FileBrowserActivity.KEY_SORT_DESC, true);
        startActivityForResult(intent,
                FileBrowserActivity.CODE_HISTORY_BROWSER);
    }

    static final int HELP_MESSAGE = 999999;

    public void help(View v) {
        // 跳转到文件浏览Activity
        //Toast.makeText(this,R.string.text_error,0);

        showDialog(HELP_MESSAGE);
    }


    public void changeSearch(View v)
    {
    
    	if(!globalUtil.changeShow(locateLayout))
    	{
    		hideIM(searchKey);
    	}
    
    	 
    }
    //sint searchStart = 0;
    public void search(View v) {
        // 跳转到文件浏览Activity
        //Toast.makeText(this,R.string.text_error,0);
      
    		
    		
    		//searchKey.setVisibility(View.GONE);
    		hideIM(searchKey);
    		if(listView == null) return;
    		
    		String key = searchKey.getText().toString();
    		int result = PlayHelper.search(key, listSeek.getProgress()+1,listSeek.getMax()-1);
    		//if(result != )
    		if(result == PlayHelper.NULL_POSITION)
    			result = PlayHelper.search(key, 0, listSeek.getProgress());
    		
    		if(result != PlayHelper.NULL_POSITION)
    		{
    			//PlayHelper.movePlay(result, 0);
    			//playItem()
    			
    			//listSeek.setProgress(result);
    			setLocation(result);
    			
    		}
    	
    }

  public void hideIM(View edt) {

        // try to hide input_method:
        try {
            InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

            IBinder windowToken = edt.getWindowToken();
            if (windowToken != null) {

                // always de-activate IM
                im.hideSoftInputFromWindow(windowToken, 0);
            }

        } catch (Exception e) {

            globalUtil.log(e);
        }
    }
  
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case HELP_MESSAGE: // 文件打开确认框
                return new AlertDialog.Builder(this)
                        .setTitle(R.string.help_title)
                        .setMessage(R.string.help_message)
                        .setPositiveButton(R.string.help_ok, null)
                        .create();
        }
        return super.onCreateDialog(id);
    }

    public void fileOpen(String fileName, String encoding, int currentPosition, int wordIndex, int firstPostion) {
        
    	textUtil = TextUtil.getInstance(fileName);
        if (!globalUtil.isStringEmpty(encoding))
            textUtil.setEncoding(encoding);

        //Core init
        PlayHelper.initData(textUtil.getList(),currentPosition,wordIndex);
        
        listAdapter = new TextListAdapter(this);
        listView = (TextListView) findViewById(R.id.list);
        listView.setDividerHeight(0);
        listView.setAdapter(listAdapter);

        listView.setOnScrollListener(listViewListner);
        listView.setOnItemLongClickListener(listViewListner);
        listView.setOnItemClickListener(listViewListner);
        listView.setOnItemSelectedListener(listViewListner);

        
        //this.currentItemPosition = currentPosition;
        //listView.s(firstPostion);

        listSeek.setMax(listView.getCount());
        
        setLocation(firstPostion);
        //listView.setSelection(firstPostion);
        //listSeek.setProgress(firstPostion);
      
        TextView title = (TextView) findViewById(R.id.title);
        title.setText(getFileShortName(fileName));


        //获得传感器管理器
        /* sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager != null) {
            //获得重力传感器
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        }*/
        //注册
        //registerSensor();

    }

    /**
     * onActivityResult
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == FileBrowserActivity.CODE_FILE_BROWSER) {
            if (resultCode == FileBrowserActivity.RESULT_OK) {
                String fileName = data.getExtras().getString(FileBrowserActivity.KEY_FILENAME);
                load(fileName);
            }
        }
        if (requestCode == FileBrowserActivity.CODE_HISTORY_BROWSER) {
            if (resultCode == FileBrowserActivity.RESULT_OK) {
                String fileName = data.getExtras().getString(FileBrowserActivity.KEY_FILENAME);
                loadHistory(fileName);
            }
        }
        if (requestCode == SettingActivity.CODE_SETTING) {
            if (resultCode == SettingActivity.RESULT_OK) {
               // String fileName = data.getExtras().getString(FileBrowserActivity.KEY_FILENAME);
                load("");
            }
        }
        if (requestCode == MyTextToSpeech.REQ_CHECK_TTS_DATA) {
            switch (resultCode) {
                case TextToSpeech.Engine.CHECK_VOICE_DATA_PASS: // TTS引擎可用
                    // 针对于重新绑定引擎，需要先shutdown()
                    if(isSetting)
                    {
                    	isSetting = false;
                    	PlayHelper.reInitTts();
                    }
                    break;
                case TextToSpeech.Engine.CHECK_VOICE_DATA_BAD_DATA: // 数据错误
                case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_DATA: // 缺失数据资源
                case TextToSpeech.Engine.CHECK_VOICE_DATA_MISSING_VOLUME: // 缺少数据存储量
                    notifyReinstallDialog(); // 提示用户是否重装TTS引擎数据的对话框
                    break;
                case TextToSpeech.Engine.CHECK_VOICE_DATA_FAIL: // 检查失败
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
     * 使用GestureDetector检测手势（ScrollView内也需监听时的方式）
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        gestureDetector.onTouchEvent(ev);
        //scrollView.onTouchEvent(ev);
        //playSelected();
        //DrmStore.Action. ev.getAction();
        return super.dispatchTouchEvent(ev);
    }


    static final int defaultPosition = 0;
   // public int currentItemPosition = defaultPosition;
    //public int currentWordStart = defaultPosition;
    //public int currentWordEnd = defaultPosition;
    //int currentPosition = defaultPosition;

    int seekStep = 1;


    public void refreshListView()
    {
        refreshListView(false);
    }
    //return is current item play end
    public synchronized void refreshListView(boolean manual) {
        try {
            if (listView == null)
                return;
            
            int currentItemPosition = PlayHelper.getCurrentPosition();

            if (currentItemPosition < 0 || currentItemPosition > listView.getCount() - 1)
            {
                //currentItemPosition = listView.getFirstVisiblePosition();
                //return ;
            	return;
            }

            if (currentItemPosition == defaultPosition) {
                //初始化状态下，无效
                //currentItemPosition = listView.getFirstVisiblePosition();
            	return;
            }

           

          /*20140405  if (currentItemPosition >= listView.getLastVisiblePosition()) {
                scroll(currentItemPosition + maxPosition);
                return ;
            }

            if (currentItemPosition < listView.getFirstVisiblePosition()) {
                scroll(currentItemPosition);
                //滚完再Play
                return ;
            }*/


           /* View view = listView.getChildAt(currentItemPosition - listView.getFirstVisiblePosition());
            if (view != null) {
            	 TextViewer content = (TextViewer) view.findViewById(R.id.content);
                 if (content != null){

	                 //貌似会丢失，重新赋值以确保没有问题；
	                 content.setViewIndex(currentItemPosition);
	                 
	                 refreshTextViewer(content);
	                 //已经精细滚动，直接返回
	                 if(scrollDistance(content))
	                 {
	                 	return ;
	                 }
                 }
            }*/
            TextViewer content = getTextViewerByIndex(currentItemPosition - listView.getFirstVisiblePosition());
            if (content != null){

                //貌似会丢失，重新赋值以确保没有问题；
                content.setViewIndex(currentItemPosition);
                
                refreshTextViewer(content);
                //已经精细滚动，直接返回
                if(scrollDistance(content))
                {
                	return ;
                }
            }
           
            //20140405
            if(currentItemPosition > listView.getLastVisiblePosition()
                  ||currentItemPosition < listView.getFirstVisiblePosition())
            {
              //listView.setSelection(currentItemPosition);
              //progressListSeek();
            	setLocation(currentItemPosition);
            	return;
            }
            else
            {
            	//2014.04.12
            	setSeekLocation(currentItemPosition);
            }
         
            //PlayHelper.playCurrentWord();
//            doPlay(content,manual);
            
        } catch (Exception e) {
            globalUtil.log(e);
        }
    }
    
    public void refreshTextViewer(TextViewer content) {
    	  if (currentViewer != null && currentViewer != content)
          {
              currentViewer.changeSelected(false);
             // content.changeSelected(true);
          }
         // if(currentViewer == null)

          currentViewer = content;

         
          content.changeSelected(true);
          

		
	}
    public int getFootHeight()
    {
    	return footer.getVisibility()!= View.VISIBLE?0:footer.getHeight();
    }
	private boolean scrollDistance(TextViewer view) {
		// TODO Auto-generated method stub
		  try{
		    int y = view.getLocationY();
		    distance = y - (display.getHeight() - getFootHeight());
		    if(distance > 0)
		    {
		    	distance = y - getHeaderHeight();
		    	handler.post(results);
		    	return true;
		    }
		    if(y<=getHeaderHeight()){
		    	//listView.smoothScrollBy( , 0);
		    	distance = y- getHeaderHeight() ;
		    	handler.post(results);
		    	return true;
		    }
		  }catch(Exception e)
		  {
			  globalUtil.log(e);
		  }
		  return false;
	}
	
	int distance = 0;
	 final Handler handler = new Handler();
	    final Runnable results = new Runnable() {
	        public void run() {
	        	listView.smoothScrollBy( distance, 0);
	        }
	    };
	    
	    static final double scrollPagePercent = 1;
	    
	    public TextViewer getTextViewerByIndex(int position)
	    {
	    	View view = (View) listView.getChildAt(position);
			if (view != null) {
           	 TextViewer content = (TextViewer) view.findViewById(R.id.content);
               	return content;
                
			}
			return null;
	    }
	public void scrollPage(boolean down)
	{
		if(listView == null) return ;
		
		if(lineHeight == 0)
		{
			
            TextViewer content = getTextViewerByIndex(0);
            if(content!=null)
            	lineHeight = content.getLineHeight();
		}
		if(down)
		{
			listView.smoothScrollBy((int) ((display.getHeight() - getHeaderHeight() - getFootHeight() - lineHeight )*scrollPagePercent ), 0);
		}
		else
		{
			listView.smoothScrollBy((int) ((display.getHeight() - getHeaderHeight() - getFootHeight() - lineHeight)*-scrollPagePercent), 0);
		}
		
	}
	

    private int lineHeight = 0;
	public int getLineHeight() {
		return lineHeight;
	}

	public void setLineHeight(int lineHeight) {
		this.lineHeight = lineHeight;
	}

	private TextViewer currentViewer = null;

  

   // int maxPosition = 100;

    public void play() {
       PlayHelper.play();
        refreshListView();
      
    }

    public void stop() {
        PlayHelper.stop();
    }


    public void playItem(int position,int charIndex,boolean manual) {

    	if(manual) PlayHelper.setPlay(true);
        PlayHelper.movePlay(position, charIndex);
       // PlayHelper.playCurrentWord();
        refreshListView(manual);
    }


    static GlobalConfig config = GlobalConfig.getInstance();
    public void playNext(View view){
            playNext(config.getSeekStep());
//        if(!config.isExchangePreNext())
//            playNext(config.getSeekStep());
//        else
//            playNext(-config.getSeekStep());
    }
    public void playPre(View view){
            playNext(-config.getSeekStep());
//        if(!config.isExchangePreNext())
//            playNext(-config.getSeekStep());
//        else
//            playNext(config.getSeekStep());
    }
    public void playNext(int seekStep) {
       
       PlayHelper.movePlay(seekStep);
      
       refreshListView();
    }

 /*   public void playPre(int seekStep) {
    	PlayHelper.movePlay(seekStep);
       
        refreshListView();
    }*/


    /**
     * 校验TTS引擎安装及资源状态
     */
    private boolean checkTtsData() {
        try {
            Intent checkIntent = new Intent();
            checkIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
            startActivityForResult(checkIntent, MyTextToSpeech.REQ_CHECK_TTS_DATA);
            return true;
        } catch (ActivityNotFoundException e) {
            return false;
        }
    }

    /**
     * 提示用户是否重装TTS引擎数据的对话框
     */
    private void notifyReinstallDialog() {
        new AlertDialog.Builder(this).setTitle("TTS引擎数据错误")
                .setMessage("是否尝试重装TTS引擎数据到设备上？")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 触发引擎在TTS引擎在设备上安装资源文件
                        Intent dataIntent = new Intent();
                        dataIntent
                                .setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                        startActivity(dataIntent);
                    }
                }).setNegativeButton("否", null).show();
    }

    /**
     * 跳转到“语音输入与输出”设置界面
     */
    public void toTtsSettings(View v) {
        try {
        	isSetting = true;
            startActivity(new Intent("com.android.settings.TTS_SETTINGS"));
            // return true;
        } catch (ActivityNotFoundException e) {
            // return false;
        }
    }
    public void volumeSettings(View v) {
        try {
        	if(audioManager == null)
        		audioManager =(AudioManager)getSystemService(Context.AUDIO_SERVICE); 
        	audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, audioManager.getStreamVolume(AudioManager.STREAM_MUSIC), AudioManager.FLAG_SHOW_UI);
            //isPlay = false;
            // return true;
        } catch (ActivityNotFoundException e) {
            // return false;
        }
    }
    /*

    private void hideIM(View edt) {

        // try to hide input_method:
        try {
            InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);

            IBinder windowToken = edt.getWindowToken();
            if (windowToken != null) {

                // always de-activate IM
                im.hideSoftInputFromWindow(windowToken, 0);
            }

        } catch (Exception e) {

            globalUtil.log(e);
        }
    }


    private View.OnFocusChangeListener focusListener = new View.OnFocusChangeListener() {

        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus == true) {
                hideIM(v);
            }
        }
    };

    private OnTouchListener touchListener = new OnTouchListener() {

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                hideIM(v);
            }
            return false; // dispatch the event further!
        }
    };
*/

    public void save(View v) {
        save();
    }

    public void load(View v) {
        load("");
    }
    

    
    boolean isRegister = false;
    public void register(View v)
    {

    	//isRegister = !isRegister;
    	if(!isRegister)
    	{
    		registerReceiver();
        	globalUtil.ShowMessage(getApplicationContext(), "注册媒体键，开始监听");
    	}
    	else
    	{
    		unRegisterReceiver();
        	globalUtil.ShowMessage(getApplicationContext(), "取消注册，释放媒体键");
    	}
    }

    private DevicePolicyManager policyManager; 
    private ComponentName componentLockName; 
    public void lock(View v) {
    	//toLock = true;
    	tryLock();
    	
    	/* if (policyManager.isAdminActive(componentLockName)) {//判断是否有权限(激活了设备管理器) 
            policyManager.lockNow();// 直接锁屏 
           // android.os.Process.killProcess(android.os.Process.myPid()); 
        }else{ 
            activeManager();//激活设备管理器获取权限 
        } */
    }
    
    private void activeManager() { 
        //使用隐式意图调用系统方法来激活指定的设备管理器 
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN); 
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentLockName); 
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "偶读"); 
        startActivity(intent); 
    } 

    String getFileShortName(String filePath)
    {
    	return filePath.substring(filePath.lastIndexOf(globalUtil.pathSplit)+1);
    }
    void save() {
        try {
            if(textUtil == null) return;

            String filePath = textUtil.getFilePath();
            String fileName = getFileShortName(filePath);

            String savePath = globalUtil.getHistoryFilePath(fileName);
            File file = new File(globalUtil.getHistoryFilePath(""));
            if (!file.exists())
                file.mkdirs();
            file = new File(savePath);
            if (!file.exists()) {
                file.createNewFile();
                // file.
            }

            PropertyUtil propertyUtil = PropertyUtil.getInstance(savePath);
            propertyUtil.set(filePathKey, filePath);
            propertyUtil.set(encodingKey, textUtil.getEncoding());
            propertyUtil.set(firstPositionKey, String.valueOf(listView.getFirstVisiblePosition()));
            propertyUtil.set(currentPositionKey, String.valueOf(PlayHelper.getCurrentPosition()));
            propertyUtil.set(currentIndexKey, String.valueOf(PlayHelper.getCurrentIndex()));
            propertyUtil.store();

            //GlobalConfig.getInstance().save();
            /*FileOutputStream fileOutputStream = new FileOutputStream(savePath);

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(fileOutputStream));
            writer.write(filePath);
            writer.write(globalUtil.line);
            writer.write(textUtil.getEncoding());
            writer.write(globalUtil.line);
            writer.write(String.valueOf(listView.getFirstVisiblePosition()));
            writer.write(globalUtil.line);
            writer.write(String.valueOf(currentPosition));
            writer.write(globalUtil.line);

            writer.flush();
            writer.close();  */

        } catch (Exception e) {
            globalUtil.log(e);
        }

    }


    void load(String fileName) {
        try {

        	//保存当前文件进度
        	//save();
        	
            String saveFileName = "";
            //确定保存的历史文件路径
            if (globalUtil.isStringEmpty(fileName)) {
                File[] historyFiles = new File(globalUtil.getHistoryFilePath("")).listFiles();
                //String lastPath = "";
                Date lastSave = null;
                Date currentSave = null;
                for (int i = 0; i < historyFiles.length; i++) {
                    currentSave = new Date(historyFiles[i].lastModified());

                    if (lastSave == null || currentSave.after(lastSave)) {
                        lastSave = currentSave;
                        saveFileName = historyFiles[i].getPath();
                    }
                }
            } else {
            	//fileName.substring(fileName.lastIndexOf(globalUtil.pathSplit))
                File file = new File(globalUtil.getHistoryFilePath(getFileShortName(fileName)));
                if (file.exists())
                    saveFileName = file.getPath();

            }

            if (globalUtil.isStringEmpty(saveFileName)) {
                //没有打开过，直接打开
                fileOpen(fileName, "", 0, 0,0);
                return;
            }
            else
                loadHistory(saveFileName);

            //FileInputStream fileInputStream = new FileInputStream(saveFileName);
            //BufferedReader reader = new BufferedReader(new InputStreamReader(fileInputStream));

//            String destfileName = reader.readLine();
//            String encoding = reader.readLine();
//            int firstPostion = Integer.parseInt(reader.readLine());
//            int currentPostion = Integer.parseInt(reader.readLine());

            //保存读取记录
           // save();

        } catch (Exception e) {
            globalUtil.log(e);
            fileOpen(fileName, "", 0,0, 0);
        }

    }

	private void loadHistory(String historyFile) {
		PropertyUtil propertyUtil = PropertyUtil.getInstance(historyFile);
		String destfileName = propertyUtil.get(filePathKey);
		String encoding = propertyUtil.get(encodingKey);
		int firstPosition = Integer.parseInt(propertyUtil.get(firstPositionKey, "0"));
		int currentPosition = Integer.parseInt(propertyUtil.get(currentPositionKey, "0"));
		int currentIndex = Integer.parseInt(propertyUtil.get(currentIndexKey, "0"));
		//if(currentPosition < firstPosition || currentPosition > firstPosition + maxPosition)
		//    firstPosition = currentPosition;

		//GlobalConfig.getInstance();
		fileOpen(destfileName, encoding, currentPosition, currentIndex, firstPosition);
	}



    static final String filePathKey = "filePath";
    static final String encodingKey = "encoding";
    static final String firstPositionKey = "firstPosition";
    static final String currentPositionKey = "currentPosition";
    static final String currentIndexKey = "currentIndex";


    @Override
    protected void onStart() {
    	//TODO 要命的一句！！！害死人
        checkTtsData(); // 校验TTS引擎安装及资源状态
        super.onStart();
    }

    
   // boolean toLock = false;
    public boolean tryLock()
    {
    	try{
    	//if(!toLock) return false;
    /*	if(policyManager == null)
    	policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
    	if(componentLockName == null)
        componentLockName = new ComponentName(this, AdminReceiver.class); */
      /* if(Build.VERSION.SDK_INT > 8)
       {
    	  StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
          StrictMode.setThreadPolicy(policy);
       }  */
     
      
    	 if (policyManager.isAdminActive(componentLockName) ) { 

    		 //activeManager();
            // toLock = false;
             policyManager.lockNow(); 
             //policyManager.removeActiveAdmin(componentName);
            // android.os.Process.killProcess(android.os.Process.myPid()); 
         } 
    	 else
    	 {
    		 activeManager();
    	 }
    	 
    	 return true;
    	}
    	catch(Exception e)
    	{
    		Log.i(TAG,e.getMessage());
    		return false;
    	}
    }
    @Override
    protected void onResume() {

        save();
        
        /* 从设置返回后重新绑定TTS，避免仍用旧引擎 */
        if (isSetting) {
            //isSetting = false;
            checkTtsData(); // 校验TTS引擎安装及资源状态
        }
       
        //tryLock();
        super.onResume();
    }


    @Override
    protected void onDestroy() {
        /* HOME键 */
    	//save();
        
    	//finalWork();
        //super.onDestroy();
    	//这方法相当变态，每back记录要触发一次，但是往往是在onCreate后才触发，可能是因为请求新资源，才释放旧资源，所以很不可靠
    	//所以还是直接用exitProgram强行退出为好，这个方法留给系统触发，不安排太多逻辑。
    	//万一被HOME键触发，不删除，只是停止；
    	save();
    	//super.onStop();
    	super.onDestroy();
    }

    private void finalWork() {

        save();
        PlayHelper.finalWork();
        unRegisterReceiver();
    }


    /*  public void unregisterSensor()
    {
        if(sensorManager != null && listViewListner != null)
            sensorManager.unregisterListener(listViewListner);
    }

    public void registerSensor()
    {
        if(sensorManager != null && listViewListner != null && sensor != null) {
            sensorManager.registerListener(listViewListner, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }

    }*/

   // boolean readyBack;    
    @Override
    public void onBackPressed() {
        /* BACK键 */
        //save();

    	if(!readyToBack(this))
    	{
    		save();
    		return;
    	}
    	
    	//确认后，回收所有资源，强行退出
    	finalWork();
    	exitProgrames();
        //PlayHelper.finalWork();
        //super.onBackPressed();
    }

//模拟home键效果，暂时不用
public void home() { 
    //实现Home键效果 
    //super.onBackPressed();这句话一定要注掉,不然又去调用默认的back处理方式了 
    Intent i= new Intent(Intent.ACTION_MAIN); 
    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
    i.addCategory(Intent.CATEGORY_HOME); 
    startActivity(i);  
}

//退出程序
public void exitProgrames(){ 
    Intent startMain = new Intent(Intent.ACTION_MAIN); 
    startMain.addCategory(Intent.CATEGORY_HOME); 
    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); 
    startActivity(startMain); 
    android.os.Process.killProcess(android.os.Process.myPid()); 
}

    static Date lastTime = null;
	public boolean readyToBack(Context context) {
		if(lastTime == null || new Date().getTime() > 2000 + lastTime.getTime())
    	{
    		//readyBack = true;
			lastTime = new Date();
			globalUtil.ShowMessage(context, R.string.ready_back);
    		

			return false;
    	}
    		
		/*if(tryLock())
			return false;
		*/
		//toLock = false;
		return true;
	}

    public TextListView getListView() {
        return listView;
    }


    public TextUtil getTextUtil() {
        return textUtil;
    }

    
    
    public void setLocation(int position)
    {

   	 	setListLocation(position);
    	setSeekLocation(position);
    }
    
    
    public void setSeekLocation(int position) {
    	//仅仅更新滚动条，列表根据滚动条更新，更安全，不过慢
      	 if (listSeek != null && listSeek.getProgress()!=position)
            listSeek.setProgress(position);
   	
      	 if(listView !=null)
      		 location.setText("第 " + String.valueOf(position) + "/" + listView.getCount() + " 段");
    
	}

	//const String locationFormat = "第 %s/{1}"
    //这个定位比较粗犷，只有在第一次加载，滚出屏幕，拖动滚动条或搜索的时候触发
    private void setListLocation(int position) {
		// TODO Auto-generated method stub
    	 if( listView != null && listView.getFirstVisiblePosition() != position)
    	 {
    		 listView.setSelectionFromTop(position,getHeaderHeight());
    		 location.setText("第 " + String.valueOf(position) + "/" + listView.getCount() + " 段");
    		      
    	 }
	}

	public  int getHeaderHeight()
    {
    	int headerHeight = header.getVisibility() != View.VISIBLE ? 0:header.getHeight();
		 return headerHeight;
    }
  
    private SeekBar.OnSeekBarChangeListener seekListner = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {

        	if (seekBar.equals(listSeek) && i != -1 && fromUser)
            {
            	//if(i!=listView.setSel)
                //listView.setSelection(i);
            	//setLocation(i);
            	 setListLocation(i);
            }

          

            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //To change body of implemented methods use File | Settings | File Templates.
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

            //To change body of implemented methods use File | Settings | File Templates.
        }
    };


    public boolean isGestureValid(MotionEvent e)
    {
    	return e.getY() > getHeaderHeight() && e.getY() < display.getHeight() - getFootHeight();
    }
    public ListViewListener getListViewListner() {
        return listViewListner;
    }
    
    

	private static Method sRegisterMediaButtonEventReceiver;
	private static Method sUnregisterMediaButtonEventReceiver;
	//private ComponentName mRemoteControlResponder;
	private static void initializeRemoteControlRegistrationMethods() {
		try {
			if (sRegisterMediaButtonEventReceiver == null) {
				sRegisterMediaButtonEventReceiver = AudioManager.class
						.getMethod("registerMediaButtonEventReceiver",
								new Class[] { ComponentName.class });
			}
			if (sUnregisterMediaButtonEventReceiver == null) {
				sUnregisterMediaButtonEventReceiver = AudioManager.class
						.getMethod("unregisterMediaButtonEventReceiver",
								new Class[] { ComponentName.class });
			}
		} catch (NoSuchMethodException nsme) { /* Ignored */ }
	}

	static {
		initializeRemoteControlRegistrationMethods();
	}

	final String TAG = "TtsActivity";
	public void registerRemoteControl() {
		Log.d(TAG, "Registering remote control listener");
        try {
            if (sRegisterMediaButtonEventReceiver == null) {
                return;
            }
            sRegisterMediaButtonEventReceiver.invoke(audioManager,
            		eventReceiver);
        } catch (InvocationTargetException ite) {
            /* unpack original exception when possible */
            Throwable cause = ite.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            } else {
                /* unexpected checked exception; wrap and re-throw */
                throw new RuntimeException(ite);
            }
        } catch (IllegalAccessException ie) {
            Log.e(TAG, "unexpected " + ie);
        }
    }
    
    public void unregisterRemoteControl() {
    	Log.d(TAG, "Unregistering remote control listener");
        try {
            if (sUnregisterMediaButtonEventReceiver == null) {
                return;
            }
            sUnregisterMediaButtonEventReceiver.invoke(audioManager,
            		eventReceiver);
        } catch (InvocationTargetException ite) {
            /* unpack original exception when possible */
            Throwable cause = ite.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            } else if (cause instanceof Error) {
                throw (Error) cause;
            } else {
                /* unexpected checked exception; wrap and re-throw */
                throw new RuntimeException(ite);
            }
        } catch (IllegalAccessException ie) {
            System.err.println("unexpected " + ie);  
        }
    }

    
    
}
