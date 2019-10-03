package cn.org.silencewing.reader.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import cn.org.silencewing.reader.R;
import cn.org.silencewing.reader.ReadActivity;
import cn.org.silencewing.reader.widget.TextViewer;

import java.util.ArrayList;


public class TextListAdapter extends BaseAdapter {


    //private ArrayList<String> data;
	//private static IndexHelper
    private LayoutInflater mInflater;

    private ReadActivity context;

    private static GlobalUtil globalUtil = GlobalUtil.getInstance();

    public TextListAdapter(ReadActivity context) {
        this.context = context;
        //this.textUtil = textUtil;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

  
    /* public void addItem(final String item) {
       data.add(item);
       notifyDataSetChanged();
   } */

    @Override
    public int getCount() {
        return PlayHelper.size();
    }

    @Override
    public String getItem(int position) {
        return PlayHelper.getData(position);
    }

    @Override
    public long getItemId(int position) {
        /* if(position == count -1)
    {
        notifyDataSetChanged();
        return 0;

    }    */
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //System.out.println("getView " + position + " " + convertView);
        try {
            TextViewer holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.itemviewer, null);
                holder = (TextViewer) convertView.findViewById(R.id.content);

               /* int itemPad = holder.getItemPad();
                convertView.setPadding(itemPad, itemPad/2, itemPad, itemPad/2);
               */ 
                //holder.setTextIsSelectable(true);

                GlobalConfig config = GlobalConfig.getInstance();
                holder.setViewIndex(position);
                //holder.setNormalTextColor(config.getTextColor());
                //holder.setNormalBackgroundColor(config.getBackgroundColor());
                //holder.setTextColor(config.getTextColor());
                //holder.setBackgroundColor(config.getBackgroundColor());
                // holder.setTextSize(config.getTextSize());
                	
                holder.initTextView();
                holder.setActivity(this.context);
                holder.setOnClickListener(this.context.getListViewListner());
                holder.setOnLongClickListener(this.context.getListViewListner());
                convertView.setTag(holder);
            } else {
                holder = (TextViewer) convertView.getTag();
                // holder.setTextSize(GlobalConfig.getInstance().getTextSize());
            }
            holder.setText(getItem(position));
            
            if(position == PlayHelper.getCurrentPosition())
            {

            	TextViewer content = holder;
            	context.refreshTextViewer(content);
            }
         
             /*
            if(position == context.getCurrentItemPosition())
            {

            	TextViewer content = holder;


                if (context.getCurrentViewer() != null && context.getCurrentViewer() != content)
                {
                	context.getCurrentViewer().changeSelected(false);
                   // content.changeSelected(true);
                }
               // if(currentViewer == null)

                context.setCurrentViewer(content);

                if(manual)
                    content.getIndexHelper().setCurrentIndexByCharIndex(content.getSelectionStart());
                content.changeSelected(true);

                context.isPlay() = true;
                //isItemEnd =
                        content.play(this.tts, tts.getBatchCount());
            }*/
            
        } catch (Exception e) {
            //e.getMessage();
            globalUtil.log(e);
        }
        return convertView;
    }

}


