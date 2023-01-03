package ejiayou.aop.module.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.youth.banner.adapter.BannerAdapter
import ejiayou.aop.module.model.Description
import ejiayou.aop.module.R

/**
 * @author: lr
 * @created on: 2022/11/6 10:12 上午
 * @description:
 */
class PermissionTextAdapter(var context: Context, texts: List<Description>) : BannerAdapter<Description, PermissionTextAdapter.TextHolder>(texts) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): TextHolder {
        val layout =
            LayoutInflater.from(context).inflate(R.layout.permission_dialog_item, parent, false)
        return TextHolder(layout)
    }

    override fun onBindView(holder: TextHolder, description: Description, position: Int, size: Int) {
        holder.title.text = description.title
        holder.content.text = description.content
    }

    class TextHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.permission_tv_title)
        var content: TextView = view.findViewById(R.id.permission_tv_content)
    }

}
