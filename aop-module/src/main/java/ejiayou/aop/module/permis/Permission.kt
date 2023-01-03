package ejiayou.aop.module.permis


/**
 * @author: lr
 * @created on: 2022/12/29 9:25 下午
 * @description:
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(value = AnnotationRetention.RUNTIME)
annotation class Permission(vararg val value: String, val requestCode: Int = 1, val rationale: String = "")

