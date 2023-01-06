package ejiayou.aop.module.util

import android.view.View
import ejiayou.aop.module.R


/**
 * @author:
 * @created on: 2023/1/5 14:54
 * @description:
 */
object FastClickCheckUtil {
    /**
     * 判断是否属于快速点击
     *
     * @param view     点击的View
     * @param interval 快速点击的阈值
     * @return true：快速点击
     */
    fun isFastClick(view: View, interval: Long): Boolean {
        val key: Int = R.id.view_click_time

        // 最近的点击时间
        val currentClickTime = System.currentTimeMillis()
        if (null == view.getTag(key)) {
            // 1. 第一次点击

            // 保存最近点击时间
            view.setTag(key, currentClickTime)
            return false
        }
        // 2. 非第一次点击
        val target = view.getTag(key) as Long
        // 上次点击时间
        return if (currentClickTime - target < interval) {
            // 未超过时间间隔，视为快速点击
            true
        } else {
            // 保存最近点击时间
            view.setTag(key, currentClickTime)
            false
        }
    }
}