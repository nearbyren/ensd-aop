> 所持有的模块
# aop权限使用规则

```
    @Permission(value = [Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE], requestCode = 999, rationale = "为了更好的体验，请打开相关权限")
    fun permission(view: View?) {
        Logger.d("Permission  permission: 权限已打开")
            
    }

    @PermissionBefore
    fun permissionBefore(rationale: String) {
        Logger.d("Permission  permissionBefore rationale = $rationale")
    }

    /**
     * 请求拒绝
     */
    @PermissionDenied
    fun permissionDenied(requestCode: Int, denyList: List<String>) {
        for (deny in denyList) {
            Logger.d("Permission  permissionDenied = $deny")
        }
    }

    /**
     * 请求拒绝且不在提示
     */
    @PermissionNoAskDenied
    fun permissionNoAskDenied(requestCode: Int, denyNoAskList: List<String>) {
        for (deny in denyNoAskList) {
            Logger.d("Permission  permissionNoAskDenied requestCode = $requestCode = deny = $deny")
        }
    }


    /**
     * @param permissions 请求权限集合
     * @param requestCode 请求码
     */
    @GoPermission
    fun goPermissions(vararg permissions: String, requestCode: Int, rationale: String, call: ResultPermissionCall) {
        Logger.d("Permission  goPermissions")
        SendPermissionByUtil.instance.goPermissions(this, *permissions, requestCode = requestCode, rationale = rationale, call = call)
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        Logger.d("Permission  onRequestPermissionsResult")
        SendPermissionByUtil.instance.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

```