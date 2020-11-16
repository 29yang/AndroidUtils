package com.dhy.utilslibrary.file;

import android.content.Context;
import android.os.Environment;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DataFileManager extends BaseManager {
    private static final String TAG = "DataFileManager";

    private String contentsName = "data.zip";
    private String contentsPath = Environment.getExternalStorageDirectory().toString() + "/smartnavi/data.zip";
    private String contentsFilePath = Environment.getExternalStorageDirectory().toString() + "/smartnavi/";
    private String SMNaviVersionConfigPath = Environment.getExternalStorageDirectory().toString() + "/smartnavi/";
    private String SMNaviVersion = "dataVersion.txt";
    public static String NaviConfPath = Environment.getExternalStorageDirectory().getPath() + "/smartnavi/data/config.ini";

    public DataFileManager(Context context) {
        _context = context;
    }

    public void copyResource(OnCopyRunnaleCallBack onCopyRunnaleCallBack) {
        boolean isSucess = true;
        boolean hasCopyNaviContents = true;
        if (ZipUtils.fileIsExists(SMNaviVersionConfigPath + SMNaviVersion)) {
            File sdcardVersion = new File(SMNaviVersionConfigPath + SMNaviVersion);
            String sdversion = "";
            String assetsVersion = "";
            try {
                sdversion = getFileContent(sdcardVersion);
                assetsVersion = readFile2String(_context.getAssets().open(SMNaviVersion));
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (sdversion.equals(assetsVersion)) {
                hasCopyNaviContents = false;
            }
        }

        if (hasCopyNaviContents) {
            //复制data
            copyFile(contentsName, contentsFilePath);
            File zipfile = new File(contentsPath);
            try {
                ZipUtils.upZipFile(zipfile, contentsFilePath);
            } catch (IOException e) {
                isSucess = false;
                e.printStackTrace();
            }
            if (ZipUtils.fileIsExists(contentsPath)) {
                zipfile.delete();
            }
            //复制版本信息
            copyFile(SMNaviVersion, SMNaviVersionConfigPath);
        }

        if (isSucess) {
            onCopyRunnaleCallBack.copyFinish();
        } else {
            onCopyRunnaleCallBack.copyError();
        }
    }

    private boolean copyFile(String fileFromName, String toDir) {
        try {
            File file = new File(toDir);
            if (!file.exists()) {
                file.mkdirs();
            }

            File book_file = new File(toDir + fileFromName);

            InputStream its = _context.getAssets().open(fileFromName);
            int fileLength = its.available();

            if (!book_file.exists()) {
                book_file.createNewFile();
            }

            FileOutputStream fots = new FileOutputStream(book_file, false);
            byte[] buffer = new byte[fileLength];
            // 已经成功读取的字节的个数
            int readCount = 0;
            while (readCount < fileLength) {
                readCount += its.read(buffer, readCount, fileLength - readCount);
            }
            fots.write(buffer, 0, fileLength);

            its.close();
            fots.close();
            return true;
        } catch (IOException e1) {
            e1.printStackTrace();
            return false;
        }
    }

    private String getFileContent(File file) {
        String content = "";
        //检查此路径名的文件是否是一个目录(文件夹)
        if (!file.isDirectory()) {
            //文件格式为""文件
            if (file.getName().endsWith("txt")) {
                try {
                    InputStream instream = new FileInputStream(file);
                    content = readFile2String(instream);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }

        return content;
    }

    public static String readFile2String(InputStream instream) {
        String content = "";
        try {
            if (instream != null) {
                InputStreamReader inputreader = new InputStreamReader(instream, "UTF-8");
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line = "";
                //分行读取
                while ((line = buffreader.readLine()) != null) {
                    content += line + "\n";
                }
                //关闭输入流
                instream.close();
            }
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

        return content;
    }

    public interface OnCopyRunnaleCallBack {
        void copyFinish();

        void copyError();
    }

}
