package ejiayou.aop.module.permis

import ejiayou.aop.module.util.AopClickUtil
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature
import kotlin.jvm.Throws

/**
 * @author:
 * @created on: 2023/1/5 16:06
 * @description:
 */
@Aspect
class AopClickAspect {
    @Pointcut("execution(@ejiayou.aop.module.permis.AopOnclick * *(..))")
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