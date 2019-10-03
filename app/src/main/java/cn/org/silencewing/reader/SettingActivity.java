package cn.org.silencewing.reader;

import cn.org.silencewing.reader.R;
import cn.org.silencewing.reader.util.GlobalConfig;
import cn.org.silencewing.reader.util.GlobalUtil;
import cn.org.silencewing.reader.util.PlayHelper;
import cn.org.silencewing.reader.widget.ColorPickerDialog;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

public class SettingActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
	//static TtsActivity
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
 
        setContentView(R.layout.setting);
        
        Window window = getWindow();
        WindowManager wm = getWindowManager();  
        Display d = wm.getDefaultDisplay();  //为获取屏幕宽、高
        window.setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND); // 背景模糊
        
          
        LayoutParams p = window.getAttributes();  //获取对话框当前的参数值  
        p.height = LayoutParams.WRAP_CONTENT;   //高度设置为屏幕的0.6  
        p.width = (int) (d.getWidth() * 0.9);    //宽度设置为屏幕的0.95
        
          
        getWindow().setAttributes(p);  
        window.setLayout((int)(window.getAttributes().width*0.9), (int)(window.getAttributes().height));
   
        
        init();

    }

    SeekBar speechRateSeek,volumeSeek,textSizeSeek,lineSpaceSeek,itemPadSeek,seekStepSeek;
    CheckBox volumeToCtrl,exchangePreNext;
    EditText root; 
   // textColorSeek, backgroundColorSeek; // 音量&语速
    
    public static final String KEY_SETTING = "setting";
    public static final int CODE_SETTING = 200;
    public static GlobalConfig config = GlobalConfig.getInstance();
    public void setTextColor(View view)
    {
    	 ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this
    			 ,config.getTextColor()
    			 , "字体颜色"
    			 , new ColorPickerDialog.OnColorChangedListener() {
					
					@Override
					public void colorChanged(int color) {
						// TODO Auto-generated method stub
						config.setTextColor(color);
						
					}
				});
    	 colorPickerDialog.show();
    }
    
    public void setBackgroundColor(View view)
    {
    	 ColorPickerDialog colorPickerDialog = new ColorPickerDialog(this
    			 ,config.getBackgroundColor()
    			 , "背景颜色"
    			 , new ColorPickerDialog.OnColorChangedListener() {
					
					@Override
					public void colorChanged(int color) {
						// TODO Auto-generated method stub
						config.setBackgroundColor(color);
						
					}
				});
    	 colorPickerDialog.show();
    }
    
    
    public void confirm(View view)
    {
    	config.setRoot(root.getText().toString());
    	config.setVolumeToCtrl(volumeToCtrl.isChecked());
        config.setExchangePreNext(exchangePreNext.isChecked());

        config.save();
    	  Intent data = new Intent();
          data.putExtra(KEY_SETTING, "OK");
          setResult(RESULT_OK, data);
          finish();
    }
    public void cancel(View view)
    {

        config.load();
    	  Intent data = new Intent();
          data.putExtra(KEY_SETTING, "CANCEL");
          setResult(RESULT_CANCELED, data);
          finish();
    }
    void init()
    {
    	 speechRateSeek = (SeekBar) findViewById(R.id.speechRateSeek);
         textSizeSeek = (SeekBar) findViewById(R.id.textSizeSeek);
         lineSpaceSeek = (SeekBar) findViewById(R.id.lineSpaceSeek);
         itemPadSeek = (SeekBar) findViewById(R.id.itemPadSeek);
         seekStepSeek = (SeekBar) findViewById(R.id.seekStepSeek);
         volumeToCtrl = (CheckBox) findViewById(R.id.volumeToCtrl);
        exchangePreNext = (CheckBox) findViewById(R.id.exchangePreNext);
         root = (EditText) findViewById(R.id.root);
         
         speechRateSeek.setProgress((int) (config.getSpeechRate() * 10));
         textSizeSeek.setProgress((int) config.getTextSize());
         lineSpaceSeek.setProgress((int) config.getLineSpace());
         itemPadSeek.setProgress((int) config.getItemPad());
         seekStepSeek.setProgress((int) config.getSeekStep());
         volumeToCtrl.setChecked((boolean)config.isVolumeToCtrl());
         exchangePreNext.setChecked((boolean)config.isExchangePreNext());
         root.setText(config.getRoot());
         

         speechRateSeek.setOnSeekBarChangeListener(seekListener);
         textSizeSeek.setOnSeekBarChangeListener(seekListener);
         lineSpaceSeek.setOnSeekBarChangeListener(seekListener);
         itemPadSeek.setOnSeekBarChangeListener(seekListener);
         seekStepSeek.setOnSeekBarChangeListener(seekListener);
         
         
        }
    static final GlobalUtil globalUtil = GlobalUtil.getInstance();
    SeekBar.OnSeekBarChangeListener seekListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        	if (seekBar.equals(textSizeSeek)) {
                config.setTextSize(i);

            }
        	if (seekBar.equals(lineSpaceSeek)) {
                config.setLineSpace(i);

            }
        	if (seekBar.equals(itemPadSeek)) {
                config.setItemPad(i);

            }
        	if (seekBar.equals(seekStepSeek)) {
                config.setSeekStep(i);

            }
            if (seekBar.equals(speechRateSeek)) {
                config.setSpeechRate(((float) i) / 10);
                //isPlay = false;
                //PlayHelper.reInitTts();
            }
            
            //Toast.makeText(getApplicationContext(),String.valueOf(seekBar.getProgress()), Toast.LENGTH_SHORT).show();
            
           /* if (seekBar.equals(volumeSeek)) {
                //config.setSpeechRate(((float) i) / 10);
                //isPlay = false;
                //tts.initTts();
            	audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, i, AudioManager.FLAG_SHOW_UI);
            	isPlay = false;
            }
               if (seekBar.equals(textColorSeek)) {
                if (i == 0)
                    i = 1;
                config.setTextColor(i * (-1));
            }

            if (seekBar.equals(backgroundColorSeek)) {
                if (i == 0)
                    i = 1;
                config.setBackgroundColor(i * (-1));
            }
            */

         
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
            //To change body of implemented methods use File | Settings | File Templates.
        	 Toast.makeText(getApplicationContext(),String.valueOf(seekBar.getProgress()), Toast.LENGTH_SHORT).show();
             
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        	//globalUtil.ShowMessage(getApplicationContext(),String.valueOf(i));
        	 Toast.makeText(getApplicationContext(),String.valueOf(seekBar.getProgress()), Toast.LENGTH_SHORT).show();
             	
            //To change body of implemented methods use File | Settings | File Templates.
        }
    };
    ;
}
