package com.mredrock.cyxbs.freshman.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mredrock.cyxbs.freshman.Bean.FavouriteBean
import com.mredrock.cyxbs.freshman.R
import kotlinx.android.synthetic.main.dialog_add_song_list_item.view.*

class AddSongListRecyclerViewAdapter(private val favouriteBean: FavouriteBean, private val listener: AddSongListRecyclerClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var context: Context? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        context = parent.context
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.dialog_add_song_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        favouriteBean.list ?: return 1
        return favouriteBean.list.size + 1
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (position == 0) {
            holder.itemView.img_dialog_add_song_list.setImageResource(R.drawable.ic_add)
            holder.itemView.tv_dialog_add_song_list_title.text = "新建歌单"
            holder.itemView.setOnClickListener {
                listener.onAddSongListClick()
            }
        } else {
            context?.let { Glide.with(it).load(favouriteBean.list[position - 1].url).into(holder.itemView.img_dialog_add_song_list) }
            holder.itemView.tv_dialog_add_song_list_title.text = favouriteBean.list[position - 1].title
            holder.itemView.tv_dialog_add_song_list_detail.text = "共${favouriteBean.list[position - 1].songList.size}首"
            holder.itemView.setOnClickListener {
                listener.onSongListClick(position - 1)
            }
        }

    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    interface AddSongListRecyclerClickListener {
        fun onAddSongListClick()
        fun onSongListClick(position: Int)
    }
}