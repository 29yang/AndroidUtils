package com.dhy.utils

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dhy.utilslibrary.logger.LoggerUtil


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        var dimension = resources.getDimension(R.dimen.dp_10)
        val dm2 = resources.displayMetrics
        println("Loggerdensity : " + dm2.density)
        println("LoggerdensityDPI : " + dm2.densityDpi)
        println("LoggerUtilheigth2 : " + dm2.heightPixels)
        println("LoggerUtilwidth2 : " + dm2.widthPixels)
        LoggerUtil.e("${dimension/dm2.density}")
    }
}