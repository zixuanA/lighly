package com.mredrock.cyxbs.freshman.Adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mredrock.cyxbs.common.utils.LogUtils
import com.mredrock.cyxbs.freshman.Bean.FavouriteBean
import com.mredrock.cyxbs.freshman.R
import com.mredrock.cyxbs.freshman.Util.dp2px
import com.mredrock.cyxbs.freshman.Util.getAlbumColor
import com.mredrock.cyxbs.freshman.Util.getAlbumIconRes
import com.mredrock.cyxbs.freshman.View.ShadowLayout
import kotlinx.android.synthetic.main.recycler_item_album.view.*
import kotlinx.android.synthetic.main.recycler_item_favourite_item.view.*
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.imageResource

class AlbumRecyclerViewAdapter (private val favouriteBean: FavouriteBean,private val clickListener: OnAlbumClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var context : Context? = null
    private var isFirstLoad = true
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflate = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_album,parent, false)
        context = parent.context
        return AlbumViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return favouriteBean.albumList.size +1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setIsRecyclable(false)
       if(position != itemCount-1){
           holder.itemView.visibility = View.VISIBLE
           holder.itemView.tv_album_name.text = favouriteBean.albumList[position].albumName
           holder.itemView.img_album_icon.imageResource =  getAlbumIconRes(position)

           val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
           params.height = dp2px(context!!,180)
           params.topMargin = dp2px(context!!,45)
           holder.itemView.layoutParams = params
           if(position == 0&&isFirstLoad){
               isFirstLoad=!isFirstLoad
               val lp = holder.itemView.layoutParams as RecyclerView.LayoutParams
               lp.height = dp2px(context!!,210)
               lp.topMargin = dp2px(context!!,10)
               holder.itemView.layoutParams = lp
           }


           holder.itemView.img_album_options.setOnClickListener {
               val intArray = IntArray(2)
               it.getLocationInWindow(intArray)
               val view = holder.itemView as ShadowLayout

               clickListener.onOptionsClick(position,intArray[0],intArray[1],view.backGroundColor)

           }
           holder.itemView.setOnClickListener {
               val intArray = IntArray(2)
               it.getLocationInWindow(intArray)
               val view = holder.itemView as ShadowLayout

               clickListener.onItemClick(position,intArray[0],intArray[1],view.backGroundColor)

           }
       }else{
           holder.itemView.visibility = View.INVISIBLE
       }

    }

    class AlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    interface OnAlbumClickListener{
        fun onItemClick(count:Int,x:Int,y:Int,color: Int)
        fun onOptionsClick(count:Int,x:Int,y:Int,color: Int)
    }
}