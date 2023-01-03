package ejiayou.aop

/**
 * @author: lr
 * @created on: 2022/12/29 9:06 下午
 * @description:
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(value = AnnotationRetention.RUNTIME)
annotation class FastClickView(val interval: Long = 3000L)
