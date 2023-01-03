package ejiayou.aop.module.permis

/**
 * @author: lr
 * @created on: 2022/12/29 9:27 下午
 * @description:
 */
interface ResultPermissionCall {

    //请求权限前
    fun permissionBefore(rationale: String)

    //同意权限
    fun permissionGranted()

    //拒绝权限并且选中不再提示
    fun permissionNoAskDenied(requestCode: Int, denyNoAskList: List<String>)

    //取消权限
    fun permissionDenied(requestCode: Int, denyList: List<String>)
}
