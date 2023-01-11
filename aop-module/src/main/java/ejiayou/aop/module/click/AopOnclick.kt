package ejiayou.aop.module.click

/**
 * @author:
 * @created on: 2023/1/5 16:06
 * @description:
 */
@Retention(AnnotationRetention.SOURCE)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.EXPRESSION)
annotation class AopOnclick(val value: Long = 1000)
