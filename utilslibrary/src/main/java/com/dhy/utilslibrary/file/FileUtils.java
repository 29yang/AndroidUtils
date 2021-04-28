package com.dhy.utilslibrary.file;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;

import com.dhy.utilslibrary.logger.LoggerUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
    private static String path = ""; //性能测试路径

    public static byte[] File2byte(File filePath) {
        byte[] buffer = null;
        try {
            FileInputStream fis = new FileInputStream(filePath);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 创建文件夹
     *
     * @param dirpath 路径
     * @return dir
     */
    public static boolean createDIR(String dirpath) {
        File dir = new File(dirpath);
        if (dir.exists()) {
            return true;
        }
        return dir.mkdirs();
    }

    public static boolean createDIR(String dirpath, boolean forceRecreate) {
        return createDIR(new File(dirpath), forceRecreate);
    }

    /**
     * 创建文件夹
     * 若文件存在,但非目录,则删除重建
     *
     * @param targetFile    要创建的目标目录文件
     * @param forceRecreate 若目录已存在,是否要强制重新闯进(删除后,新建)
     * @return 是否创建成功
     */
    public static boolean createDIR(File targetFile, boolean forceRecreate) {
        if (targetFile == null) {
            return false;
        }

        if (targetFile.exists()) {
            // 存在同名文件
            boolean isDir = targetFile.isDirectory();
            if (!isDir) { // 非目录,删除以便创建目录
                boolean result = targetFile.delete();
            } else if (forceRecreate) { // 强制删除目录
                deleteDir(targetFile);
            } else { // 目录存在
                return true;
            }
        }

        return targetFile.mkdirs();
    }

    /**
     * 删除指定目录
     * 若存在同名非目录文件,则不处理
     */
    public static boolean deleteDir(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return true;
        for (File file : dir.listFiles()) {
            if (file.isFile()) {// 删除所有文件
                file.delete();
            } else if (file.isDirectory()) { // 递归删除子目录
                deleteDir(file);
            }
        }
        return dir.delete();// 删除空目录本身
    }

    /**
     * 创建文件
     *
     * @param filepath 路径
     * @return file对象
     * @throws IOException
     */
    public static File createFile(String filepath) throws IOException {
        File file = new File(path + filepath);
        boolean b = file.createNewFile();
        return file;
    }

    /**
     * 写文件
     *
     * @param msg             写入的内容
     * @param filePathAndName 路径及文件地址
     * @param append          是否是追加模式
     */
    public static void writeToFile(String msg, String filePathAndName, boolean append) {
        File dir = new File(path);
        if (!dir.exists()) {
            boolean mkdirs = dir.mkdirs();
        }
        FileWriter fileWriter = null;
        BufferedWriter bufferedWriter = null;
        try {
            File file = new File(path + filePathAndName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileWriter = new FileWriter(file, append);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(msg + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedWriter != null) {
                    bufferedWriter.close();
                }
                if (fileWriter != null) {
                    fileWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 清除测试数据
     * 文件夹 sdcard/function/文件夹
     */
    public static void cleanCache() {
        File mapEngineLogDir = new File(path);
        if (mapEngineLogDir.listFiles() == null || mapEngineLogDir.listFiles().length == 0) {
            return;
        }
        for (File files : mapEngineLogDir.listFiles()) {
            files.delete();
        }
    }

    /**
     * 保存文件
     *
     * @param filePath 要保存的路径,如 /sdcard/amapauto20/jniScreenshot/xxx.jpg
     */
    public static void saveImage(Bitmap bitmap, String filePath) {
        if (bitmap == null || TextUtils.isEmpty(filePath)) {
            return;
        }
        try {
            File file = new File(filePath);
            File folder = file.getParentFile();
            if (!folder.exists()) {
                folder.mkdirs();
            }
            if (file.exists()) {
                file.delete();
            }
            FileOutputStream out = new FileOutputStream(file);
            Bitmap.CompressFormat format =
                    Bitmap.Config.ARGB_8888 == bitmap.getConfig() ? Bitmap.CompressFormat.PNG : Bitmap.CompressFormat.JPEG;
            bitmap.compress(format, 100, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteDir(String pPath) {
        deleteDirWihtFile(new File(pPath));
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory()) {
            return;
        }
        for (File file : dir.listFiles()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                deleteDirWihtFile(file);
            }
        }
        dir.delete();
    }

    public static String getFileData(String filePath) {
        System.out.println(System.currentTimeMillis());
        StringBuffer buffer = new StringBuffer();
        BufferedReader bf = null;
        try {
            bf = new BufferedReader(new FileReader(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        String s = null;
        while (true) {
            try {
                if (!((s = bf.readLine()) != null)) break;
            } catch (IOException e) {
                e.printStackTrace();
            }//使用readLine方法，一次读一行
            buffer.append(s.trim());
        }
        System.out.println(System.currentTimeMillis());
        return buffer.toString();

    }
}
