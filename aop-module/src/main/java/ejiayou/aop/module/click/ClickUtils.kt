package ejiayou.aop.module.click

/**
 * @author:
 * @created on: 2023/1/11 14:00
 * @description:
 */
class ClickUtils private constructor() {
    private val SINGLE_INTERVAL = 400L
    private var lastClickTime: Long

    companion object {

        @JvmStatic
        fun getInstance(): ClickUtils {
            return SingletonHolder.holder
        }
    }

    private object SingletonHolder {
        val holder = ClickUtils()
    }

    init {
        lastClickTime = System.currentTimeMillis() - (1 + SINGLE_INTERVAL)
    }

    fun isNoFastClick(): Boolean {
        val t = System.currentTimeMillis()
        if (Math.abs(t - lastClickTime) > SINGLE_INTERVAL) {
            lastClickTime = t
            return true
        }
        return false
    }
}