package ejiayou.aop.module.point

import org.aspectj.lang.JoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Aspect

/**
 * @author:
 * @created on: 2023/1/11 11:05
 * @description:
 */
@Aspect
open class DataPointAspect {
    @After("execution(@DataPoint * *..*.*(..))")
    fun afterPoint(point: JoinPoint) {
        val dataPointBehavior = point.`this` as DataPointBehavior
        val params = dataPointBehavior.params()
        println("Aspect - 埋点数据 $params")
    }
}