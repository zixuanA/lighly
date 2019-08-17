package com.mredrock.cyxbs.freshman.Activity

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mredrock.cyxbs.common.ui.BaseViewModelActivity
import com.mredrock.cyxbs.freshman.Adapter.MainRecyclerViewAdapter
import com.mredrock.cyxbs.freshman.Bean.FavouriteBean
import com.mredrock.cyxbs.freshman.R
import com.mredrock.cyxbs.freshman.Util.PixelUtil
import com.mredrock.cyxbs.freshman.Util.dp2px
import com.mredrock.cyxbs.freshman.ViewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.recycler_item_head.view.*
import android.opengl.ETC1.getWidth
import android.opengl.ETC1.getHeight
import android.view.View
import android.view.ViewAnimationUtils
import androidx.core.app.ActivityOptionsCompat
import com.mredrock.cyxbs.freshman.Util.getAlbumIconRes
import org.jetbrains.anko.startActivity


class MainActivity : BaseViewModelActivity<MainViewModel>() {
    override val viewModelClass: Class<MainViewModel> = MainViewModel::class.java
    override val isFragmentActivity: Boolean = false
    val a = FavouriteBean()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val b = FavouriteBean.AlbumBean()
        b.albumName = "25"
        a.albumList = listOf<FavouriteBean.AlbumBean>(b,b,b,b,b,b,b,b)
        val c = FavouriteBean.FavouriteItemBean()
        c.url = "http://p2.music.126.net/ZRTknw6EXU6XsCbjN8x1og==/109951163984548453.jpg?param=300x300"
        c.detail = "detail"
        c.title = "like"
        a.list = listOf(c,c,c)
        res_main.adapter = MainRecyclerViewAdapter(this,a,object :MainRecyclerViewAdapter.OnMainRecyclerClick{
            override fun onItemClicked(position: Int) {

            }

            override fun onAlbumOptionsClicked(position: Int,x:Int,y:Int,color: Int) {
            }

            override fun onAlbumClicked(position: Int,x:Int,y:Int,color: Int) {
                val view = View.inflate(this@MainActivity,R.layout.placeholder,null)
                cl_main.addView(view)
                view.setBackgroundColor(color)
                val circularReveal = ViewAnimationUtils.createCircularReveal(view, x+ dp2px(this@MainActivity,80), y+ dp2px(this@MainActivity,90), 0f, 3000f)
//                mImage_bg.setBackgroundColor(Color.BLACK)
                circularReveal.duration = 600
                circularReveal.start()
                val bundle =  ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle()
                val intent = Intent(this@MainActivity,SongListActivity::class.java)
                intent.putExtra("color",color)
                intent.putExtra("title",a.albumList[position].albumName)
                intent.putExtra("resource", getAlbumIconRes(position))
                startActivity(intent,bundle)

                circularReveal.addListener(object:Animator.AnimatorListener{
                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {

                        cl_main.removeView(view)
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        cl_main.removeView(view)
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                })

            }

        })
        res_main.layoutManager = LinearLayoutManager(this)
//        val translation = ChangeBounds()
//        translation.duration = 500
//        TransitionManager.beginDelayedTransition(res_main,translation)
    }


}
