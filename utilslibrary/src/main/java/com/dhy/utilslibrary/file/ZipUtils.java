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


//    // 解压文件
//    public static boolean upZipFile(File zipFile, String folderPath, ArrayList<String> list) throws IOException {
//        boolean bSuccess = true;
//        File desDir = new File(folderPath);
//        if (!desDir.exists()) {
//            desDir.mkdirs();
//        }
//        ZipFile zf = new ZipFile(zipFile);
//
//        for (Enumeration<?> entries = zf.entries(); entries.hasMoreElements(); ) {
//            ZipEntry entry = ((ZipEntry) entries.nextElement());
//            if (entry.isDirectory()) {
//                continue;
//            }
//            InputStream in = zf.getInputStream(entry);
//            in = CrcUtil.doCheck32sum(in);
//
//            long zipCrc = entry.getCrc();
//            // Log.d("PARA", "fileCrc:"+strCrc+"  zipCrc:"+zipCrc);
//
//            String str = folderPath + File.separator + entry.getName();
//            if (entry.getName().endsWith("packet.dat")) {// 对packet.dat特殊处理,剔除cn文件夹
//                str = str.replace("/cn", "");
//
//            }
//            // MapbarLog.i("-----文件路径为-----"+str);
//            str = new String(str.getBytes("8859_1"), "GB2312");
//
//            if (list == null) {
//
//                File desFile = new File(str);
//                // Log.d("ZIP", "file:"+desFile.getName());
//                if (desFile.exists()) {
//                    int len = in.available();
//                    long fileLen = desFile.length();
//                    // Log.d("ZIP", "Zip  len:"+len);
//                    // Log.d("ZIP", "File len:"+fileLen);
//                    if (len == fileLen) {
//                        try {
//                            CheckedInputStream cis = new CheckedInputStream(new FileInputStream(desFile), new CRC32());
//                            byte[] buf = new byte[ZipUtil.BUFF_SIZE];
//                            while (cis.read(buf) >= 0) {
//                            }
//
//                            long resCrc = cis.getChecksum().getValue();
//                            if (zipCrc == resCrc) {
//                                // Log.d("ZIP", "zipCrc==resCrc");
//                                continue;
//                            }
//                            // Log.d("ZIP", "zipCrc!=resCrc"+zipCrc+";"+resCrc);
//                            cis.close();
//                        } catch (IOException e) {
//                        }
//                    } else {
//                        try {
//                            desFile.delete();
//                        } catch (Exception ex) {
//                            // Log.d("ZIP", "删除失败");
//                            continue;
//                        }
//                    }
//                }
//
//                File tempFile = new File(str + ".tmp");
//
//                if (tempFile.exists()) {
//                    try {
//                        tempFile.delete();
//                    } catch (Exception ex) {
//                    }
//                }
//
//                if (!tempFile.exists()) {
//                    File fileParentDir = tempFile.getParentFile();
//                    if (!fileParentDir.exists()) {
//                        fileParentDir.mkdirs();
//                    }
//                    tempFile.createNewFile();
//                }
//                OutputStream out = new FileOutputStream(tempFile);
//                byte buffer[] = new byte[ZipUtil.BUFF_SIZE];
//                int realLength;
//                while ((realLength = in.read(buffer)) > 0) {
//                    out.write(buffer, 0, realLength);
//                }
//                long strCrc = ((CheckedInputStream) in).getChecksum().getValue();
//                in.close();
//                zf.close();
//                out.close();
//
//                // crc32校验失败
//                if (strCrc != zipCrc) {
//                    // 删除
//                    tempFile.delete();
//                    bSuccess = false;
//                } else {
//                    // 重命名
//                    tempFile.renameTo(desFile);
//                }
//            }
//        }
//        return bSuccess;
//    }

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
