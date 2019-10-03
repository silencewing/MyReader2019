package cn.org.silencewing.reader.widget;

import cn.org.silencewing.reader.R;
import cn.org.silencewing.reader.util.GlobalUtil;
import android.content.Context;
import android.graphics.Rect;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;


public class TextListView extends ListView {

    public TextListView(Context context) {
        super(context);
    }

    public TextListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TextListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


//    @Override
//    public boolean onInterceptTouchEvent(MotionEvent event)   //这个方法如果返回 true 的话 两个手指移动，启动一个按下的手指的移动不能被传播出去。
//    {
//        super.onInterceptTouchEvent(event);
//        return false;
//    }
//
//    @Override
//    public boolean onTouchEvent(MotionEvent event)		//这个方法如果 true 则整个Activity 的 onTouchEvent() 不会被系统回调
//    {
//        super.onTouchEvent(event);
//        return false;
//    }
//
  /*//todo
    public TextViewer scrollAndGetSelection(int position)
    {
    	int[] location = new int[2];
    	int y = this.getBottom();
    	
    	this.setSelection(position);
    	
    	 View view = getChildAt(position);
         if (view == null) {
             return null;
         }

         TextViewer content = (TextViewer) view.findViewById(R.id.content);
         return content;
    	//this.getAdapter().getView(position, convertView, parent)(0);
    	//this.scrollBy(0, y)
    	int[] location = new int[2];
    	this.getLocationOnScreen(location);
    	
    	 Layout layout = getLayout();
    	 int line = layout.getLineForOffset(startIndex);
    	 Rect bounds = new Rect();
    	 layout.getLineBounds(line, bounds);
    	 int y = location[1] +  bounds.top; 
         this.scrollTo(0, y);
    	//return 
    }*/

}