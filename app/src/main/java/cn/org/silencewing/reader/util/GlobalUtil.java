package cn.org.silencewing.reader.util;

import android.content.Context;
import android.graphics.Paint;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.org.silencewing.reader.R;

/**
 * @brief 全局公用类
 * @details 各部分公用内容都较少，就丢一起了==。
 */
public final class GlobalUtil {

    /**
     * Bob Lee懒加载：内部类GlobalUtilHolder
     */
    static class GlobalUtilHolder {
        static GlobalUtil instance = new GlobalUtil();
    }

    /**
     * Bob Lee懒加载：返回GlobalUtil的单例
     */
    public static GlobalUtil getInstance() {
        return GlobalUtilHolder.instance;
    }

    /**
     * 获取窗口默认显示信息
     */
    public Display getDisplay(Context mContext) {
        WindowManager wm = (WindowManager) mContext
                .getSystemService(Context.WINDOW_SERVICE);
        return wm.getDefaultDisplay();
    }

    public void ShowMessage(Context context,int messageId)
    {
    	Toast.makeText(context, messageId, Toast.LENGTH_SHORT).show();
		

    }
    public void ShowMessage(Context context,String message)
    {
    	Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
		

    }

    public static final String applicationPath = "myreader/";
    public static final String historyPath = "history/";
    public static final String pathSplit = "/";


    public static final String Root = android.os.Environment.getExternalStorageDirectory().getPath();
   /* public static String getRoot()
    {
    	if(root.trim() == "")
    		root = 
    	return root;
    }*/
    public static String getHistoryFilePath(String fileName) {
        return Root + pathSplit + applicationPath + historyPath + fileName;
    }

    public static String getConfigPath() {
        return Root + pathSplit + applicationPath + "config.ini";
    }

    /**
     * 动画方式
     */
    public enum AnimMode {
        UP_IN, UP_OUT, DOWN_IN, DOWN_OUT, LEFT_IN, LEFT_OUT, RIGHT_IN, RIGHT_OUT
    }

    ;

    public static final String line = "\r\n";

    /**
     * @brief 横移或竖移动画
     * @note 顶部或底部动画时，基础坐标值指X轴。左侧和右侧动画时，指Y轴。（坐标值为窗口绝对坐标值）
     */
    public void startTransAnim(View v, AnimMode animMode, long durationMillis) {
        int w = v.getWidth(), h = v.getHeight(); // 获取移动视图宽高
        float fromXDelta = 0, toXDelta = 0, fromYDelta = 0, toYDelta = 0;
        switch (animMode) {
            case UP_IN:
                fromYDelta = -h;
                break;
            case UP_OUT:
                toYDelta = -h;
                break;
            case DOWN_IN:
                fromYDelta = h;
                break;
            case DOWN_OUT:
                toYDelta = h;
                break;
            case LEFT_IN:
                fromXDelta = -w;
                break;
            case LEFT_OUT:
                toXDelta = -w;
                break;
            case RIGHT_IN:
                fromXDelta = w;
                break;
            case RIGHT_OUT:
                toXDelta = w;
                break;
        }
        TranslateAnimation transAnim = new TranslateAnimation(fromXDelta,
                toXDelta, fromYDelta, toYDelta); // 位移动画
        transAnim.setDuration(durationMillis); // 设置时间
        v.startAnimation(transAnim); // 开始动画
    }

    /**
     * @param is       输入流
     * @param encoding 编码方式
     * @return 字符串结果
     * @throws UnsupportedEncodingException 不支持的编码
     * @brief InputStream转为String
     */
    public String is2Str(InputStream is, String encoding)
            throws UnsupportedEncodingException {
        /*
           * 不直接从InputStream里读byte[]，再转成String，以避免截断汉字。
           * 如utf8一个汉字3字节，用byte[1024]会截断末尾而乱码。
           */
        InputStreamReader isReader = new InputStreamReader(is, encoding);
        /* 以char[]方式读取 */
        StringBuffer out = new StringBuffer();
        try {
            char[] b = new char[4096]; // 1024*4*2Byte
            for (int n; (n = isReader.read(b)) != -1; ) {
                out.append(b, 0, n);
            }
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * @param fis 文件输入流
     * @return 字符串结果
     * @throws UnsupportedEncodingException 不支持的编码
     * @brief 带BOM的文本的FileInputStream转为String，自动判断编码类型
     */
    public String is2Str(FileInputStream fis)
            throws UnsupportedEncodingException {
        // 转成BufferedInputStream
        BufferedInputStream bis = new BufferedInputStream(fis);
        // 简单判断文本编码
        String encoding = getIsEncoding(bis);
        // 转成BufferedReader
        BufferedReader reader = new BufferedReader(new InputStreamReader(bis,
                encoding));
        /* 以readLine()方式读取 */
        StringBuffer out = new StringBuffer();
        try {
            for (String s; (s = reader.readLine()) != null; ) {
                out.append(s);
                out.append("\n");
            }
            return out.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }


    public boolean isStringEmpty(String source) {
        return source == null || source.trim().equals("");//|| source.replaceAll(" ", "").trim().equals("");
    }

    public String bytes2Str(byte[] source, String encoding) {
        // 转成BufferedReader
        try {
            return new String(source, encoding).replaceAll("\r\n", ""); //.getString(source,encoding);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            return "";
        }
    }

    public String getSpeakString(String source) {
        return source.replace("\r", "").replace("\n", "");
    }




    /**
     * @param is InputStream
     * @return 编码类型
     * @brief 带BOM的文本判断，否则认为GB2312（即ANSI类型的TXT）
     * @details 复杂文件编码检测，请google cpdetector！
     * @warning markSupported为true时才进行判断，否则返回默认GB2312。
     * \n FileInputStream不支持mark/reset操作；BufferedInputStream支持此操作。
     */
    public String getIsEncoding(InputStream is) {
        String code = "GB2312";
        // log.e("is.markSupported()", "==" + is.markSupported() + "==");
        if (is.markSupported()) { // 支持mark()
            try {
                is.mark(5); // 打个TAG(5>3)
                byte[] head = new byte[3];
                is.read(head);
                if (head[0] == -1 && head[1] == -2)
                    code = "UTF-16";
                if (head[0] == -2 && head[1] == -1)
                    code = "Unicode";
                if (head[0] == -17 && head[1] == -69 && head[2] == -65)
                    code = "UTF-8";
                is.reset(); // 返回TAG
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return code;
    }


    // 本方法 分行又分页
    public static ArrayList<ArrayList<String>> getPageContentStringInfo(
            Paint m_paint, String content, int pageLines, float pageWidth) {

        char ch;
        int w = 0;
        int istart = 0;
        int lineNum = 0;
        ArrayList<ArrayList<String>> contentList = new ArrayList<ArrayList<String>>();
        ArrayList<String> cl = null;
        // 内容长度
        for (int i = 0; i < content.length(); i++) {
            if (cl == null)
                cl = new ArrayList<String>();
            ch = content.charAt(i);
            float[] widths = new float[1];
            String srt = String.valueOf(ch);
            m_paint.getTextWidths(srt, widths);
            if (ch == '\n') {
                // 如果遇到断行符
                lineNum++;
                cl.add(content.substring(istart, i));
                istart = i + 1;
                w = 0;
            } else {
                // 遇到字符
                w += (int) (Math.ceil(widths[0]));
                // 当长度小于宽度时
                if (w > pageWidth) {
                    lineNum++;
                    cl.add(content.substring(istart, i));
                    istart = i;
                    i--;
                    w = 0;
                } else {
                    if (i == (content.length() - 1)) {
                        lineNum++;
                        cl.add(content.substring(istart, content.length()));
                    }
                }
            }
            if (lineNum == pageLines || i == (content.length() - 1)) {
                contentList.add(cl);
                System.out.println(cl.toString());
                cl = null;
                // 当最后一个字符时的行数
                pageLines = lineNum;
                lineNum = 0;
            }
        }
        return contentList;
    }


    public void log(Exception e) {
        e.printStackTrace();
        // Toast.makeText(context,e.getMessage(),0);
    }
    
    
    public boolean changeShow(View view)
    {
    	if(view.getVisibility() == View.GONE)
    	{
    		view.setVisibility(View.VISIBLE);
    	}
    	else
    	{
    		view.setVisibility(View.GONE);
    	}
    	return view.getVisibility() == View.VISIBLE;
    }

}
