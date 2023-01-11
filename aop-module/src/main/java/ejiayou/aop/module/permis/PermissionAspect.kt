package ejiayou.aop.module.permis

import android.content.Context
import androidx.fragment.app.Fragment
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import java.lang.reflect.InvocationTargetException

/**
 * @author: lr
 * @created on: 2022/12/29 9:23 下午
 * @description:
 */
@Aspect
open class PermissionAspect {
    @Around("execution(@ejiayou.aop.module.permis.Permission * *(..)) && @annotation(permission)")
    @Throws(Throwable::class)
    open fun aroundJoinPoint(joinPoint: ProceedingJoinPoint, permission: Permission?) {
        println("Aspect - aroundJoinPoint = 启动...")
        var context: Context? = null
        val obj = joinPoint.getThis()
        if (obj is Context) {
            context = obj
        } else if (obj is Fragment) {
            context = obj.activity
        }
        if (context == null || permission == null) {
            joinPoint.proceed()
            return
        }
        val cls: Class<*> = obj.javaClass
        val methods = cls.declaredMethods
        if (methods.isEmpty()) return
        for (method in methods) {
            //过滤不含自定义注解PermissionNoAskDenied的方法
            val isHasAnnotation = method.isAnnotationPresent(GoPermission::class.java)
            if (isHasAnnotation) {
                method.isAccessible = true
                //获取方法类型
                val types = method.parameterTypes
                if (types.size != 4) return
                //获取方法上的注解
                method.getAnnotation(GoPermission::class.java) ?: return
                //解析注解上对应的信息
                try {
                    //去请求权限
                    method.invoke(obj, permission.value, permission.requestCode, permission.rationale, object : ResultPermissionCall {

                        override fun permissionBefore(rationale: String) {
                            println("Aspect - permissionBefore")
                            val javaClass: Class<*> = obj.javaClass
                            val declaredMethods = javaClass.declaredMethods
                            if (declaredMethods.isEmpty()) return
                            for (declaredMethod in declaredMethods) {
                                //过滤不含自定义注解PermissionBefore的方法
                                val isHasAnnotations =
                                    declaredMethod.isAnnotationPresent(PermissionBefore::class.java)
                                if (isHasAnnotations) {
                                    declaredMethod.isAccessible = true
                                    //获取方法类型
                                    val parameterTypes = declaredMethod.parameterTypes
                                    if (parameterTypes.size != 1) return
                                    //获取方法上的注解
                                    declaredMethod.getAnnotation(PermissionBefore::class.java) ?: return
                                    //解析注解上对应的信息
                                    try {
                                        declaredMethod.invoke(obj, rationale)
                                    } catch (e: IllegalAccessException) {
                                        e.printStackTrace()
                                    } catch (e: InvocationTargetException) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        }

                        override fun permissionGranted() {
                            println("Aspect - permissionGranted")
                            //许可授予
                            try {
                                joinPoint.proceed()
                            } catch (throwable: Throwable) {
                                throwable.printStackTrace()
                            }
                        }

                        override fun permissionNoAskDenied(requestCode: Int, denyNoAskList: List<String>) {
                            println("Aspect - permissionNoAskDenied")
                            //许可授予 不再询问
                            val javaClass: Class<*> = obj.javaClass
                            val declaredMethods = javaClass.declaredMethods
                            if (declaredMethods.isEmpty()) return
                            for (declaredMethod in declaredMethods) {
                                //过滤不含自定义注解PermissionNoAskDenied的方法
                                val isHasAnnotations =
                                    declaredMethod.isAnnotationPresent(PermissionNoAskDenied::class.java)
                                if (isHasAnnotations) {
                                    declaredMethod.isAccessible = true
                                    //获取方法类型
                                    val parameterTypes = declaredMethod.parameterTypes
                                    if (parameterTypes.size != 2) return
                                    //获取方法上的注解
                                    declaredMethod.getAnnotation(PermissionNoAskDenied::class.java)
                                        ?: return
                                    //解析注解上对应的信息
                                    try {
                                        declaredMethod.invoke(obj, requestCode, denyNoAskList)
                                    } catch (e: IllegalAccessException) {
                                        e.printStackTrace()
                                    } catch (e: InvocationTargetException) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        }

                        override fun permissionDenied(requestCode: Int, denyList: List<String>) {
                            println("Aspect - permissionDenied")
                            //许可授予 拒绝
                            val javaClass: Class<*> = obj.javaClass
                            val declaredMethods = javaClass.declaredMethods
                            if (declaredMethods.isEmpty()) return
                            for (declaredMethod in declaredMethods) {
                                //过滤不含自定义注解PermissionDenied的方法
                                val isHasAnnotations =
                                    declaredMethod.isAnnotationPresent(PermissionDenied::class.java)
                                if (isHasAnnotations) {
                                    declaredMethod.isAccessible = true
                                    //获取方法类型
                                    val parameterTypes = declaredMethod.parameterTypes
                                    if (parameterTypes.size != 2) return
                                    //获取方法上的注解
                                    declaredMethod.getAnnotation(PermissionDenied::class.java) ?: return
                                    //解析注解上对应的信息
                                    try {
                                        declaredMethod.invoke(obj, requestCode, denyList)
                                    } catch (e: IllegalAccessException) {
                                        e.printStackTrace()
                                    } catch (e: InvocationTargetException) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        }

                    })
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                } catch (e: InvocationTargetException) {
                    e.printStackTrace()
                }
            }
        }
    }
}
