package ejiayou.aop.module.util

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.util.Log


/**
 * @author: lr
 * @created on: 2023/1/1 11:28 上午
 * @description:
 */
object PermissionPageManagement {
    private const val TAG = "JumpPermissionManagement"

    /**
     * Build.MANUFACTURER
     */
    private const val MANUFACTURER_HUAWEI = "HUAWEI" //华为
    private const val MANUFACTURER_MEIZU = "Meizu" //魅族
    private const val MANUFACTURER_XIAOMI = "Xiaomi" //小米
    private const val MANUFACTURER_SONY = "Sony" //索尼
    private const val MANUFACTURER_OPPO = "OPPO" //oppo
    private const val MANUFACTURER_LG = "LG"
    private const val MANUFACTURER_VIVO = "vivo" //vivo
    private const val MANUFACTURER_SAMSUNG = "samsung" //三星
    private const val MANUFACTURER_ZTE = "ZTE" //中兴
    private const val MANUFACTURER_YULONG = "YuLong" //酷派
    private const val MANUFACTURER_LENOVO = "LENOVO" //联想

    /**
     * 此函数可以自己定义
     * @param activity
     */
    fun goToSetting(activity: Activity) {
        when (Build.MANUFACTURER) {
            MANUFACTURER_HUAWEI -> Huawei(activity)
            MANUFACTURER_MEIZU -> Meizu(activity)
            MANUFACTURER_XIAOMI -> Xiaomi(activity)
            MANUFACTURER_SONY -> Sony(activity)
            MANUFACTURER_OPPO -> OPPO(activity)
            MANUFACTURER_VIVO -> VIVO(activity)
            MANUFACTURER_LG -> LG(activity)
            else -> {
                ApplicationInfo(activity)
                Log.e("goToSetting", "目前暂不支持此系统")
            }
        }
    }

    fun Huawei(activity: Activity) {
        try {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("packageName", activity.applicationInfo.packageName)
            val comp =
                ComponentName("com.huawei.systemmanager", "com.huawei.permissionmanager.ui.MainActivity")
            intent.component = comp
            activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            goIntentSetting(activity)
        }
    }

    fun Meizu(activity: Activity) {
        try {
            val intent = Intent("com.meizu.safe.security.SHOW_APPSEC")
            intent.addCategory(Intent.CATEGORY_DEFAULT)
            intent.putExtra("packageName", activity.packageName)
            activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            goIntentSetting(activity)
        }
    }

    fun Xiaomi(activity: Activity) {
        try {
            val intent = Intent("miui.intent.action.APP_PERM_EDITOR")
            intent.putExtra("extra_pkgname", activity.packageName)
            val componentName =
                ComponentName("com.miui.securitycenter", "com.miui.permcenter.permissions.PermissionsEditorActivity")
            intent.component = componentName
            activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            goIntentSetting(activity)
        }
    }

    fun Sony(activity: Activity) {
        try {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("packageName", activity.packageName)
            val comp = ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity")
            intent.component = comp
            activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            goIntentSetting(activity)
        }
    }

    fun OPPO(activity: Activity) {
        try {
            val intent = Intent()
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("packageName", activity.packageName)
            //        ComponentName comp = new ComponentName("com.color.safecenter", "com.color.safecenter.permission.PermissionManagerActivity");
            val comp =
                ComponentName("com.coloros.securitypermission", "com.coloros.securitypermission.permission.PermissionAppAllPermissionActivity") //R11t 7.1.1 os-v3.2
            intent.component = comp
            activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            goIntentSetting(activity)
        }
    }

    fun VIVO(activity: Activity) {
        val localIntent: Intent
        if (Build.MODEL.contains("Y85") && !Build.MODEL.contains("Y85A") || Build.MODEL.contains("vivo Y53L")) {
            localIntent = Intent()
            localIntent.setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.PurviewTabActivity")
            localIntent.putExtra("packagename", activity.packageName)
            localIntent.putExtra("tabId", "1")
            activity.startActivity(localIntent)
        } else {
            localIntent = Intent()
            localIntent.setClassName("com.vivo.permissionmanager", "com.vivo.permissionmanager.activity.SoftPermissionDetailActivity")
            localIntent.action = "secure.intent.action.softPermissionDetail"
            localIntent.putExtra("packagename", activity.packageName)
            activity.startActivity(localIntent)
        }
    }

    fun LG(activity: Activity) {
        try {
            val intent = Intent("android.intent.action.MAIN")
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("packageName", activity.packageName)
            val comp =
                ComponentName("com.android.settings", "com.android.settings.Settings\$AccessLockSummaryActivity")
            intent.component = comp
            activity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            goIntentSetting(activity)
        }
    }

    /**
     * 只能打开到自带安全软件
     * @param activity
     */
    fun _360(activity: Activity) {
        val intent = Intent("android.intent.action.MAIN")
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra("packageName", activity.packageName)
        val comp =
            ComponentName("com.qihoo360.mobilesafe", "com.qihoo360.mobilesafe.ui.index.AppEnterActivity")
        intent.component = comp
        activity.startActivity(intent)
    }

    /**
     * 应用信息界面
     * @param activity
     */
    fun ApplicationInfo(activity: Activity) {
        val localIntent = Intent()
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.action = "android.settings.APPLICATION_DETAILS_SETTINGS"
            localIntent.data = Uri.fromParts("package", activity.packageName, null)
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.action = Intent.ACTION_VIEW
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails")
            localIntent.putExtra("com.android.settings.ApplicationPkgName", activity.packageName)
        }
        activity.startActivity(localIntent)
    }

    /**
     * 系统设置界面
     * @param activity
     */
    fun SystemConfig(activity: Activity) {
        val intent = Intent(Settings.ACTION_SETTINGS)
        activity.startActivity(intent)
    }

    /**
     * 默认打开应用详细页
     */
    private fun goIntentSetting(pActivity: Activity) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri: Uri = Uri.fromParts("package", pActivity.packageName, null)
        intent.data = uri
        try {
            pActivity.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
