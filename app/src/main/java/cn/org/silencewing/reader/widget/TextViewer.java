package cn.org.silencewing.reader.widget;
 
import android.content.Context;
import android.graphics.*;
import android.os.Handler;
import android.text.*;
import android.text.style.*;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import cn.org.silencewing.reader.MyTextToSpeech;
import cn.org.silencewing.reader.ReadActivity;
import cn.org.silencewing.reader.util.GlobalConfig;
import cn.org.silencewing.reader.util.GlobalUtil;
import cn.org.silencewing.reader.util.IndexHelper;
import cn.org.silencewing.reader.util.ListViewListener;
import cn.org.silencewing.reader.util.PlayHelper;

import java.util.Map;
import java.util.regex.Pattern;


public class TextViewer extends EditText {


	static GlobalConfig config = GlobalConfig.getInstance();
	int itemPad;
	float lineSpace;
	float diff;
	//float size;
    void init() {
        //this.setTextIsSelectable(true);
        //this.setSelectAllOnFocus(true);

  	// initTextView();
     
       itemPad = config.getItemPad();
       lineSpace = config.getLineSpace();
       //size = config.getTextSize();
       
       //itemPad = (int) (itemPad + diff);
        normalTextColor = config.getTextColor();
        normalBackgroundColor = config.getBackgroundColor();
        normalTextColorSpan = new ForegroundColorSpan(normalTextColor);
        normalBackgroundColorSpan = new BackgroundColorSpan(normalBackgroundColor);

        selectTextColorSpan = new ForegroundColorSpan(normalBackgroundColor);
        selectBackgroundColorSpan = new BackgroundColorSpan(normalTextColor);
        
        

    }

    public void initTextView() {

     	 this.setTextSize(config.getTextSize());
     	this.setNormalTextColor(config.getTextColor());
     	this.setNormalBackgroundColor(config.getBackgroundColor());
         this.setTextColor(config.getTextColor());
         this.setBackgroundColor(config.getBackgroundColor());
         
         this.setLineSpacing(lineSpace,1);
         //this.setGravity(Gravity);
         //after gravity!!!!
         //int diff = (int) (( (this.getLayoutParams().width - itemPad*4) % config.getTextSize() )/2);
         int diff = (int)(itemPad*1.2);
         this.setPadding(diff, itemPad/2, itemPad/2, itemPad/2);
        // MarginLayoutParams layoutParams=(MarginLayoutParams) getLayoutParams();

        // layoutParams.setMargins(itemPad, itemPad,itemPad, itemPad);
        /* this.setLeft(itemPad);
         this.setRight(itemPad);
         this.setTop(itemPad/2);
         this.setBottom(itemPad/2);*/
         //this.set
	}

	public ForegroundColorSpan selectTextColorSpan;
    public BackgroundColorSpan selectBackgroundColorSpan;
    public ForegroundColorSpan normalTextColorSpan;
    public BackgroundColorSpan normalBackgroundColorSpan;

    public TextViewer(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        init();
    }

    public TextViewer(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        init();
    }

    public TextViewer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        init();
    }


    @Override
    public float getTextSize() {
        return config.getTextSize();
    }


    private Rect rect;
    private Paint paint;

    final static GlobalUtil globalUtil = GlobalUtil.getInstance();




    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    private int normalTextColor = Color.WHITE;
    private int normalBackgroundColor = Color.BLACK;
    /* int textSize = 0;
String textFont = "";
String theme = "";   */

    // @Override
    int startIndex,endIndex;
	private int viewIndex;
	//IndexHelper indexHelper;
    public void changeSelected(boolean selected) {
        try {
            if (selected) {
            	//init();
            	IndexHelper indexHelper = PlayHelper.getIndexHelper(viewIndex);

                startIndex = indexHelper.getCurrentWordIndex();
                endIndex = indexHelper.getNextWordIndex();
                
               // this.activity.currentWordStart = startIndex;
               // this.activity.currentWordEnd = endIndex;
                //context
                //this.setSelection(startIndex,endIndex);
                //this.setSelection(0,0);
                Spannable content = this.getText();
                //this.setBackgroundColor(Color.RED);
                
                content.removeSpan(selectTextColorSpan);
                content.removeSpan(selectBackgroundColorSpan);

                content.setSpan(normalTextColorSpan, 0,content.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                content.setSpan(normalBackgroundColorSpan, 0,content.length()-1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                content.setSpan(selectTextColorSpan, startIndex,endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                content.setSpan(selectBackgroundColorSpan, startIndex,endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //content.setSpan(span, startIndex,endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                
                initTextView();
                this.setText(content);


                
            } else {
                //indexHelper = null;

                Spannable content = this.getText();

                content.removeSpan(selectTextColorSpan);
                content.removeSpan(selectBackgroundColorSpan);

                //content.setSpan(normalTextColorSpan, 0,content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                //content.setSpan(normalBackgroundColorSpan, 0,content.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

                //initTextView();
                this.setText(content);
                //setBackgroundColor(normalBackgroundColor);
                //setTextColor(normalTextColor);
            }
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
        }
    }

    public int getNormalTextColor() {
        return normalTextColor;
    }

    public void setNormalTextColor(int normalTextColor) {
        this.normalTextColor = normalTextColor;
    }

    public int getNormalBackgroundColor() {
        return normalBackgroundColor;
    }

    public void setNormalBackgroundColor(int normalBackgroundColor) {
        this.normalBackgroundColor = normalBackgroundColor;
    }


     /*
    @Override
    protected void onCreateContextMenu(ContextMenu menu) {
        //不做任何处理，为了阻止长按的时候弹出上下文菜单
    }

    @Override
    public boolean getDefaultEditable() {
        return false;

    }

      */
    
    
    public int getLocationY()
    {
    	int[] location = new int[2];
    	this.getLocationOnScreen(location);
    	 Layout layout = getLayout();
    	 if(layout == null) return 0;
    	 int line = layout.getLineForOffset(startIndex);
    	 Rect bounds = new Rect();
    	 layout.getLineBounds(line, bounds);
    	
    	 int y = location[1] +  bounds.top; 

    	 return y;
    }
    
    public int getLineHeight()
    {
    	
    	 Layout layout = getLayout();
    	 if(layout == null) return 0;
    	
    	 int lineCount = this.getLineCount();
    	 if( layout.getHeight()>0)
    		 return layout.getHeight() / lineCount;
    	 
    	 Rect bounds = new Rect();
    	 layout.getLineBounds(0, bounds);
    	 return bounds.bottom - bounds.top ;
/*
    	 int line = 0;
    	 Rect bounds = new Rect();
    	 layout.getLineBounds(line, bounds);
    	 if(activity.getLineHeight() == 0 && line > 1)
    	 {
    		 Rect boundsPre = new Rect();
    		 layout.getLineBounds(line-1,boundsPre);
    		 activity.setLineHeight ( bounds.top - boundsPre.top );
    		 
    	 }*/
    }
    int off = 0;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        Layout layout = getLayout();
        int line = 0;
        int x = (int)event.getX() + this.getScrollX();
        switch(action) {

        case MotionEvent.ACTION_DOWN:
        line = layout.getLineForVertical(getScrollY()+ (int)event.getY());
        off = layout.getOffsetForHorizontal(line, x);
        Selection.setSelection(getEditableText(), off);
//        this.activity.hideIM(this);
        break;
        case MotionEvent.ACTION_MOVE:
        case MotionEvent.ACTION_UP:
            line = layout.getLineForVertical(getScrollY()+(int)event.getY());
            int curOff = layout.getOffsetForHorizontal(line, x);
            Selection.setSelection(getEditableText(), off, curOff);

//            this.activity.hideIM(this);
        //PlayHelper.getIndexHelper().setCurrentIndexByCharIndex(content.getSelectionStart());

        //handler.post(results);
      
        break;

    }
        //return false;
        return super.onTouchEvent(event);
    }
    
	
/*	final Handler handler = new Handler();
	final Runnable results = new Runnable() {
	    public void run() {
	    	 // PlayHelper.moveToChar(viewIndex, off);
	    	  //PlayHelper.playCurrentWord();
	    }
	};
*/
    ReadActivity activity;
	public void setActivity(ReadActivity context) {
		// TODO Auto-generated method stub
		this.activity = context;
//		this.setTextIsSelectable(true);
//		this.setFocusable(true);
//		this.setFocusableInTouchMode(true);
		
	}


	public int getViewIndex() {
		return viewIndex;
	}

	public void setViewIndex(int viewIndex) {
		this.viewIndex = viewIndex;
	}

	public int getItemPad() {
		return itemPad;
	}

	public void setItemPad(int itemPad) {
		this.itemPad = itemPad;
	}

	



}