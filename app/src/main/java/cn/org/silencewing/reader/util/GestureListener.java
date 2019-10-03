package cn.org.silencewing.reader.util;

import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import cn.org.silencewing.reader.ReadActivity;
import cn.org.silencewing.reader.widget.TextListView;


public class GestureListener extends GestureDetector.SimpleOnGestureListener {


    private ReadActivity context;


    public GestureListener(ReadActivity context) {

        this.context = context;

    }


    
    @Override
    public boolean onDown(MotionEvent e) {
        //if(context.isPlayScroll())
        //    return true;

        return false;
    }

   
    @Override
    public void onShowPress(MotionEvent e) {

    }


    @Override

    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }


   // boolean scroll = false;
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //if(context.isPlayScroll())
        //    return true;


        // TextUtil textUtil = this.context.getTextUtil();
        //this.context.getContent().append(textUtil.appendContent());
    	/*if(context.isPlay())
    	{
    		scroll = true;
    		return true;
    	}*/
        return false;

    }


    @Override
    public void onLongPress(MotionEvent e) {
       // this.context.playSelected();
    }


    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        //  if(context.isPlayScroll())
        //      return true;
    	
    	/*if(context.isPlay())
    	{
    		scroll = true;
    		return true;
    	}*/
        return false;

    }


    @Override
    public boolean onDoubleTap(MotionEvent e) {
        //Toast.makeText(mContext, "DOUBLE " + e.getAction(), Toast.LENGTH_SHORT).show();

        return false;

    }


    @Override
    public boolean onDoubleTapEvent(MotionEvent e) {
        //Toast.makeText(mContext, "DOUBLE EVENT " + e.getAction(), Toast.LENGTH_SHORT).show();

    	
        if (e.getAction() == MotionEvent.ACTION_UP && context.isGestureValid(e))
        {
            context.changePlay();
        return true;
        //return false;
        }
        return false;
    }


    @Override
    public boolean onSingleTapConfirmed(MotionEvent e) {
        //Toast.makeText(mContext, "SINGLE CONF " + e.getAction(), Toast.LENGTH_SHORT).show();

        //context.changeShow();
    	if(!context.isGestureValid(e))
    		return false;
    	
    	float y = e.getY();
    	float x = e.getX();
    	int height = context.display.getHeight();
    	int width = context.display.getWidth();
    	if(x > width /4 && x < width*3/4)
    		return false;
    	
    	down = (y>height/2);
    	handler.post(results);
    	
        return true;
    }

    boolean down = true;
    final Handler handler = new Handler();
    final Runnable results = new Runnable() {
        public void run() {
            context.scrollPage(down);
        	//PlayHelper.movePlay(1);
        }
    };



}
