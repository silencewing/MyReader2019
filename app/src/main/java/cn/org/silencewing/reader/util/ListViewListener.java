package cn.org.silencewing.reader.util;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.text.Spannable;
import android.text.Spanned;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import cn.org.silencewing.reader.R;
import cn.org.silencewing.reader.ReadActivity;
import cn.org.silencewing.reader.widget.TextViewer;


public class ListViewListener implements AdapterView.OnItemLongClickListener
        ,AdapterView.OnItemClickListener
        ,AdapterView.OnItemSelectedListener
        ,EditText.OnClickListener
        ,EditText.OnLongClickListener
      //  ,EditText.OnTouchListener
        , AbsListView.OnScrollListener, SensorEventListener {


    ReadActivity context;

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        //To change body of implemented methods use File | Settings | File Templates.
    	//滚动到达预定位置
      /*  if( i == PlayHelper.getCurrentPosition())
       {
        TextViewer holder = (TextViewer) view.findViewById(R.id.content);
        holder.setViewIndex(i);
       	 context.refreshTextViewer(holder);
       	// holder.setSelection(context.currentWordStart);
       	
       }*/
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public ListViewListener(ReadActivity context) {
        this.context = context;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

        try {
        	TextViewer holder = (TextViewer)view.findViewById(R.id.content);
            context.playItem(i,holder.getSelectionStart(),true);
        	//context.play();
            return false;  //To change body of implemented methods use File | Settings | File Templates.
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
     /*  TextViewer viewer = (TextViewer) adapterView.getItemAtPosition(i);

        Spannable content = viewer.getText();
        int startIndex = viewer.getSelectionStart();
        int endIndex = viewer.getSelectionEnd();

        content.setSpan(viewer.normalTextColorSpan, startIndex,endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        content.setSpan(viewer.normalBackgroundColorSpan, startIndex,endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);  */
    }

    @Override
    public void onScrollStateChanged(AbsListView absListView, int scrollState) {
    /*    if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
            return;
        }*/
        //To change body of implemented methods use File | Settings | File Templates.
        if (scrollState == SCROLL_STATE_IDLE) {
        	context.setSeekLocation(absListView.getFirstVisiblePosition());
        }
    }

    @Override
    public void onScroll(AbsListView absListView, int i, int i1, int i2) {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    //速度阈值，当摇晃速度达到这值后产生作用
    private static final int SPEED_SHRESHOLD = 500;
    //两次检测的时间间隔
    private static final int MIN_UPTATE_INTERVAL_TIME = 80;
    private static final int MAX_UPTATE_INTERVAL_TIME = 200;


    //手机上一个位置时重力感应坐标
    private float lastX;
    private float lastY;
    private float lastZ;

    //上次检测时间
    private long lastUpdateTime;

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        // if(sensorEvent.timestamp = new Timestamp())


        //To change body of implemented methods use File | Settings | File Templates.
        //sensorEvent.sensor = Sensor.

        //if(sensorEvent.values[0]+sensorEvent.values[0]+sensorEvent.values[0] >10+9.81)
//现在检测时间
        long currentUpdateTime = System.currentTimeMillis();
        //两次检测的时间间隔
        long timeInterval = currentUpdateTime - lastUpdateTime;
        //判断是否达到了检测时间间隔
        if (timeInterval < MIN_UPTATE_INTERVAL_TIME)
            return;

        // if(Math.abs(sensorEvent.values[SensorManager.DATA_Z] - 9.81f) < 1.5 )
        //    return ;

        //现在的时间变成last时间
        lastUpdateTime = currentUpdateTime;

        //获得x,y,z坐标
        float x = sensorEvent.values[0];
        float y = sensorEvent.values[1];
        float z = sensorEvent.values[2];

        //获得x,y,z的变化值
        float deltaX = x - lastX;
        float deltaY = y - lastY;
        float deltaZ = z - lastZ;

        //将现在的坐标变成last坐标
        lastX = x;
        lastY = y;
        lastZ = z;


        if (timeInterval > MAX_UPTATE_INTERVAL_TIME)

            return;
        double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ) / timeInterval * 10000;
        //达到速度阀值，发出提示
        if (speed >= SPEED_SHRESHOLD) {
            /*context.unregisterSensor();
            try{
            context.changePlay();
            }
            catch (Exception e)
            {
                GlobalUtil.getInstance().log(e);
            }
            context.registerSensor();*/
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
        //To change body of implemented methods use File | Settings | File Templates.
        //Accuracy.

    }

    @Override
    public void onClick(View view) {
        //view
        /*TextViewer viewer = (TextViewer)view;
        //TextViewer viewer = (TextViewer) adapterView.getItemAtPosition(i);

        Spannable content = viewer.getText();
        int startIndex = viewer.getSelectionStart();
        int endIndex = viewer.getSelectionEnd();

        content.setSpan(viewer.selectTextColorSpan, startIndex,endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        content.setSpan(viewer.selectBackgroundColorSpan, startIndex,endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

         */
    }

    @Override
    public boolean onLongClick(View view) {
       /* TextViewer viewer = (TextViewer)view;
        //TextViewer viewer = (TextViewer) adapterView.getItemAtPosition(i);

        Spannable content = viewer.getText();
        int startIndex = viewer.getSelectionStart();

       */
        //this.context.playItem(this.context.getListView().fin);
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
