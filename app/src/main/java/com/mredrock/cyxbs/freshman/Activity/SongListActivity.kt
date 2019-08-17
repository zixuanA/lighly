package com.mredrock.cyxbs.freshman.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.transition.*
import android.view.GestureDetector
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.transition.ChangeBounds
import androidx.transition.Transition
import androidx.transition.TransitionSet
import com.mredrock.cyxbs.common.ui.BaseViewModelActivity
import com.mredrock.cyxbs.common.utils.LogUtils
import com.mredrock.cyxbs.freshman.R
import com.mredrock.cyxbs.freshman.ViewModel.SongListViewModel
import com.transitionseverywhere.extra.Scale
import kotlinx.android.synthetic.main.activity_songlist.*
import org.jetbrains.anko.backgroundColor

class SongListActivity : BaseViewModelActivity<SongListViewModel>() {


    override val viewModelClass: Class<SongListViewModel> = SongListViewModel::class.java
    override val isFragmentActivity: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songlist)

        initAnimation()
        fl_back_song_list.setOnClickListener {
            finishAfterTransition()
        }

    }
    private fun initAnimation(){
        window.transitionBackgroundFadeDuration = 600
        ll_song_list.backgroundColor = intent.extras?.getInt("color") as Int
        tv_song_list_album.text = intent.extras?.getString("title")
        img_song_list_album_icon.setImageResource(intent.extras?.getInt("resource")!!)
        val slide = Slide()
        slide.removeTarget(ll_song_list_detail)
        slide.removeTarget(ll_song_list_tool)
        window.enterTransition = slide
        val fade = Fade()
        window.returnTransition = fade


    }
    private var rawX = 0f
    private var rawY = 0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(event?.action == MotionEvent.ACTION_DOWN){
            rawX = event.x
            rawY = event.y
        }

        if(event?.action == MotionEvent.ACTION_MOVE){
            if(event.y - rawY > -100 ){
                TransitionManager.beginDelayedTransition(ll_song_list)
                ll_song_list_tool.visibility = View.GONE
                val params = ll_song_list_detail.layoutParams as LinearLayout.LayoutParams
                params.bottomMargin = 0
                ll_song_list_detail.layoutParams = params
                return true
            }
        }
        return false
    }
}
