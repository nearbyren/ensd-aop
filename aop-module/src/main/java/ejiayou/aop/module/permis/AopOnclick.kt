package ejiayou.aop.module.permis

/**
 * @author:
 * @created on: 2023/1/5 16:06
 * @description:
 */
@Target(AnnotationTarget.FUNCTION)
annotation class AopOnclick(val value: Long = 1000)
