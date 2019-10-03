package cn.org.silencewing.reader.util;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 * @details 各部分公用内容都较少，就丢一起了==。
 */
public final class TextUtil {


    String filePath = "";
    int currentIndex = 0;
    // int speekIndex = 0;
    int mainCount = 102400;
    int appendCount = 102400;

    FileInputStream fileInputStream;
    BufferedReader reader;
    //byte[] content = new byte[pageCount];
    private String encoding = "";

    //StringBuffer content = new StringBuffer();

    static GlobalUtil globalUtil = GlobalUtil.getInstance();


    public static TextUtil getInstance(String fileName) {
        TextUtil textUtil = new TextUtil(fileName);
        return textUtil;

    }


    private TextUtil(String filePath) {
        try {
            this.filePath = filePath;
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            fileInputStream = new FileInputStream(filePath);
            encoding = globalUtil.getIsEncoding(fileInputStream);

            reader = new BufferedReader(new InputStreamReader(fileInputStream, encoding));


        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
    }

    public String getFilePath() {
        return filePath;
    }


   // @SuppressLint("NewApi")
	public ArrayList<String> getList() {
        ArrayList<String> contents = new ArrayList<String>();

        try {
            String content = null;
            while (true) {
                content = reader.readLine();
                if (content == null)
                    break;
                if (globalUtil.isStringEmpty(content))
                    continue;
                
                contents.add(content);

            }
        } catch (Exception e) {
            globalUtil.log(e);
        } finally {
            try {
                if (reader != null)
                    reader.reset();
            } catch (Exception e) {

            }
        }
        return contents;
    }


    private String getContent() {
        // if(this.currentIndex == currentIndex)
        //     return content;

        if (fileInputStream == null)
            return "";

        String content;
        try {
            //return globalUtil.is2Str(fileInputStream);
            byte[] bufferBytes = new byte[mainCount];
            int readCount = fileInputStream.read(bufferBytes);
            content = new String(bufferBytes, encoding);

            this.currentIndex = currentIndex + readCount;
            return content;
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return "";

    }

    private String getLine() {
        if (fileInputStream == null)
            return "";
        try {

            return reader.readLine();
        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return "";
    }

    private String appendContent() {
        try {
            byte[] bufferBytes = new byte[appendCount];
            int readCount = fileInputStream.read(bufferBytes);
            this.currentIndex = currentIndex + readCount;
            return new String(bufferBytes, encoding);

        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return "";
    }


    public String getEncoding() {
        return encoding;
    }

    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
