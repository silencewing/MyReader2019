package cn.org.silencewing.reader.util;

import cn.org.silencewing.reader.ReadActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class HomeKeyReceiver extends BroadcastReceiver{  
	   
    static final String SYSTEM_REASON = "reason";    
       static final String SYSTEM_HOME_KEY = "homekey";//home key    
       static final String SYSTEM_RECENT_APPS = "recentapps";//long home key    
       //static 
      
   @Override 
   public void onReceive(Context context, Intent intent) {  
          
          
       // TODO Auto-generated method stub  
        String action = intent.getAction();    
           if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {    
               String reason = intent.getStringExtra(SYSTEM_REASON);    
               if (reason != null) {    
                   if (reason.equals(SYSTEM_HOME_KEY)) {    
                       // home key处理点    
                          
                       //停止服务  
                       //context.stopService(HLWelcomeActivity.m_intent_RotateMenuIntent);  
  
                	   //if(TtsActivity.readyToBack(context))
                	//	   return;
                       //Log.i("vvvvvvvvvv", "mmmmmm");  
                          
                   } else if (reason.equals(SYSTEM_RECENT_APPS)) {    
                       // long home key处理点    
                   }    
               }    
           }    
   }  
      
  
}