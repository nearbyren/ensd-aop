package ejiayou.aop.module.util

import android.os.SystemClock

/**
 * @author:
 * @created on: 2023/1/5 16:12
 * @description:
 */
object AopClickUtil {
    /**
     * 最近一次点击的时间
     */
    private var mLastClickTime: Long = 0

    /**
     * 是否是快速点击
     *
     * @param intervalMillis  时间间期（毫秒）
     * @return  true:是，false:不是
     */
    fun isFastDoubleClick(intervalMillis: Long): Boolean {
        //        long time = System.currentTimeMillis();
        val time = SystemClock.elapsedRealtime()
        val timeInterval = Math.abs(time - mLastClickTime)
        println("time = $time - mLastClickTime = $mLastClickTime - timeInterval = $timeInterval - intervalMillis = $intervalMillis")
        return if (timeInterval < intervalMillis) {
            true
        } else {
            mLastClickTime = time
            false
        }
    }
}