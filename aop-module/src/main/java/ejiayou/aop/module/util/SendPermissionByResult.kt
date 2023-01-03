package ejiayou.aop.module.util

/**
 * @author: lr
 * @created on: 2022/12/31 4:21 下午
 * @description:
 */
interface SendPermissionByResult {

    fun byBefore()

    fun byDenied()

    fun byNoAskDenied()
}