package com.dhy.utilslibrary.logger;

import android.content.Context;
import android.os.Environment;
import android.os.Process;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Keep;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Keep
public final class LoggerUtil {
    private static String sTag = "LoggerUtil";

    public static final int LEVEL_VERBOSE = 0;
    public static final int LEVEL_DEBUG = 1;
    public static final int LEVEL_INFO = 2;
    public static final int LEVEL_WARN = 3;
    public static final int LEVEL_ERROR = 4;
    public static final int LEVEL_NONE = 10;
    public static final int JSON_INDENT = 2;
    public static final int MIN_STACK_OFFSET = 3;

    public static int logLevel = LEVEL_DEBUG;
    private static final String CHARSETNAME_UTF8 = "utf-8";
    private static Context applicationContext;
    private static String logpath = "LoggerUtil";
    private static boolean isWriteFile = false;
    private static FileLogger fileLogger = new FileLogger();
    private static SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private LoggerUtil() {
    }

    private static LoggerUtil instance = new LoggerUtil();

    public static LoggerUtil getInstance() {
        return instance;
    }

    public static void init(Context context, String logDir, int level, String tag, boolean isFile) {
        applicationContext = context.getApplicationContext();
        if (logDir != null) {
            logpath = logDir;
        }
        logLevel = level;
        if (tag != null) {
            sTag = tag;
        }
        isWriteFile = isFile;
    }

    public static void unInit() {
        LoggerThread.getInstance().flushToFile();
    }

    public static void v(String msg) {
        v(sTag, msg);
    }

    public static void d(String msg) {
        d(sTag, msg);
    }

    public static void i(String msg) {
        i(sTag, msg);
    }

    public static void w(String msg) {
        w(sTag, msg);
    }

    public static void e(String msg) {
        e(sTag, msg);
    }

    // verbose info only print to logcat, don't need write to file
    public static void v(String tag, String msg) {
        if (LEVEL_VERBOSE >= logLevel && !TextUtils.isEmpty(msg)) {
            String s = getMethodNames();
            Log.v(tag, String.format(s, msg));
        }
    }

    //from DEBUG LEVEL write to file
    public static void d(String tag, String msg) {
        if (LEVEL_DEBUG >= logLevel && !TextUtils.isEmpty(msg)) {
            String s = getMethodNames();
            Log.d(tag, String.format(s, msg));
            if (isWriteFile) {
                LoggerThread.getInstance().write(tag, String.format(s, msg));
            }
        }
    }

    public static void i(String tag, String msg) {
        if (LEVEL_INFO >= logLevel && !TextUtils.isEmpty(msg)) {
            String s = getMethodNames();
            Log.i(tag, String.format(s, msg));
            if (isWriteFile) {
                LoggerThread.getInstance().write(tag, String.format(s, msg));
            }
        }
    }

    public static void w(String tag, String msg) {
        if (LEVEL_WARN >= logLevel && !TextUtils.isEmpty(msg)) {
            String s = getMethodNames();
            Log.w(tag, String.format(s, msg));
            if (isWriteFile) {
                LoggerThread.getInstance().write(tag, String.format(s, msg));
            }
        }
    }

    public static void e(String tag, String msg) {
        if (LEVEL_ERROR >= logLevel && !TextUtils.isEmpty(msg)) {
            String s = getMethodNames();
            Log.e(tag, String.format(s, msg));
            if (isWriteFile) {
                LoggerThread.getInstance().write(tag, String.format(s, msg));
            }
        }
    }

    /**
     * 格式化json字符串
     *
     * @param jsonStr 需要格式化的json串
     * @return 格式化后的json串
     */
    public static String formatJson(String jsonStr) {
        if (null == jsonStr || "".equals(jsonStr)) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        char last = '\0';
        char current = '\0';
        int indent = 0;
        for (int i = 0; i < jsonStr.length(); i++) {
            last = current;
            current = jsonStr.charAt(i);
            //遇到{ [换行，且下一行缩进
            switch (current) {
                case '{':
                case '[':
                    sb.append(current);
                    sb.append('\n');
                    indent++;
                    addIndentBlank(sb, indent);
                    break;
                //遇到} ]换行，当前行缩进
                case '}':
                case ']':
                    sb.append('\n');
                    indent--;
                    addIndentBlank(sb, indent);
                    sb.append(current);
                    break;
                //遇到,换行
                case ',':
                    sb.append(current);
                    if (last != '\\') {
                        sb.append('\n');
                        addIndentBlank(sb, indent);
                    }
                    break;
                default:
                    sb.append(current);
            }
        }
        return sb.toString();
    }

    /**
     * 添加space
     *
     * @param sb
     * @param indent
     */
    private static void addIndentBlank(StringBuilder sb, int indent) {
        for (int i = 0; i < indent; i++) {
            sb.append('\t');
        }
    }

    /**
     * 获取程序执行的线程名,类名和方法名,以及行号等信息
     */
    private static String getMethodNames() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        int stackOffset = getStackOffset(stackTrace);
        stackOffset++;
        StringBuilder sb = new StringBuilder(30);
        sb.append(stackTrace[stackOffset].getMethodName())
                .append("(").append(stackTrace[stackOffset].getFileName())
                .append(":").append(stackTrace[stackOffset].getLineNumber())
                .append(") ").append("%s");
        return sb.toString();
    }

    private static int getStackOffset(StackTraceElement... trace) {
        int i = MIN_STACK_OFFSET;
        while (i < trace.length) {
            String name = trace[i].getClassName();
            if (!LoggerUtil.class.getName().equalsIgnoreCase(name)) {
                return --i;
            }
            i++;
        }
        return -1;
    }

    public static String getLogFileDir(Context context) {
        String logFilePath = null;
        logFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + logpath + "/" + "demolog";
        return logFilePath;
    }

    private static void msgFromParams(StringBuffer bf, String msg, Object... params) {
        if (msg == null) {
            return;
        }
        if (params == null) {
            bf.append(msg);
            return;
        }
        String msgArray[] = msg.split("\\{\\?\\}");
        int minLen = Math.min(msgArray.length, params.length);
        for (int i = 0; i < minLen; i++) {
            Object param = params[i];
            bf.append(msgArray[i]).append(param);
        }
        for (int i = minLen; i < msgArray.length; i++) {
            bf.append(msgArray[i]);
        }
    }

    private static class FileLogger {
        private final static long MAX_FILE_SIZE = 5 * 1024 * 1024;
        public static int MAX_LOG_NUMBER = 3;
        private static final String FILE_NAME = "CustomMap.log";
        private String filePath = null;
        private boolean isCheckedLogDir = false;
        private boolean isLogFileExist = false;
        private File mCurrentLogFile;
        private StringBuffer bf = new StringBuffer();
        private long lastSaveLogTime = 0;

        private void checkLogFile() {
            if (applicationContext == null || isCheckedLogDir) {
                return;
            }
            filePath = getLogFileDir(applicationContext);
            File tempFile = new File(filePath);
            if (tempFile.exists()) {
                if (!tempFile.isDirectory()) {
                    tempFile.delete();
                }
            }
            isCheckedLogDir = true;
            File logFilepath = new File(filePath);
            if (!logFilepath.exists()) {
                boolean ret = logFilepath.mkdirs();
                if (!ret) {
                    Log.w(sTag, "create file Path: " + filePath + " failed!");
                }

            }
            if (logFilepath.exists() && logFilepath.isDirectory()) {
                this.isLogFileExist = true;
            } else {
                Log.w(sTag, "dir not exist file Path: " + filePath);
            }
        }

        private boolean writeFile(byte[] aData, boolean append) {
            if (applicationContext == null) {
                return false;
            }
            OutputStream outFile = null;
            boolean result = false;
            try {
                outFile = new FileOutputStream(mCurrentLogFile, append);
                outFile.write(aData);
                outFile.flush();
                result = true;
            } catch (Exception e) {
            } finally {
                if (outFile != null) {
                    try {
                        outFile.close();
                    } catch (IOException e) {
                    }
                }
            }
            return result;
        }

        private void createLogFile() {
            if (mCurrentLogFile == null) {
                mCurrentLogFile = new File(filePath, FILE_NAME);
            }
            try {
                if (!mCurrentLogFile.exists()) {
                    if (isLogFileExist) {
                        boolean ret;
                        ret = mCurrentLogFile.createNewFile();
                    }
                } else {
                    if (mCurrentLogFile.length() > MAX_FILE_SIZE) {
                        resetLogFiles();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private void resetLogFiles() throws IOException {
            String path = mCurrentLogFile.getParent();
            String fileName = mCurrentLogFile.getName();
            File deleteFile = new File(path, mCurrentLogFile.getName() + "." + (MAX_LOG_NUMBER + 1));
            if (deleteFile.exists()) {
                deleteFile.delete();
            }
            for (int i = MAX_LOG_NUMBER; i > 0; i--) {
                File f = new File(path, mCurrentLogFile.getName() + "." + i);
                if (f.exists()) {
                    f.renameTo(new File(path, mCurrentLogFile.getName() + "." + (i + 1)));
                }
            }
            mCurrentLogFile.renameTo(new File(path, mCurrentLogFile.getName() + "." + 1));
            mCurrentLogFile = new File(path, fileName);
            mCurrentLogFile.createNewFile();
        }

        public void write(String tag, long milliseconds, String threadName, String msg, Object... params) {
            bf.append(df.format(new Date(milliseconds))).append("##");
            bf.append("/[").append(Process.myPid()).append("]");
            bf.append("[").append(threadName).append("]");
            bf.append(tag).append(":");
            msgFromParams(bf, msg, params);
            bf.append("\r\n");

            if (SystemClock.elapsedRealtime() - lastSaveLogTime > 2000) {
                try {
                    writeFile(bf.toString().getBytes(CHARSETNAME_UTF8), true);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                lastSaveLogTime = SystemClock.elapsedRealtime();
                bf.setLength(0);
            }

        }

        public void flushFile() {
            writeFile(null, true);
        }
    }

    private static class LoggerThread {
        private static LoggerThread instance;
        private List<Runnable> taskLogList = Collections.synchronizedList(new LinkedList<Runnable>());
        private Object locker = new Object();
        private boolean isWait = false;

        private LoggerThread() {
            initThread();
        }

        private static LoggerThread getInstance() {
            if (instance == null) {
                instance = new LoggerThread();
            }
            return instance;
        }

        public void write(final String tag, final String msg, final Object... params) {
            final long now = System.currentTimeMillis();
            final String currentThreadName = Thread.currentThread().getName();

            fileLogger.checkLogFile();

            taskLogList.add(() -> {
                setLowThreadPriority();

                fileLogger.createLogFile();
                fileLogger.write(tag, now, currentThreadName, msg, params);
            });
            notifyLock();
        }

        public void flushToFile() {
            fileLogger.flushFile();
            //暂时先不需要销毁，现在有问题，就是退出后，状态还保存，下次再请求，没办法进行初始化优化
        }

        private void setLowThreadPriority() {
            try {
                Process.setThreadPriority(Process.THREAD_PRIORITY_LOWEST);
                Thread.currentThread().setPriority(Thread.MIN_PRIORITY);
            } catch (Throwable e) {
            }
        }

        private void initThread() {
            new Thread(() -> {
                setLowThreadPriority();
                while (true) {
                    try {
                        executeTask();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }, "LoggerThread").start();
        }

        private void executeTask() throws InterruptedException {
            int logTaskSize = taskLogList.size();
            if (logTaskSize > 0) {
                try {
                    Runnable r = taskLogList.remove(0);
                    r.run();
                } catch (Throwable e) {
                }
            } else {
                synchronized (locker) {
                    isWait = true;
                    locker.wait();
                    isWait = false;
                }
            }
        }

        private void notifyLock() {
            if (isWait) {
                try {
                    synchronized (locker) {
                        locker.notify();
                    }
                } catch (Throwable e) {
                }
            }
        }
    }

}