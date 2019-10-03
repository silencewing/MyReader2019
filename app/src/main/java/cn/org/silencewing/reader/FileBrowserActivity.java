package cn.org.silencewing.reader;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import cn.org.silencewing.reader.R;
import cn.org.silencewing.reader.widget.FileBrowser;
import cn.org.silencewing.reader.widget.OnFileBrowserListener;

public class FileBrowserActivity extends Activity implements
        OnFileBrowserListener {

    public static final int CODE_FILE_BROWSER = 101; // 该Activity的响应Code
    public static final int CODE_HISTORY_BROWSER = 111; // 该Activity的响应Code
    
    public static final String KEY_FILENAME = "filename"; // 文件名的key值

    private static final int DIALOG_CONFIRM_OPEN_FILE = 0; // 文件打开确认框

    private FileBrowser fileBrowser; // 文件浏览器
    private String filename; // 文件名称

    public final static String KEY_ROOT = "fileBrowerRoot";
    public final static String KEY_SORT = "sort";
    public final static String KEY_SORT_DESC = "sortDesc";

    public static final int CODE_SORT_MODIFY_TIME = 1;
    public static final int CODE_SORT_FILE_NAME = 0;
    public static final int CODE_SORT_NULL = -1;

    public String root = "";
    private int sort = 0;
    private boolean sortDesc = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filebrowser);

        Intent intent = getIntent();
        root = intent.getStringExtra(KEY_ROOT);
        sort = intent.getIntExtra(KEY_SORT,CODE_SORT_FILE_NAME);
        sortDesc = intent.getBooleanExtra(KEY_SORT_DESC,true);
        //this.setIntent(intent);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND,
                WindowManager.LayoutParams.FLAG_BLUR_BEHIND); // 背景模糊

        fileBrowser = (FileBrowser) findViewById(R.id.filebrowser);
        //Root = (root);
        fileBrowser.setOnFileBrowserListener(this);
        fileBrowser.init(root,sort,sortDesc);
        setTitle(root);
    }
    

    @Override
    public void onFileItemClick(String filename) {
        setTitle(filename);
        // 如果以txt结尾
        if (filename.toLowerCase().endsWith(".txt")||filename.toLowerCase().endsWith(".mrh")) {
            this.filename = filename;

            Intent data = new Intent();
            data.putExtra(KEY_FILENAME, filename);
            setResult(RESULT_OK, data);
            finish();

            //showDialog(DIALOG_CONFIRM_OPEN_FILE);
        }
        
      

    }

    @Override
    public void onDirItemClick(String path) {
        setTitle(path);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DIALOG_CONFIRM_OPEN_FILE: // 文件打开确认框
                return new AlertDialog.Builder(this)
                        .setTitle(R.string.confirm_title)
                        .setMessage(R.string.confirm_message)
                        .setPositiveButton(R.string.open,
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        Intent data = new Intent();
                                        data.putExtra(KEY_FILENAME, filename);
                                        setResult(RESULT_OK, data);
                                        finish();
                                    }
                                }).setNegativeButton(R.string.cancel, null)
                        .create();
        }
        return super.onCreateDialog(id);
    }

}
