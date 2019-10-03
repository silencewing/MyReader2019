package cn.org.silencewing.reader;

import android.content.Context;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import cn.org.silencewing.reader.util.GlobalConfig;
import cn.org.silencewing.reader.util.GlobalUtil;
import cn.org.silencewing.reader.util.PlayHelper;

import java.io.BufferedReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-2
 * Time: 下午1:40
 * To change this template use File | Settings | File Templates.
 */
public class MyTextToSpeech implements TextToSpeech.OnInitListener, TextToSpeech.OnUtteranceCompletedListener {
    //private AudioManager audioManager; // 音频管理对象
    //private static final int STREAM_TTS = AudioManager.STREAM_MUSIC;
    private TextToSpeech tts;
    // TTS对象
    public static final int REQ_CHECK_TTS_DATA = 110;
    // TTS数据校验请求值

    private ReadActivity context;

    // 进入设置标记
    private boolean isRateChanged = false;
    // 被停止，则不触发事件
    private int batchCount = 0;
    //TTS引擎停止发声标记
    // private float speechRate = ;
    // 朗读速率
    //private SeekBar volumeBar, speedBar; // 音量&语速

    public void batchAdd() {
        batchCount = batchCount + 1;
    }

    public int getBatchCount() {
        return batchCount;
    }

    public void init(ReadActivity activity) {
       this.setContext(activity);
        this.initTts();
    }

    public void initTts() {
        // 针对于重新绑定引擎，需要先shutdown()
       /* if (null != tts) {
            tts.stop(); // 停止当前发声
            tts.shutdown(); // 释放资源
        }*/
    	shutDown();
        tts = new TextToSpeech(context, this);
    }


    @Override
    public void onInit(int status) {
        //To change body of implemented methods use File | Settings | File Templates.

        if (status == TextToSpeech.SUCCESS) {
            //mTts = new TextToSpeech(,this);
            if (GlobalConfig.getInstance().getSpeechRate() != 0)
                tts.setSpeechRate(GlobalConfig.getInstance().getSpeechRate()); // 设置朗读速率
            
            tts.setLanguage(Locale.CHINESE); //设置语言
            // 设置发声合成监听，注意也需要在onInit()中做才有效
            tts.setOnUtteranceCompletedListener(this);

        }
    }

    @Override
    public void onUtteranceCompleted(String s) {
 
        //继续下一段播放
        //if (context.isPlay() && !context.isPlayScroll() && 
    	if(PlayHelper.isPlay() &&
    			s.equals(String.valueOf(batchCount)))
            handler.post(results);
    }

    final Handler handler = new Handler();
    final Runnable results = new Runnable() {
        public void run() {
            context.playNext(1);
        	//PlayHelper.movePlay(1);
        }
    };

    public void play(int batchCount, String content, boolean isAdd) {
        try {
            if (!isAdd)
                stop();

            String title = String.valueOf(batchCount);

            if (GlobalUtil.getInstance().isStringEmpty(content)) {
                playOne(title, GlobalUtil.line);
                return;
            }
            BufferedReader reader = new BufferedReader(new StringReader(content));
            String temp = "";
            while (true) {
                temp = reader.readLine();
                //Selection.setSelection(content,);
                if (temp == null) {

                    break;
                }
                playOne(title, temp);
            }

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

    }

    /**
     * tts合成语音播放
     */
    private int playOne(String title, String content) {
        if (null != tts) {
            //isStopped = false; // 设置标记
            /**
             * 叙述text。
             *
             * 1） 参数2（int queueMode）
             *    1.1）QUEUE_ADD：增加模式。增加在队列尾，继续原来的说话。
             *    1.2）QUEUE_FLUSH：刷新模式。中断正在进行的说话，说新的内容。
             * 2）参数3（HashMap<String, String> params）
             *    2.1）请求的参数，可以为null。
             *    2.2）注意KEY_PARAM_UTTERANCE_ID。
             */
            try {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, title);
                /*if(isRateChanged)
                {
                    isRateChanged = false;
                    tts.setSpeechRate(GlobalConfig.getInstance().speechRate);
                }*/
                int result = tts.speak(content, TextToSpeech.QUEUE_ADD, params);
                //isStopped = false;
                return result;
            } catch (Exception exception) {
                return TextToSpeech.ERROR;
            }
        }
        return TextToSpeech.ERROR;
    }

    /**
     * 停止当前发声，同时放弃所有在等待队列的发声
     */
    public int stop() {
        //isStopped = true; // 设置标记
        return (null == tts) ? TextToSpeech.ERROR : tts.stop();
    }

    /**
     * 释放资源（解除语音服务绑定）
     */
    public void shutDown() {
        if (null != tts) {
        	if(tts.isSpeaking())
        		tts.stop();
            tts.shutdown();
            tts = null;
        }
    }

    public void setContext(ReadActivity context) {
        this.context = context;
    }

    public boolean isRateChanged() {
        return isRateChanged;
    }

    public void setRateChanged(boolean rateChanged) {
        isRateChanged = rateChanged;
    }

    /*  public boolean isStopped() {
        return isStopped;
    }

    public void setStopped(boolean stopped) {
        isStopped = stopped;
    }*/

    /* public float getSpeechRate() {
       return speechRate;
   }

   public void setSpeechRate(float mSpeechRate) {
       this.speechRate = mSpeechRate;
   } */

    /*

    public void playOnFocus()
    {
        View view = this.context.getWindow().getCurrentFocus();

        Spannable content =  ((EditText)view).getEditableText();
        Selection.getSelectionStart(content);
        //Selection.setSelection(content);
        //content.setSpan();

    }

     */

}
