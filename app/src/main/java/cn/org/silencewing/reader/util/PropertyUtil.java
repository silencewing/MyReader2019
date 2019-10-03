package cn.org.silencewing.reader.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @details 各部分公用内容都较少，就丢一起了==。
 */
public final class PropertyUtil {


    String filePath = "";
//    String tempPath = "";


    public static PropertyUtil getInstance(String fileName) {
        PropertyUtil util = new PropertyUtil(fileName);
        return util;

    }

    private Properties properties = null;


    private PropertyUtil(String filePath) {
        try {

            this.filePath = filePath;

            File file = new File(filePath);


            if (!file.exists())
            {
                new File(file.getParent()).mkdir();
                file.createNewFile();
            }

            FileInputStream fileInputStream = new FileInputStream(filePath);

            properties = new Properties();
            properties.load(fileInputStream);


        } catch (Exception e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.

        }
    }


    public String get(String key) {
        if (properties.containsKey(key) == false) {
            return "";
        }
        return String.valueOf(properties.get(key));
    }

    public String get(String key, String defaultValue) {
        if (properties.containsKey(key) == false) {
            return defaultValue;
        }
        return String.valueOf(properties.get(key));
    }


    public void set(String key, String value) {
        properties.setProperty(key, value);
        //return String.valueOf(properties.get(key));
    }

    public void store() {
        try {
            //SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_hhmmss");
            String tempPath = filePath + ".tmp";
            String backupPath = filePath + ".bak";
            properties.store(new FileOutputStream(tempPath), null);

            File tempFile = new File(tempPath);
            if(tempFile.exists() && tempFile.getUsableSpace()>0){
                File file = new File(filePath);
                if(file.exists()) {
                    File backup = new File(backupPath);
                    if(backup.exists())
                        backup.delete();
                    file.renameTo(backup);
//                    backup.deleteOnExit();

                }
                tempFile.renameTo(file);
            }



//            properties.store(new FileOutputStream(filePath), null);
        } catch (Exception e) {
            GlobalUtil.getInstance().log(e);
        }
    }

    public String getFilePath() {
        return filePath;
    }


    public class Config {

    }

}

