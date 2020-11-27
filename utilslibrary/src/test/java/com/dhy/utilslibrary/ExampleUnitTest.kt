package com.dhy.utilslibrary

import com.dhy.utilslibrary.keyboard.KeyBoardUtil
import com.dhy.utilslibrary.math.NumberUtil
import com.dhy.utilslibrary.math.TimeUtil
import org.junit.Test

import org.junit.Assert.*
import java.sql.Time

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        print(TimeUtil.getPreTime("2020-10-2 12:12:12","-60"))
    }
}