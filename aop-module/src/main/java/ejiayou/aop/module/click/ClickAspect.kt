package ejiayou.aop.module.click

import ejiayou.aop.module.util.AopClickUtil
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import kotlin.jvm.Throws

/**
 * @author:
 * @created on: 2023/1/11 14:00
 * @description:
 */
@Aspect
class ClickAspect {
    @Pointcut("execution(@ejiayou.aop.module.click.AopOnclick * *(..))")
    fun methodClick() {
    }

    @Around("methodClick()")
    @Throws(Throwable::class)
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint) {
        val methodSignature = joinPoint.signature as MethodSignature
        val method = methodSignature.method
        if (!method.isAnnotationPresent(AopOnclick::class.java)) {
            return
        }
        val aopOnclick = method.getAnnotation(AopOnclick::class.java)
        if (!AopClickUtil.isFastDoubleClick(aopOnclick.value)) {
            joinPoint.proceed()
        }
    }
}