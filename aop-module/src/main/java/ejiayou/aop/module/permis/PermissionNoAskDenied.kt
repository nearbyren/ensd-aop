package ejiayou.aop.module.permis


/**
 * @author: lr
 * @created on: 2022/12/29 9:26 下午
 * @description:
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(value = AnnotationRetention.RUNTIME)
annotation class PermissionNoAskDenied

