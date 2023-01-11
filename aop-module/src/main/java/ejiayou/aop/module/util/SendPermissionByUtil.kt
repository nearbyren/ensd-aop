package ejiayou.aop.module.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.text.TextUtils
import android.view.Gravity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.orhanobut.logger.Logger
import ejiayou.aop.module.model.Description
import ejiayou.aop.module.model.PermissionDto
import ejiayou.aop.module.dialog.PermissionDialog
import ejiayou.aop.module.permis.ResultPermissionCall

/**
 * @author: lr
 * @created on: 2022/12/31 4:14 下午
 * @description:
 */
open class SendPermissionByUtil private constructor() {

    private lateinit var activity: FragmentActivity
    private lateinit var resultCall: ResultPermissionCall
    private var permissionDialog: PermissionDialog? = null


    private var MIN_SDK_PERMISSIONS: MutableMap<String, Int> = mutableMapOf(

        //相机
        "android.permission.CAMERA" to 1,
        //访问 粗略位置 精细位置
        "android.permission.ACCESS_COARSE_LOCATION" to 2,
        "android.permission.ACCESS_FINE_LOCATION" to 3,
        //使用的传感器来测量
        "android.permission.BODY_SENSORS" to 4,
        //拨打电话
        "android.permission.CALL_PHONE" to 5,
        //访问账户Gmail列表
        "android.permission.GET_ACCOUNTS" to 6,
        //处理拨出电话
        "android.permission.PROCESS_OUTGOING_CALLS" to 7,
        //读取日程提醒
        "android.permission.READ_CALENDAR" to 8,
        //读取通话记录
        "android.permission.READ_CALL_LOG" to 9,
        //读取联系人
        "android.permission.READ_CONTACTS" to 10,
        //读取电话状态
        "android.permission.READ_PHONE_STATE" to 11,
        //读取短信内容
        "android.permission.READ_SMS" to 112,
        //接收彩信
        "android.permission.RECEIVE_MMS" to 13,
        //接收短信
        "android.permission.RECEIVE_SMS" to 14,
        //录音
        "android.permission.RECORD_AUDIO" to 15,
        //发送短信
        "android.permission.SEND_SMS" to 16,
        //写入日程提醒
        "android.permission.WRITE_CALENDAR" to 17,
        //写入联系人数据
        "android.permission.WRITE_CALL_LOG" to 18,
        //写入外部存储
        "android.permission.WRITE_EXTERNAL_STORAGE" to 19,


        )

    private var DESCRIPTION_PERMISSIONS: MutableMap<String, PermissionDto> = mutableMapOf(
        "android.permission.ACCESS_FINE_LOCATION" to PermissionDto(
            "android.permission.ACCESS_FINE_LOCATION", Description(
                "访问地理位置信息权限说明", "为了判断和收集您的地理位置信息，实现推荐附近优惠加油的油站，不授权该权限会影响app正常使用。"
            )
        ),
        "android.permission.ACCESS_COARSE_LOCATION" to PermissionDto(
            "android.permission.ACCESS_COARSE_LOCATION", Description(
                "访问地理位置信息权限说明", "为了判断和收集您的地理位置信息，实现推荐附近优惠加油的油站，不授权该权限会影响app正常使用。"
            )
        ),
        "android.permission.WRITE_EXTERNAL_STORAGE" to PermissionDto(
            "android.permission.CAMERA", Description(
                "访问存储权限说明", "我们访问您的存储权限是为了更新用户头像"
            )
        ),
    )

    companion object {
        val instance: SendPermissionByUtil by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            SendPermissionByUtil()
        }

    }

    /**
     * 检测隐私权限返回结果
     * @param activity
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        println("Aspect - onRequestPermissionsResult ")
        if (verifyPermissions(*grantResults)) {
            resultCall.permissionGranted()
            //关闭权限说明
            dismissDialog()
        } else {
            if (!shouldShowRequestPermissionRationale(activity, *permissions)) {
                //权限被拒绝并且选中不再提示
                if (permissions.size != grantResults.size) return
                val denyNoAskList: MutableList<String> = ArrayList()
                for (i in grantResults.indices) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        denyNoAskList.add(permissions[i])
                    }
                }
                resultCall.permissionNoAskDenied(requestCode, denyNoAskList)
                showToSetting(activity)
            } else {
                //权限被取消
                val denyList: MutableList<String> = ArrayList()
                for (i in grantResults.indices) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        denyList.add(permissions[i])
                    }
                }
                resultCall.permissionDenied(requestCode, denyList)
                //关闭权限说明
                dismissDialog()
            }

        }
    }

    /**
     * 启动检测隐私权限
     * @param activity
     * @param permissions
     * @param requestCode
     * @param rationale
     * @param call
     */
    fun goPermissions(activity: FragmentActivity, vararg permissions: String, requestCode: Int, rationale: String, call: ResultPermissionCall) {
        println("Aspect - goPermissions ")
        resultCall = call
        this.activity = activity
        if (hasSelfPermissions(activity.applicationContext, *permissions)) {
            resultCall.permissionGranted()
        }
        //无提示语
        if (TextUtils.isEmpty(rationale)) {
            startReminder(*permissions)
            ActivityCompat.requestPermissions(activity, permissions, requestCode)
        } else {
            //提示语
            val shouldShowRequestPermissionRationale =
                shouldShowRequestPermissionRationale(activity, *permissions)
            if (shouldShowRequestPermissionRationale) {
                showRequestPermissionRationale(activity, requestCode, permissions = permissions, rationale = rationale)
            } else {
                startReminder(*permissions)
                ActivityCompat.requestPermissions(activity, permissions, requestCode)
            }
        }
    }

    /**
     * 打开隐私权限说明
     * @param activity
     * @param permissions
     */
    private fun startReminder(vararg permissions: String) {
        //获取未授权的并显示提示框
        val hasReminder = hasSelfPermissionsReminder(activity.applicationContext, *permissions)
        if (hasReminder.isNotEmpty()) {
            println("Aspect - showDialog ")
            permissionDialog = PermissionDialog(hasReminder)
            permissionDialog.let { dialog ->
                dialog?.setGravity(Gravity.TOP)
                dialog?.show(activity = activity, "permission_dialog")
            }
        }
    }

    /**
     * 关闭隐私权限说明
     */
    private fun dismissDialog() {
        println("Aspect - dismissDialog ")
        permissionDialog?.let {
            if (it.isVisible) {
                it.dismiss()
            }
        }
        permissionDialog = null
    }

    /**
     * 隐私权限被拒绝再次提示
     * @param activity
     * @param requestCode
     * @param permissions
     * @param rationale
     */
    private fun showRequestPermissionRationale(activity: Activity?, requestCode: Int, vararg permissions: String, rationale: String) {
        println("Aspect - showRequestPermissionRationale ")
        AlertDialog.Builder(activity).setMessage(rationale).setPositiveButton("确定") { dialog, _ ->
            dialog.dismiss()
            startReminder(*permissions)
            ActivityCompat.requestPermissions(activity!!, permissions, requestCode)
        }.setNegativeButton("取消") { dialog, _ ->
            dismissDialog()
            dialog.dismiss()
        }.create().show()
    }

    /***
     * 去设置页开启权限
     * @param activity
     */
    private fun showToSetting(activity: Activity) {
        println("Aspect - showToSetting ")
        AlertDialog.Builder(activity).setMessage("为了更好的体验，请打开相关权限")
                .setPositiveButton("确定") { dialog, _ ->
                    dialog.dismiss()
                    //关闭权限说明
                    dismissDialog()
                    PermissionPageManagement.goToSetting(activity)
                }.setNegativeButton("取消") { dialog, _ ->
                    dialog.dismiss()
                    //关闭权限说明
                    dismissDialog()
                }.create().show()
    }

    /**
     * 检查所给权限List是否需要给提示
     *
     * @param activity    Activity
     * @param permissions 权限list
     * @return 如果某个权限需要提示则返回true
     */
    private fun shouldShowRequestPermissionRationale(activity: Activity?, vararg permissions: String): Boolean {
        println("Aspect - shouldShowRequestPermissionRationale ")
        for (permission in permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity!!, permission)) {
                return true
            }
        }
        return false
    }

    /**
     * 判断是否所有权限都同意了，都同意返回true 否则返回false
     *
     * @param context     context
     * @param permissions permission list
     * @return return true if all permissions granted else false
     */
    private fun hasSelfPermissions(context: Context, vararg permissions: String): Boolean {
        println("Aspect - hasSelfPermissions ")
        for (permission in permissions) {
            if (permissionExists(permission) && !hasSelfPermission(context, permission)) {
                return false
            }
        }
        return true
    }


    /**
     * 判断是否所有权限都同意了，都同意返回true 否则返回false
     *
     * @param context     context
     * @param permissions permission list
     * @return return 未授权的权限提示语
     */
    private fun hasSelfPermissionsReminder(context: Context, vararg permissions: String): MutableMap<String, PermissionDto> {
        println("Aspect - hasSelfPermissionsReminder ")
        val has = mutableMapOf<String, PermissionDto>()
        for (permission in permissions) {
            if (permissionExists(permission) && !hasSelfPermission(context, permission)) {
                val p = DESCRIPTION_PERMISSIONS[permission]
                p?.let {
                    has[permission] = it
                }
            }
        }
        return has
    }

    /**
     * 判断权限是否存在
     *
     * @param permission permission
     * @return return true if permission exists in SDK version
     */
    private fun permissionExists(permission: String): Boolean {
        println("Aspect - permissionExists ")
        val minVersion = MIN_SDK_PERMISSIONS[permission]
        return minVersion == null || Build.VERSION.SDK_INT >= minVersion
    }

    /**
     * 判断单个权限是否同意
     *
     * @param context    context
     * @param permission permission
     * @return return true if permission granted
     */
    private fun hasSelfPermission(context: Context, permission: String): Boolean {
        println("Aspect - hasSelfPermission ")
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
    }

    /**
     * 检查是否都赋予权限
     *
     * @param grantResults grantResults
     * @return 所有都同意返回true 否则返回false
     */
    private fun verifyPermissions(vararg grantResults: Int): Boolean {
        println("Aspect - verifyPermissions ")
        if (grantResults.isEmpty()) return false
        for (result in grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

}