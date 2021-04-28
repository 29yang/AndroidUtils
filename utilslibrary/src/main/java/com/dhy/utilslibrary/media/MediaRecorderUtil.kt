package com.dhy.utilslibrary.media

import android.content.Context
import android.media.MediaRecorder
import java.io.File
import android.R.attr.start
import android.os.Environment
import android.os.Environment.getExternalStorageDirectory
import android.text.format.DateFormat
import java.util.*


/**
 * Created by dhy
 * Date: 2020/1/9
 * Time: 15:07
 * describe:
 */
class MediaRecorderUtil {
    private var mRecorder: MediaRecorder? = null
    //开启录音
    fun startRecord(): String {
        this.mRecorder = MediaRecorder()
        mRecorder?.reset()
        mRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC) //设置麦克风
        mRecorder?.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)//录制的音频文件的格式
        mRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
        val fileName =
            "${DateFormat.format("yyyyMMdd_HHmmss", Calendar.getInstance(Locale.CHINA))}.m4a"
        val destDir = File("${getExternalStorageDirectory()}/test/")
        if (!destDir.exists()) {
            destDir.mkdirs()
        }
        val filePath = "${getExternalStorageDirectory()}/test/$fileName"
        mRecorder?.setOutputFile(filePath)
        mRecorder?.prepare()
        mRecorder?.start()
        return filePath
    }

    //停止
    fun stopRecord() {
        onDestroy()
    }

    fun onDestroy() {
        mRecorder?.stop()
        mRecorder?.release()
        mRecorder = null
    }


}