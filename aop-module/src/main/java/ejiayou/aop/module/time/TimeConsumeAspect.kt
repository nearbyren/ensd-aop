package ejiayou.aop.module.time

import android.util.Log
import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Before
import org.aspectj.lang.annotation.Pointcut
import org.aspectj.lang.reflect.MethodSignature

/**
 * @author: lr
 * @created on: 2022/12/29 7:52 下午
 * @description:
 */
@Aspect
class TimeConsumeAspect {


    var startTime: Long = 0

    @Pointcut("execution(@ejiayou.aop.module.time.TimeConsume * *(..))")
    fun methodTimeConsumePoint() {
    }

    @Before("methodTimeConsumePoint()")
    fun doBefore(joinPoint: JoinPoint) {
        val signature: MethodSignature = joinPoint.signature as MethodSignature
        val method = signature.method
        println("Aspect - doBefore: $method")
        startTime = System.currentTimeMillis()
    }

    @After("methodTimeConsumePoint()")
    fun doAfter() {
        val endTime = System.currentTimeMillis()
        val consumeTime = endTime - startTime
        println("Aspect - 开始于${startTime}，结束于$endTime, 耗时 $consumeTime ms")
    }
}