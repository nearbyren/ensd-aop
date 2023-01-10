package ejiayou.aop.module.dialog

import android.os.Bundle
import android.view.View
import ejiayou.aop.module.model.Description
import ejiayou.aop.module.model.PermissionDto
import ejiayou.aop.module.adapter.PermissionTextAdapter
import ejiayou.aop.module.R
import ejiayou.aop.module.databinding.PermissionDialogBinding
import ejiayou.uikit.module.dialog.BaseBindDialogFragment

/**
 * @author:
 * @created on: 2022/7/14 14:01
 * @description:
 */
class PermissionDialog(val map: MutableMap<String, PermissionDto>) : BaseBindDialogFragment<PermissionDialogBinding>() {


    override fun getLayoutId(): Int {
        return R.layout.permission_dialog
    }


    override fun initialize(view: View, savedInstanceState: Bundle?) {
        val noticeAars = mutableListOf<Description>()
        map.let {
            for (map in map.entries) {
//                val key = map.key
                val value = map.value
                binding.permissionTvTitle.text = value.description.title
                binding.permissionTvContent.text = value.description.content
                noticeAars.add(Description(value.description.title, value.description.content))
            }
        }
        binding.stationNoticeBanner.addBannerLifecycleObserver(this)
        binding.stationNoticeBanner.setAdapter(PermissionTextAdapter(requireActivity(), noticeAars))
        binding.stationNoticeBanner.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.stationNoticeBanner.destroy()
    }
}