package com.dhy.utilslibrary.tts;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import java.util.Locale;

/**
 * Created by dhy
 * Date: 2020/10/26
 * Time: 11:27
 * describe:
 */

public class TextToSpeechImpl implements TextToSpeech.OnInitListener {

    private Context mContext;
    private TextToSpeech mTTS;


    public TextToSpeechImpl(Context context){
        mContext = context;
        mTTS = new TextToSpeech(mContext,this);
    }

    public boolean speak(final String text){
        Log.d("dione","speak text:"+text);
        // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
        mTTS.setPitch(1.0f);
        // 设置语速
        mTTS.setSpeechRate(1.0f);
        //播放语音
        int ret = mTTS.speak(text,TextToSpeech.QUEUE_ADD,null);
        if(ret == 0) {
            Toast.makeText(mContext, "文字转语音成功...", Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            Toast.makeText(mContext, "文字转语音错误...", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onInit(int status) {
        // 判断是否转化成功
        if (status == TextToSpeech.SUCCESS){
            //默认设定语言为中文，原生的android貌似不支持中文。
            int result = mTTS.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED){
                Toast.makeText(mContext, "数据丢失或不支持..."+String.valueOf(result), Toast.LENGTH_SHORT).show();
            }else{
                //不支持中文就将语言设置为英文
                mTTS.setLanguage(Locale.US);
            }
        }
    }
}
