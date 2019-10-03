package cn.org.silencewing.reader.util;

/**
 * Created with IntelliJ IDEA.
 * User: Administrator
 * Date: 13-1-9
 * Time: 下午6:54
 * To change this template use File | Settings | File Templates.
 */
public class GlobalConfig {


    //static PropertyUtil config ;//= PropertyUtil.getInstance("");

    static GlobalConfig config;


    private GlobalConfig() {
    }

    public static GlobalConfig getInstance() {
        if (config == null) {
            config = new GlobalConfig();
            config.load();
        }
        return config;
    }

    public void load() {

        //PropertyUtil config = getConfig();
        try {
            PropertyUtil configProperty = PropertyUtil.getInstance(GlobalUtil.getConfigPath());


            theme = configProperty.get(themeKey, "");
            speechRate = Float.parseFloat(configProperty.get(speechRateKey, "1"));
            textFont = configProperty.get(textFontKey, "");
            textSize = Float.parseFloat(configProperty.get(textSizeKey, "12"));
            lineSpace = Integer.parseInt(configProperty.get(lineSpaceKey, "1"));
            itemPad = Integer.parseInt(configProperty.get(itemPadKey, "1"));
            textColor = Integer.parseInt(configProperty.get(textColorKey, "-9262421"));
            backgroundColor = Integer.parseInt(configProperty.get(backgroundColorKey, "-13281963"));


            seekStep = Integer.parseInt(configProperty.get(seekStepKey, "1"));
            root = configProperty.get(rootKey, "/");
            volumeToCtrl = Boolean.parseBoolean(configProperty.get(volumeToCtrlKey, "false"));

            exchangePreNext = Boolean.parseBoolean(configProperty.get(exchangePreNextKey, "false"));
            
        } catch (Exception e) {

            /*    theme = configProperty.get(themeKey,"");
          speechRate = Float.parseFloat(configProperty.get(speechRateKey,"1"));
          textFont = configProperty.get(textFontKey,"");
          textSize = Float.parseFloat(configProperty.get(textSizeKey,"10"));
          textColor = Integer.parseInt(configProperty.get(textColorKey,"-1"));
          backgroundColor = Integer.parseInt(configProperty.get(backgroundColorKey,"-16777216"));*/
        }

    }

    public void save() {
        PropertyUtil configProperty = PropertyUtil.getInstance(GlobalUtil.getConfigPath());
        configProperty.set(themeKey, "");
        configProperty.set(speechRateKey, String.valueOf(speechRate));
        configProperty.set(textFontKey, textFont);
        configProperty.set(textSizeKey, String.valueOf(textSize));
        configProperty.set(lineSpaceKey, String.valueOf(lineSpace));
        configProperty.set(itemPadKey, String.valueOf(itemPad));
        configProperty.set(seekStepKey, String.valueOf(seekStep));
        configProperty.set(textColorKey, String.valueOf(textColor));
        configProperty.set(backgroundColorKey, String.valueOf(backgroundColor));

        configProperty.set(rootKey, String.valueOf(root));
        configProperty.set(volumeToCtrlKey, String.valueOf(volumeToCtrl));
        configProperty.set(exchangePreNextKey, String.valueOf(exchangePreNext));

        configProperty.store();
    }


    public static final String themeKey = "theme";
    public static final String speechRateKey = "speechRate";
    public static final String textFontKey = "textFont";
    public static final String textSizeKey = "textSize";
    public static final String lineSpaceKey = "lineSpace";
    public static final String itemPadKey = "itemPad";
    public static final String seekStepKey = "seekStep";
    public static final String textColorKey = "textColor";
    public static final String backgroundColorKey = "backgroundColor";
    public static final String rootKey = "root";
    public static final String volumeToCtrlKey = "volumeToCtrl";
    public static final String exchangePreNextKey = "exchangePreNext";
    
    private float speechRate = 0;
    private String theme = "";
    private String textFont = "";
    private float textSize = 0;
    private int lineSpace = 0;
    private int itemPad = 0;
    private int seekStep = 1;
    private int textColor = 0;
    private int backgroundColor = 0;
    private String root = "";
    private Boolean volumeToCtrl = false;
    private Boolean exchangePreNext = false;

    public float getSpeechRate() {
        return speechRate;
    }

    public void setSpeechRate(float speechRate) {
        this.speechRate = speechRate;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getTextFont() {
        return textFont;
    }

    public void setTextFont(String textFont) {
        this.textFont = textFont;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }

    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

	public String getRoot() {
		return root;
	}

	public void setRoot(String root) {
		this.root = root;
	}

	public int getLineSpace() {
		return lineSpace;
	}

	public void setLineSpace(int lineSpace) {
		this.lineSpace = lineSpace;
	}

	public int getItemPad() {
		return itemPad;
	}

	public void setItemPad(int itemPad) {
		this.itemPad = itemPad;
	}

	public int getSeekStep() {
		return seekStep;
	}

	public void setSeekStep(int seekStep) {
		this.seekStep = seekStep;
	}

	public Boolean isVolumeToCtrl() {
		return volumeToCtrl;
	}

	public void setVolumeToCtrl(Boolean volumeToCtrl) {
		this.volumeToCtrl = volumeToCtrl;
	}


    public Boolean isExchangePreNext() {
        return exchangePreNext;
    }

    public void setExchangePreNext(Boolean exchangePreNext) {
        this.exchangePreNext = exchangePreNext;
    }
}
