package cn.org.silencewing.reader.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.org.silencewing.reader.MyTextToSpeech;
import cn.org.silencewing.reader.ReadActivity;


public class PlayHelper {
	
	  private static Map<Integer,IndexHelper>indexHelpers;// = new HashMap<Integer,IndexHelper>();
	  private static int currentPosition=0;
	  private static int currentIndex=0;
	  static MyTextToSpeech tts;
	  
	  static int batchCount;
	  static boolean isPlay;
	  static ReadActivity context;
	  
	  public static boolean isPlay()
	  {
		  return isPlay;
	  }
	  public static void setPlay(boolean isPlayValue)
	  {
		  isPlay = isPlayValue;
	  }
	  
	  public static void setTts(MyTextToSpeech textToSpeech)
	  {
		 tts = textToSpeech; 
	  }
	  public static void initData(ArrayList<String> list,int position,int index)
	  {
		  //tts = textToSpeech;
		  if(indexHelpers != null)
			  indexHelpers.clear();
		  else
			  indexHelpers = new HashMap<Integer, IndexHelper>();
		  
		  int i = 0;
		  for(String source :list)
		  {
			  indexHelpers.put(i, IndexHelper.newInstance(source));
			  i++;
		  }
		  moveTo(position, index);
	  }
	  
		public static void finalWork() {
			//if (tts != null && isPlay) {
				stop();//tts.stop(); // 停止当前发声
				//if(isEnd)
				tts.shutDown();
	           
	        //}
			// tts = null;
		    //  	context = null;
		    //  	indexHelpers.clear();
		      	  
	            //?tts = null;
	            //isPlay = false;
	        //    currentPosition=0;
	      	//   currentIndex=0;
	      	//   batchCount=0;
	      	//   isPlay = false;
		}
		
	  public static IndexHelper getIndexHelper(int position)
	  {
		  if(!indexHelpers.containsKey(position))
			  return null;
		  
		  IndexHelper indexHelper = indexHelpers.get(position);
		 // if(!indexHelper.isReady)
		  indexHelper.init();
		  
		  return indexHelper;
	  }
	  
	  public static int size()
	  {
		  return indexHelpers.size();
	  }
	  
	  public static String getData(int index)
	  {
		  return getIndexHelper(index).source;
	  }

	
	  
	  public static void movePlay(int position,int charIndex)
	  {
		  moveToChar(position,charIndex);
		  movePlay(0);
	  }
	  public static void movePlay(int step)
	  {
		  if(tts==null)return;
		  if(indexHelpers.size()<=0)return;
		  //if(!isPlay) return;
		  if(step>0)
		  {
			  moveNext(step);
			  playCurrentWord();
			  return;
		  }
		  if(step<0)
		  {
			  movePre(-step);
			  playCurrentWord();
			  return;
		  }
		  if(step==0)
		  {
			 playCurrentWord();
			 return;
		  }
	  }
	  
	  private static void playCurrentWord()
	  {
		  IndexHelper indexHelper = getIndexHelper(currentPosition);
		  if(indexHelper == null)
			  return;
		  
		  //isPlay = false;
	      
		  //isPlay = true;
		  if(tts!=null && isPlay)
		  {
			  String content = indexHelper.source.substring(indexHelper.getCurrentWordIndex(), indexHelper.getNextWordIndex());				 
			  tts.batchAdd();
			  tts.play(tts.getBatchCount(), content, false);
		  }
	  }

	  public static boolean changePlay() { 
			if (isPlay)
		            stop();
		   else
		            play();
			return isPlay;
			
		}
	    public static void play() {
	        isPlay = true;
	        if (tts == null)
	            return;

	        movePlay(0);
	    }

	    public static void stop() {
	        isPlay = false;

	        if (tts != null)
	            tts.stop();
	    }
	    
	    public static final int NULL_POSITION = -1;
	    public static int search(String key,int startPosition,int endPosition)
	    {
	    	if(startPosition>=endPosition) return NULL_POSITION;
	    	for(int i = startPosition ; i<indexHelpers.size();i++)
	    	{
	    		IndexHelper indexHelper = getIndexHelper(i);
	    		if(indexHelper!=null)
	    		{
	    			if(-1 != indexHelper.source.indexOf(key))
	    			{
	    				return i;
	    			}
	    		}
	    	}
	    	return NULL_POSITION;
	    }

	    private static boolean moveTo(int position,int index)
		  {
	    	currentPosition = position;
			 
			 IndexHelper indexHelper = getIndexHelper(position);
			 if(indexHelper!=null)
			 {
				 currentIndex = index;
					
				 indexHelper.setCurrentIndex(currentIndex);
				 return true;
			 }
			 return true;
		  }
	  private static boolean moveToChar(int position,int charIndex)
	  {
		 currentPosition = position;
		 
		 IndexHelper indexHelper = getIndexHelper(position);
		 if(indexHelper!=null)
		 {
			 currentIndex = indexHelper.getWordIndexByCharIndex(charIndex);
				
			 indexHelper.setCurrentIndex(currentIndex);
			 return true;
		 }
		 return true;
	  }
	  
	  
	    private static void moveNext(int step) {
	    	IndexHelper indexHelper = getIndexHelper(currentPosition);
	    	
	    	
	    	int diff = indexHelper.size() - 1  - currentIndex ;
	    	int leftStep = step - diff;
	    	if(leftStep>0)
	    	{
	    		//下一段
	    		if(currentPosition >= indexHelpers.size()-1)
	    		{
	    			currentIndex = indexHelper.size()-1;
	   			 	indexHelper.setCurrentIndex(currentIndex);
	    			return;
	    		}
	    		//移下并减Step
	    		currentPosition ++;
	    		currentIndex = 0;
	    		leftStep --;
	    		
	    		moveNext(leftStep);
	    	}
	    	else
	    	{
	    		currentIndex = currentIndex + step;
   			 	indexHelper.setCurrentIndex(currentIndex);
	    	}
	    		    	
	    }

	    private static void movePre(int step) {

	    	int diff = currentIndex  ;
	    	int leftStep = step - diff;
	    	if(leftStep>0)
	    	{
	    		//上一段
	    		if(currentPosition <= 0)
	    		{
	    			
	    			currentIndex = 0;
	    			IndexHelper indexHelper = getIndexHelper(currentPosition);
	   			 	indexHelper.setCurrentIndex(currentIndex);
	    			return;
	    		}

	    		currentPosition --;
	    		IndexHelper indexHelper = getIndexHelper(currentPosition);
	    		currentIndex = indexHelper.size() - 1;
	    		leftStep --;
	    		movePre(leftStep);
	    	}
	    	else
	    	{
	    		currentIndex = currentIndex - step;
	    		IndexHelper indexHelper = getIndexHelper(currentPosition);
   			 	indexHelper.setCurrentIndex(currentIndex);
	    	}
	    		
	    }

	    
	    private void batchAdd() {
	        tts.batchAdd();
	    }

	    
	

		public static void reInitTts() {
			if(tts !=null)
			tts.initTts();
		}

		public static int getCurrentPosition() {
			return currentPosition;
		}

		public static void setCurrentPosition(int currentPosition) {
			PlayHelper.currentPosition = currentPosition;
		}

		public static int getCurrentIndex() {
			return currentIndex;
		}

		public static void setCurrentIndex(int currentIndex) {
			PlayHelper.currentIndex = currentIndex;
		}

		


}
