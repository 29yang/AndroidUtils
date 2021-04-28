package com.dhy.utilslibrary.media

import android.media.MediaPlayer

/**
 * Created by dhy
 * Date: 2020/1/9
 * Time: 15:54
 * describe:
 */
class MediaPlayerUtil {
    var mediaPLayer: MediaPlayer? = null
    fun playSound(soundPath:String){
        if (mediaPLayer == null){
            mediaPLayer = MediaPlayer()
        }
        mediaPLayer?.reset()
        mediaPLayer?.setDataSource(soundPath)
        mediaPLayer?.prepare()
        mediaPLayer?.start()
    }

    fun stopSound(){
        if (mediaPLayer != null && mediaPLayer?.isPlaying!!){
            mediaPLayer?.stop()
            mediaPLayer?.release()
            mediaPLayer = null
        }
    }

    fun onDestroy(){
        mediaPLayer?.stop()
        mediaPLayer?.release()
        mediaPLayer = null
    }
}