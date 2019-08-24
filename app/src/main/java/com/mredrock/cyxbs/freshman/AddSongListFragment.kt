package com.mredrock.cyxbs.freshman

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mredrock.cyxbs.common.utils.LogUtils
import com.mredrock.cyxbs.freshman.Adapter.AddSongListRecyclerViewAdapter
import com.mredrock.cyxbs.freshman.Bean.FavouriteBean
import com.mredrock.cyxbs.freshman.Bean.MessageBean
import com.mredrock.cyxbs.freshman.ViewModel.favouriteObject

class AddSongListFragment(val bean: FavouriteBean, private val addListener: AddSongListListener) : DialogFragment() {
    private var v: View? = null
    private val listener = object : AddSongListRecyclerViewAdapter.AddSongListRecyclerClickListener {
        override fun onAddSongListClick() {
            addListener.addSongList()
            dismiss()
        }

        override fun onSongListClick(position: Int) {
            bean.list[position].detail = "共${bean.list[0].songList.size}首"
            bean.list[position].addSong(MessageBean.ResultBean().apply {
                url = com.mredrock.cyxbs.freshman.getUrl()
                pic = com.mredrock.cyxbs.freshman.getPic()
                title = com.mredrock.cyxbs.freshman.getTitle()
                author = com.mredrock.cyxbs.freshman.getAuthor()
            })
            favouriteObject?.put("favouriteBean", bean)
            favouriteObject?.saveInBackground()?.subscribe()
            dismiss()
        }

    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity!!)
        v = activity!!.layoutInflater.inflate(R.layout.dialog_add_song_list, null)
        builder.setView(v)
//        builder?.setView(View.inflate(context,R.id.res_add_song_list,null))
//        initRecycler()

        initRecycler()
        return builder.create()!!
    }

    private fun initRecycler() {
        LogUtils.d("MyTag", "${(v as ViewGroup).childCount}")
        ((v as ViewGroup).getChildAt(0) as RecyclerView).apply {
            this.adapter = AddSongListRecyclerViewAdapter(bean, listener)
            this.layoutManager = LinearLayoutManager(context)
        }
//        v.res_add_song_list.adapter = AddSongListRecyclerViewAdapter(bean,listener)
//        res_add_song_list.layoutManager = LinearLayoutManager(context)
    }

    interface AddSongListListener {
        fun addSongList()
    }
}