package com.mredrock.cyxbs.freshman

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.mredrock.cyxbs.freshman.Bean.FavouriteBean
import com.mredrock.cyxbs.freshman.Bean.MessageBean
import com.mredrock.cyxbs.freshman.ViewModel.favouriteObject

class CrateSongListFragment(val bean: FavouriteBean) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view = activity?.layoutInflater!!.inflate(R.layout.dialog_creat_song_list, null) as ViewGroup

        val builder = AlertDialog.Builder(context)

        return builder.setTitle("创建歌单").setView(view).setNegativeButton("创建") { dialog, which ->
            bean.list?.add(FavouriteBean.FavouriteItemBean().apply {
                url = getPic()
                title = "${(view.getChildAt(0) as EditText).text}"
                detail = "共1首"
                songList = ArrayList<MessageBean.ResultBean>()
                songList.add(MessageBean.ResultBean().apply {
                    url = com.mredrock.cyxbs.freshman.getUrl()
                    pic = com.mredrock.cyxbs.freshman.getPic()
                    title = com.mredrock.cyxbs.freshman.getTitle()
                    author = com.mredrock.cyxbs.freshman.getAuthor()
                })
            })
            favouriteObject?.put("favouriteBean", bean)
            favouriteObject?.saveInBackground()?.subscribe()
        }.create()
    }
}