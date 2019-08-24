package com.mredrock.cyxbs.freshman.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mredrock.cyxbs.freshman.Bean.MessageBean
import com.mredrock.cyxbs.freshman.R
import kotlinx.android.synthetic.main.recycler_item_search.view.*

class SearchRecyclerViewAdapter(private val callback: OnSearchClick) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var messageBean: MessageBean? = null
    fun setBean(bean: MessageBean) {
        messageBean = bean
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_search, parent, false))
//        return MyViewHolder(View.inflate(parent.context, R.layout.recycler_item_search,null))
    }

    override fun getItemCount(): Int {
        messageBean?.result ?: return 0
        return messageBean?.result!!.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (messageBean != null) {
            holder.itemView.tv_search_item.text = messageBean!!.result[position].title
            holder.itemView.tv_search_item_detail.text = messageBean!!.result[position].author
            holder.itemView.setOnClickListener {
                callback.itemClick(position)
            }
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    interface OnSearchClick {
        fun itemClick(position: Int)
    }
}