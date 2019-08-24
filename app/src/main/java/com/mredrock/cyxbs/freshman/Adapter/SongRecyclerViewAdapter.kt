package com.mredrock.cyxbs.freshman.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mredrock.cyxbs.common.utils.LogUtils
import com.mredrock.cyxbs.freshman.Bean.MessageBean
import com.mredrock.cyxbs.freshman.R
import com.mredrock.cyxbs.freshman.Util.dp2px
import kotlinx.android.synthetic.main.recycler_item_song.view.*

class SongRecyclerViewAdapter(private var messageBean: MessageBean) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var context: Context? = null
    private var listener: OnSongClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_song, parent, false)
        context = parent.context
        return MyViewHolder(view)
    }

    fun setBean(bean: MessageBean) {
        messageBean = bean
        notifyDataSetChanged()
    }


    override fun getItemCount(): Int = messageBean.result?.size?.plus(1) ?: 1

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (position != itemCount - 1) {
            holder.itemView.visibility = View.VISIBLE
            if (position == 0) {
                (holder.itemView.layoutParams as RecyclerView.LayoutParams).apply { topMargin = dp2px(context!!, 45) }
            } else {
                (holder.itemView.layoutParams as RecyclerView.LayoutParams).apply { topMargin = dp2px(context!!, 10) }
            }

//            holder.itemView.img_recycler_item_song_music_playing.visibility
            LogUtils.d("MyTag", messageBean.result[position].pic)
            Glide.with(context!!).load(messageBean.result[position].pic).apply(RequestOptions().circleCrop()).into(holder.itemView.img_recycler_item_song_music_img)
            holder.itemView.tv_recycler_item_song_music_name.text = messageBean.result[position].title
            holder.itemView.tv_recycler_item_song_music_author.text = messageBean.result[position].author
            holder.itemView.setOnClickListener {
                listener?.onclick(position)
            }
        } else {
            holder.itemView.visibility = View.INVISIBLE
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    interface OnSongClickListener {
        fun onclick(position: Int)
    }

    fun setOnSongClickListener(listener: OnSongClickListener) {
        this.listener = listener
    }
}