package ejiayou.aop.module.permis

import android.content.Context
import android.util.Log
import androidx.fragment.app.Fragment
import com.orhanobut.logger.Logger
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
    fun aroundJoinPoint(joinPoint: ProceedingJoinPoint, permission: Permission?) {
        Logger.d("Permission  aroundJoinPoint = 启动...")
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
                            val cls: Class<*> = obj.javaClass
                            val methods = cls.declaredMethods
                            if (methods.isEmpty()) return
                            for (method in methods) {
                                //过滤不含自定义注解PermissionBefore的方法
                                val isHasAnnotation =
                                    method.isAnnotationPresent(PermissionBefore::class.java)
                                if (isHasAnnotation) {
                                    method.isAccessible = true
                                    //获取方法类型
                                    val types = method.parameterTypes
                                    if (types.size != 1) return
                                    //获取方法上的注解
                                    method.getAnnotation(PermissionBefore::class.java) ?: return
                                    //解析注解上对应的信息
                                    try {
                                        method.invoke(obj, rationale)
                                    } catch (e: IllegalAccessException) {
                                        e.printStackTrace()
                                    } catch (e: InvocationTargetException) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        }

                        override fun permissionGranted() {
                            //许可授予
                            try {
                                joinPoint.proceed()
                            } catch (throwable: Throwable) {
                                throwable.printStackTrace()
                            }
                        }

                        override fun permissionNoAskDenied(requestCode: Int, denyNoAskList: List<String>) {
                            //许可授予 不再询问
                            val cls: Class<*> = obj.javaClass
                            val methods = cls.declaredMethods
                            if (methods.isEmpty()) return
                            for (method in methods) {
                                //过滤不含自定义注解PermissionNoAskDenied的方法
                                val isHasAnnotation =
                                    method.isAnnotationPresent(PermissionNoAskDenied::class.java)
                                if (isHasAnnotation) {
                                    method.isAccessible = true
                                    //获取方法类型
                                    val types = method.parameterTypes
                                    if (types.size != 2) return
                                    //获取方法上的注解
                                    method.getAnnotation(PermissionNoAskDenied::class.java)
                                        ?: return
                                    //解析注解上对应的信息
                                    try {
                                        method.invoke(obj, requestCode, denyNoAskList)
                                    } catch (e: IllegalAccessException) {
                                        e.printStackTrace()
                                    } catch (e: InvocationTargetException) {
                                        e.printStackTrace()
                                    }
                                }
                            }
                        }

                        override fun permissionDenied(requestCode: Int, denyList: List<String>) {
                            //许可授予 拒绝
                            val cls: Class<*> = obj.javaClass
                            val methods = cls.declaredMethods
                            if (methods.isEmpty()) return
                            for (method in methods) {
                                //过滤不含自定义注解PermissionDenied的方法
                                val isHasAnnotation =
                                    method.isAnnotationPresent(PermissionDenied::class.java)
                                if (isHasAnnotation) {
                                    method.isAccessible = true
                                    //获取方法类型
                                    val types = method.parameterTypes
                                    if (types.size != 2) return
                                    //获取方法上的注解
                                    method.getAnnotation(PermissionDenied::class.java) ?: return
                                    //解析注解上对应的信息
                                    try {
                                        method.invoke(obj, requestCode, denyList)
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
