package cn.org.silencewing.reader.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;

import java.io.File;
import java.util.*;

import cn.org.silencewing.reader.FileBrowserActivity;

public class FileBrowser extends ListView implements
        android.widget.AdapterView.OnItemClickListener {

    private final String namespace = "http://reader.silencewing.org.cn/";
    private String rootDirectory;

    // 保存当前目录中所有的File对象（每一个File对象表示目录或文件）
    private List<File> fileList = new ArrayList<File>();

    // 用于分段保存当前目录。例如/sdcard/xyz/abc，将会分段sdcard、xyz、abc从前往后压入栈
    private Stack<String> dirStack = new Stack<String>();
    private FileListAdapter fileListAdapter;
    private OnFileBrowserListener onFileBrowserListener;

    // 保存<FileBrowser>标签的folderImage属性值。 表示显示在目录列表项前面的图像资源ID。
    private int folderImageResId;

    /*
      * 保存<FileBrowser>标签的otherFileImage属性值。
      * 表示未设置图像资源（通过文件扩展名设置）的文件列表项前面显示默认图像资源ID。
      */
    private int otherFileImageResId;

    // 表示所有通过扩展名设置的文件列表项前面显示的图像资源ID。 key表示文件扩展名，value表示该扩展名对应的图像资源ID。
    private Map<String, Integer> fileImageResIdMap = new HashMap<String, Integer>();

    // 保存<FileBrowser>标签的onlyFolder属性值。 true:FileBrowser组件将不会显示当前目录中的文件列表。
    private boolean onlyFolder = false;

    // 保存<FileBrowser>标签的onBackPressed属性值。true：相应返回键回上级目录。
    private boolean onBackPressed = false;

    // 保存<FileBrowser>标签的sort属性值。true：以上一级、文件夹、文件顺序排序。
    private int sort;
    private boolean sortDesc;

    public FileBrowser(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnItemClickListener(this);
        setBackgroundColor(android.graphics.Color.BLACK);

        folderImageResId = attrs.getAttributeResourceValue(namespace,
                "folderImage", 0);
        otherFileImageResId = attrs.getAttributeResourceValue(namespace,
                "otherFileImage", 0);
        onlyFolder = attrs.getAttributeBooleanValue(namespace, "onlyFolder",
                false);
        onBackPressed = attrs.getAttributeBooleanValue(namespace,
                "onBackPressed", false);
        //sort = attrs.getAttributeBooleanValue(namespace, "sort", false);

        // 动态属性
        int index = 1;
        while (true) {
            String extName = attrs.getAttributeValue(namespace, "extName"
                    + index);
            int fileImageResId = attrs.getAttributeResourceValue(namespace,
                    "fileImage" + index, 0);
            // 如果读取不到extName或fileImage属性时跳出循环
            if ("".equals(extName) || extName == null || fileImageResId == 0) {
                break;
            }
            fileImageResIdMap.put(extName, fileImageResId);
            index++;
        }

    }

    public void init(String root,int sort,boolean sortDesc)
    {
    	 this.rootDirectory = root;
    	 this.sort = sort;
    	 this.sortDesc = sortDesc;
         //rootDirectory = android.os.Environment.getRootDirectory().getPath();
         
         // 将SD卡的根目录压入栈
         dirStack.push(rootDirectory);
         // 生成当前目录中子目录及文件的名称列表（实际上是File对象）
         addFiles();

         fileListAdapter = new FileListAdapter(getContext());
         setAdapter(fileListAdapter);
    }
    //String currentPath = "/mnt/sdcard/books/";

    /**
     * 扫描当前目录，并将当前目录的File对象集合添加到fileList变量中
     * true:扫描目录有成功;false:扫描目录有错误;
     */
    private boolean addFiles() {

        // 获得当前路径
        String currentPath = getCurrentPath();
        // 获得当前目录中所有的File对象
        File[] files = new File(currentPath).listFiles();

        // .android_secure返回null
        if (null == files) {
            dirStack.pop();
            return false;
        }

        fileList.clear();

        // 当前不是根目录，使fileList变量的第1个元素为null，如果元素为null会显示一个“..”
        // 单击该列表项会返回到上一级目录
        if (dirStack.size() > 1)
            fileList.add(null);

        for (File file : files) {
            if (onlyFolder) {
                // 只添加目录
                if (file.isDirectory())
                    fileList.add(file);
            } else {
                fileList.add(file);
            }
        }

        // 排序
       // if (sort) {
        if(sort!=FileBrowserActivity.CODE_SORT_NULL)
        {
        	//final int i = sortDesc? -1:1;
            Collections.sort(fileList, new Comparator<File>() {

                @Override
                public int compare(File f1, File f2) {
                	try{
                    if (null == f1) {
                        return -1;
                    }
                    if (null == f2) {
                        return 1;
                    }
                    if (f1.isDirectory() && !f2.isDirectory()) {
                        return -1;
                    }
                    if (!f1.isDirectory() && f2.isDirectory()) {
                        return 1;
                    } else {
                    	switch(sort)
                    	{
                    	case FileBrowserActivity.CODE_SORT_FILE_NAME:
                    		if(!sortDesc)
                    			return f1.toString().compareToIgnoreCase(f2.toString());
                    		else
                        		return f2.toString().compareToIgnoreCase(f1.toString());
                            //return (f1.toString().compareToIgnoreCase(f2.toString())>0 && sortDesc)? -1:1 ;
                    	case FileBrowserActivity.CODE_SORT_MODIFY_TIME:
                    		return (f1.lastModified()>f2.lastModified() && sortDesc)?-1:1;
                    	//default:	
                    	
                    	}
                    	
                    }
                	}catch(Exception e)
                	{
                		Log.i("FileBrowser",e.getMessage());
                	}
                    return -1 ;
                }
            });
        }

        return true;
    }

    /**
     * 由dirStack变量获得当前的完整目录
     */
    private String getCurrentPath() {
        String path = "";
        for (String dir : dirStack) {
            path += dir + "/";
        }
        // 去除末尾“/”
        path = path.substring(0, path.length() - 1);
        return path;
    }

    /**
     * 获得文件后缀名
     */
    private String getExtName(String filename) {

        int position = filename.lastIndexOf(".");
        if (position >= 0)
            // 转成小写
            return filename.substring(position + 1).toLowerCase();
        else
            return "";
    }

    /**
     * 刷新列表项
     */
    public void refresh() {
        addFiles();
        fileListAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {

        // fileList元素值为null（ListView中列表项的值是“..”），返回上一级目录
        if (fileList.get(position) == null) {
            // 将最上层目录出栈
            dirStack.pop();
            // 重新获得当前目录中的子目录和文件的File对象
            addFiles();
            // 通知fileListAdapter对象数据已经变化，重新刷新列表
            fileListAdapter.notifyDataSetChanged();
            // 如果设置了FileBrowser事件则调用onDirItemClick方法，表示当前目录被单击
            if (onFileBrowserListener != null) {
                onFileBrowserListener.onDirItemClick(getCurrentPath());
            }
            // 单击的是目录列表项
        } else if (fileList.get(position).isDirectory()) {
            // 将当前单击的目录名压栈
            dirStack.push(fileList.get(position).getName());
            if (addFiles()) {
                fileListAdapter.notifyDataSetChanged();
                if (onFileBrowserListener != null) {
                    // 调用目录单击方法
                    onFileBrowserListener.onDirItemClick(getCurrentPath());
                }
            } else {
                Toast.makeText(getContext(), "系统不允许访问！", Toast.LENGTH_SHORT)
                        .show();
            }
            // 单击的是文件列表项
        } else {
            if (onFileBrowserListener != null) {
                // 获得当前单击的文件的完整目录名
                String filename = getCurrentPath() + "/"
                        + fileList.get(position).getName();
                // 调用文件单击事件方法
                onFileBrowserListener.onFileItemClick(filename);
            }
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        // 如果按下的是返回键、相应返回上级目录且不是SD卡根目录
        if (keyCode == KeyEvent.KEYCODE_BACK && onBackPressed
                && !getCurrentPath().equals(rootDirectory)) {
            // 将最上层目录出栈
            dirStack.pop();
            // 重新获得当前目录中的子目录和文件的File对象
            addFiles();
            // 通知fileListAdapter对象数据已经变化，重新刷新列表
            fileListAdapter.notifyDataSetChanged();
            // 如果设置了FileBrowser事件则调用onDirItemClick方法，表示当前目录被单击
            if (onFileBrowserListener != null) {
                onFileBrowserListener.onDirItemClick(getCurrentPath());
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * 自定义Adapter类
     */
    private class FileListAdapter extends BaseAdapter {
        private Context context;

        public FileListAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return fileList.size();
        }

        @Override
        public Object getItem(int position) {
            return fileList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LinearLayout fileLayout = new LinearLayout(context);
            fileLayout.setLayoutParams(new LayoutParams(
                    LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
            fileLayout.setOrientation(LinearLayout.HORIZONTAL);
            fileLayout.setPadding(5, 10, 0, 10);

            ImageView ivFile = new ImageView(context);
            TextView tvFile = new TextView(context);
            tvFile.setTextColor(android.graphics.Color.WHITE);
            tvFile.setTextAppearance(context,
                    android.R.style.TextAppearance_Large);
            tvFile.setPadding(5, 5, 0, 0);
            
            float size = tvFile.getTextSize();
            ivFile.setLayoutParams(new LayoutParams((int)(size*1.5), (int)(size*1.2)));

            if (fileList.get(position) == null) { // 上一级
                if (folderImageResId > 0)
                    ivFile.setImageResource(folderImageResId);
                tvFile.setText(". .");
            } else if (fileList.get(position).isDirectory()) { // 文件夹时
                if (folderImageResId > 0)
                    ivFile.setImageResource(folderImageResId);
                tvFile.setText(fileList.get(position).getName());
            } else { // 文件时
                tvFile.setText(fileList.get(position).getName());
                Integer resId = fileImageResIdMap.get(getExtName(fileList.get(
                        position).getName()));
                int fileImageResId = 0;
                if (resId != null) {
                    if (resId > 0) {
                        fileImageResId = resId;
                    }
                }
                if (fileImageResId > 0)
                    ivFile.setImageResource(fileImageResId);
                else if (otherFileImageResId > 0)
                    ivFile.setImageResource(otherFileImageResId);
            }

            tvFile.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
                    LayoutParams.WRAP_CONTENT));
            fileLayout.addView(ivFile);
            fileLayout.addView(tvFile);
            return fileLayout;
        }
    }

    public void setOnFileBrowserListener(OnFileBrowserListener listener) {
        this.onFileBrowserListener = listener;
    }

	public void setRoot(String root) {
		// TODO Auto-generated method stub
		this.rootDirectory = root;
		
	}

}