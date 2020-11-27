package com.dhy.utilslibrary.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipUtils {
    private static final String TAG = "ZipUtils";

    /**
     * 缓存: 1M Byte
     */
    private static final int BUFF_SIZE = 1024 * 1024;

    /**
     * 解压缩一个文件
     *
     * @param zipFile    压缩文件
     * @param folderPath 解压缩的目标目录
     * @return
     * @throws IOException 当解压缩过程出错时抛出
     */

    public static ArrayList<File> upZipFile(File zipFile, String folderPath) throws IOException {
        ArrayList<File> fileList = new ArrayList<File>();
        File desDir = new File(folderPath);

        if (!desDir.exists()) {
            desDir.mkdirs();
        }

        ZipFile zf = new ZipFile(zipFile);
        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            if (entry.isDirectory()) {
                continue;
            }

            InputStream is = zf.getInputStream(entry);
            String str = folderPath + File.separator + entry.getName();
            str = new String(str.getBytes("8859_1"), "UTF-8");
            File desFile = new File(str);
            if (!desFile.exists()) {
                File fileParentDir = desFile.getParentFile();

                if (!fileParentDir.exists()) {
                    fileParentDir.mkdirs();
                }

                desFile.createNewFile();
            }

            OutputStream os = new FileOutputStream(desFile);
            byte buffer[] = new byte[BUFF_SIZE];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }

            os.flush();
            os.close();
            is.close();
            fileList.add(desFile);
        }

        return fileList;
    }

    public static boolean fileIsExists(String strFile) {
        try {
            File f = new File(strFile);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
}
