package com.dhy.utilslibrary.file;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;

public class IOUtils {
    public static byte[] decodeAssetResData(Context context, String resName) {
        AssetManager assetManager = context.getAssets();
        InputStream is;
        try {
            is = assetManager.open(resName);
            java.io.ByteArrayOutputStream bout = new java.io.ByteArrayOutputStream();

            byte[] bufferByte = new byte[1024];
            int l;
            while ((l = is.read(bufferByte)) > -1) {
                bout.write(bufferByte, 0, l);
            }
            byte[] rBytes = bout.toByteArray();
            bout.close();
            is.close();
            return rBytes;

        } catch (IOException e) {
            return null;
        } catch (OutOfMemoryError e) {
            return null;
        }
    }

    public static String getFileMD5(String fileName) {
        File file = new File(fileName);
        return getFileMD5(file);
    }

    public static String getFileMD5(File file) {
        FileInputStream fileStream = null;
        try {
            fileStream = new FileInputStream(file);
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fileStream.read(buffer)) != -1) {
                md5.update(buffer, 0, length);
            }
            fileStream.close();
            byte[] md5Bytes = md5.digest();
            StringBuilder hexValue = new StringBuilder();
            for (byte md5Byte : md5Bytes) {
                int val = (md5Byte) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            return null;
        } finally {

        }
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (final Exception ignored) {
                ignored.printStackTrace();
            }
        }
    }

    public static String getByteArrayMD5(byte[] byteArray) {
        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(byteArray);
            byte[] md5Bytes = md5.digest();
            StringBuilder hexValue = new StringBuilder();
            for (byte md5Byte : md5Bytes) {
                int val = (md5Byte) & 0xff;
                if (val < 16) {
                    hexValue.append("0");
                }
                hexValue.append(Integer.toHexString(val));
            }
            return hexValue.toString();
        } catch (Exception e) {
            return null;
        }
    }
}
