package cn.org.silencewing.reader.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-2-25
 * Time: 上午12:04
 * To change this template use File | Settings | File Templates.
 */
public class IndexHelper {

    private IndexHelper(){}

    private Map<Integer,Integer> indexes = null;
    private int currentIndex = 0;
    private int length = 0;

    //String currentWord = "";
   // private  static IndexHelper indexHelper;
    String source = "";
	public boolean isReady = false;
    
    public static IndexHelper newInstance(String source)
    {
      	IndexHelper indexHelper = new IndexHelper();
      	indexHelper.source = source;
        //indexHelper.init(source);
        return  indexHelper;
    }
    
  /*  public static IndexHelper getInstance(String source)
    {
        if(indexHelper==null)
            indexHelper = new IndexHelper();

        indexHelper.init(source);

        return  indexHelper;
    }*/
    public Integer getCurrentWordIndex()
    {
        if(indexes == null || currentIndex >= indexes.size())
            return -1;
        return indexes.get(currentIndex);
    }
    public Integer getNextWordIndex()
    {
        if(indexes == null || currentIndex + 1 >= indexes.size())
            return length ;
        return indexes.get(currentIndex+1);
    }

    final static String patternFormat = "(.+?((\\s)|[，。！？：；——……,.!?:;-]))";//"(\\w+?((\\s)|[，。！？：；——……,.!?:;-]))";
    static Pattern pattern = null;
    static Pattern getPattern()
    {
        if(null == pattern)
        pattern= Pattern.compile(patternFormat);
        return pattern;

    }
    public void init()
    {
    	if(isReady) return;
    	
    	isReady = true;
    	 
    	/*if(this.source.equals(source))
    		return;
    	
    	this.source = source;*/
        //source = source + "\\n";
    	source = source.replace(" ", "");
        currentIndex = 0;
        //isend = false;
        length = source.length();

        Matcher matcher = getPattern().matcher(source);

        if(indexes==null)
            indexes = new LinkedHashMap<Integer, Integer>();
        indexes.clear();
       

        /*indexes.put(0,0);
        if(source.trim() == "")
        {
            length = 0;
            return;
        }*/
        
        while(matcher.find()) {
            int i = 0;
            int index = 0;
            while(matcher.find(index)) {
                index = matcher.start();
               // if(index != 0)
                	indexes.put(i, index);
                i++;
                index = matcher.end();

            }
            //for(int i = 0; i <= matcher.groupCount(); i++)
            //    indexes.put(i, matcher.start());
        }
        
       if(indexes.isEmpty())
        {
        	indexes.put(0,0);
        	//length = 0;
        }
    }
    
    public int size()
    {
    	if(indexes == null)
    		return 0;
    	return indexes.size();
    }

    public Map<Integer, Integer> getIndexes() {
        return indexes;
    }

    public void setIndexes(Map<Integer, Integer> indexes) {
        this.indexes = indexes;
    }

    public int getCurrentIndex() {
        return currentIndex;
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
    }

   /* public boolean isEnd() {
        return isend || this.currentIndex >= indexes.size()-1;
    }

    public boolean isStart() {
        return isstart || this.currentIndex <= 0;
    }   */

    public int getWordIndexByCharIndex(int charIndex) {
        //if(charIndex == 0) return;
        int wordIndex = 0;
        for(Integer key : this.indexes.keySet())
        {
            if(indexes.get(key) <= charIndex)
            {
                wordIndex = key;
            }
            else
                break;
        }
        return wordIndex;
    }
  
    /*public void setCurrentIndexByCharIndex(int charIndex) {
        //if(charIndex == 0) return;
        int wordIndex = 0;
        for(Integer key : this.indexes.keySet())
        {
            if(indexes.get(key) <= charIndex)
            {
                wordIndex = key;
            }
            else
                break;
        }
        currentIndex =  wordIndex;
    }*/
}
