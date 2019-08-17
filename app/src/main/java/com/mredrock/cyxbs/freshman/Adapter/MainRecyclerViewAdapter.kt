package com.mredrock.cyxbs.freshman.Adapter

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnticipateOvershootInterpolator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mredrock.cyxbs.common.utils.LogUtils
import com.mredrock.cyxbs.freshman.Bean.FavouriteBean
import com.mredrock.cyxbs.freshman.R
import com.mredrock.cyxbs.freshman.Util.dp2px
import com.mredrock.cyxbs.freshman.View.ShadowLayout
import kotlinx.android.synthetic.main.recycler_item_favourite_item.view.*
import kotlinx.android.synthetic.main.recycler_item_head.view.*
import org.jetbrains.anko.firstChild



class MainRecyclerViewAdapter(val context:Context, private val favouriteBean: FavouriteBean,val click: OnMainRecyclerClick) :RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType == 0){
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_head,parent,false)
            HeadViewHolder(view)
        }else{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_favourite_item,parent,false)
            ItemViewHolder(view)
        }
    }

    override fun getItemCount(): Int {
        return favouriteBean.list.size+1
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is HeadViewHolder -> {
                holder.itemView.tv_welcome.text = "Welcome"
                holder.itemView.res_main_album.adapter = AlbumRecyclerViewAdapter(favouriteBean,object :AlbumRecyclerViewAdapter.OnAlbumClickListener{
                    override fun onItemClick(count: Int,x:Int,y:Int,color: Int) {
                        click.onAlbumClicked(count,x,y,color)

                    }

                    override fun onOptionsClick(count: Int,x:Int,y:Int,color: Int) {
                        click.onAlbumOptionsClicked(count,x,y,color)
                    }

                })
                val layoutManager = object :LinearLayoutManager(context){

                }
                layoutManager.orientation = LinearLayoutManager.HORIZONTAL
                holder.itemView.res_main_album.layoutManager = layoutManager
                holder.itemView.res_main_album.addOnScrollListener(onScrollLister)
                setMaxFlingVelocity(holder.itemView.res_main_album,2000)
                holder.itemView.tv_favourite.text = "Faourite"
            }
            is ItemViewHolder ->{
                Glide.with(holder.itemView.context).load(favouriteBean.list[position-1].url).into(holder.itemView.img_circle)
                holder.itemView.tv_main_favourite_title.text = favouriteBean.list[position-1].title
                holder.itemView.tv_main_favourite_detail.text = favouriteBean.list[position-1].detail
                holder.itemView.setOnClickListener {
                    click.onItemClicked(position)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(position == 0){
            0
        }else{
            1
        }
    }


    class HeadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    }
    class ItemViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    private val onScrollLister = object :RecyclerView.OnScrollListener() {
        private var consumeX = 0
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

            val index = consumeX%(dp2px(context,214f))
            LogUtils.d("MyTag","consumex=${consumeX+dx},chi=${recyclerView.layoutManager!!.childCount*(dp2px(context,214))}")
            val manager = recyclerView.layoutManager as LinearLayoutManager
            if(manager.findLastCompletelyVisibleItemPosition() == manager.findLastVisibleItemPosition()){
                doCloseAnimation(recyclerView.getChildAt(0))
                doOpenAnimation(recyclerView.getChildAt(1))
            }

            if((index<0.5*(dp2px(context,214f))&&(index+dx)>=0.5*(dp2px(context,214f)))||(index>0.5*(dp2px(context,214f))&&(index+dx)<=0.5*(dp2px(context,214f)))){
                LogUtils.d("MyTag","!$dx,")
                val view = recyclerView.getChildAt(1)
                doOpenAnimation(view)
                doCloseAnimation(recyclerView.getChildAt(0))
                doCloseAnimation(recyclerView.getChildAt(2))
            }
            if(manager.findFirstCompletelyVisibleItemPosition() == 0){
                val view = recyclerView.getChildAt(0)
                doOpenAnimation(view)
                val view1 = recyclerView.getChildAt(1)
                doCloseAnimation(view1)
            }
            consumeX+=dx
        }
        fun doOpenAnimation(view:View){
           if(view.layoutParams.height != dp2px(context,210)){
               val shadowLayout = view as ShadowLayout
               shadowLayout.setBottomShow(true)
               val layoutParams = view.layoutParams as RecyclerView.LayoutParams


//                layoutParams.height = dp2px(context,220)
               val animator1 = ValueAnimator.ofInt(180,210)
               animator1.interpolator = AnticipateOvershootInterpolator()
               animator1.addUpdateListener {
                   layoutParams.height = dp2px(context,it.animatedValue as Int)
                   layoutParams.topMargin = dp2px(context,220-(it.animatedValue as Int))
                   view.layoutParams = layoutParams
               }
               animator1.start()
           }
        }
        fun doCloseAnimation(view:View){
            if(view.layoutParams.height != dp2px(context,180)){
                val shadowLayout = view as ShadowLayout
                shadowLayout.setBottomShow(false)
                val layoutParams = view.layoutParams as RecyclerView.LayoutParams


//                layoutParams.height = dp2px(context,220)
                val animator1 = ValueAnimator.ofInt(210,180)
                animator1.interpolator = AnticipateOvershootInterpolator()
                animator1.addUpdateListener {
                    layoutParams.height = dp2px(context,it.animatedValue as Int)
                    layoutParams.topMargin = dp2px(context,210-(it.animatedValue as Int))
                    view.layoutParams = layoutParams
                }
                animator1.start()
            }
        }
    }
    fun setMaxFlingVelocity(recyclerView:RecyclerView,v:Int){
        val field = recyclerView.javaClass.getDeclaredField("mMaxFlingVelocity")
        field.isAccessible = true
        field.set(recyclerView,v)
    }
    interface OnMainRecyclerClick{
        fun onItemClicked(position: Int)
        fun onAlbumOptionsClicked(position: Int,x:Int,y:Int,color: Int)
        fun onAlbumClicked(position: Int,x:Int,y:Int,color: Int)
    }
}