package com.mredrock.cyxbs.freshman.Activity

import android.os.Bundle
import android.transition.*
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mredrock.cyxbs.common.ui.BaseViewModelActivity
import com.mredrock.cyxbs.common.utils.LogUtils
import com.mredrock.cyxbs.freshman.Adapter.SongRecyclerViewAdapter
import com.mredrock.cyxbs.freshman.Event.*
import com.mredrock.cyxbs.freshman.R
import com.mredrock.cyxbs.freshman.ViewModel.SongListViewModel
import com.mredrock.cyxbs.freshman.getMusicState
import com.mredrock.cyxbs.freshman.getPic
import com.mredrock.cyxbs.freshman.getUrl
import kotlinx.android.synthetic.main.activity_songlist.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.backgroundColor

class SongListActivity : BaseViewModelActivity<SongListViewModel>() ,SbuscribeMusicEventAble{
    @Subscribe
    override fun onMusicPauseEvent(event: MusicPauseEvent) {

    }
    @Subscribe
    override fun onMusicReplayEvent(event: MusicReplayEvent) {
    }
    @Subscribe
    override fun onMusicChangedEvent(event: MusicChangeEvent) {
        setPlayingTab()
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    override fun onMusicProgressAddingEvent(event: MusicProgressAddingEvent) {
        smp_music_playing.setDegree(event.getProgress())
    }
    @Subscribe
    override fun onMusicStartEvent(event: MusicStartEvent) {
    }


    override val viewModelClass: Class<SongListViewModel> = SongListViewModel::class.java
    override val isFragmentActivity: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songlist)

        initAnimation()
        setPlayingTab()
        fl_back_song_list.setOnClickListener {
            finishAfterTransition()
        }
        initRecyclerView()

        if(intent.extras?.getString("res")==null){
            viewModel.getBean(intent.extras?.getString("title")!!)
        }else{
            TODO()
        }
       addTabClickListener()

    }
    private fun addTabClickListener(){
        cl_song_playing.setOnClickListener {
            startActivity<SongActivity>()
        }
        fl_tab_state.setOnClickListener {
            if(getMusicState())
                EventBus.getDefault().post(getMusicPauseEvent())
            else
                EventBus.getDefault().post(getMusicStartEvent())
        }
    }
    private fun setPlayingTab(){
        LogUtils.d("MyTag","${getMusicState()}")
        if(getUrl() != ""){
            cl_song_playing.visibility = View.VISIBLE
            tv_music_playing_author.text = com.mredrock.cyxbs.freshman.getAuthor()
            tv_music_playing_name.text = com.mredrock.cyxbs.freshman.getTitle()
            Glide.with(this).load(getPic()).into(img_music_playing)
            if(getMusicState()){
                Glide.with(this).load(R.drawable.ic_pause_small).into(img_music_playing_state)
            }else{
                Glide.with(this).load(R.drawable.ic_music_play_small).into(img_music_playing_state)
            }
        }
        else
            cl_song_playing.visibility = View.GONE
    }
    private fun initRecyclerView(){
        rv_song_list.layoutManager =  LinearLayoutManager(this)
        viewModel.data.observe(this, Observer {
            if(rv_song_list.adapter == null){
                rv_song_list.adapter = SongRecyclerViewAdapter(it)
                (rv_song_list.adapter as SongRecyclerViewAdapter).setOnSongClickListener(object :SongRecyclerViewAdapter.OnSongClickListener{
                    override fun onclick(position: Int) {
                        EventBus.getDefault().post(MusicChangeEvent(it.result[position]))
                    }

                })
            }else{

                (rv_song_list.adapter as SongRecyclerViewAdapter).setBean(it)
            }
        })
        rv_song_list.itemAnimator = DefaultItemAnimator()
        rv_song_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var scrollY = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val manager = recyclerView.layoutManager as LinearLayoutManager
                if(dy>0 && ll_song_list_tool.visibility != View.GONE){
                    TransitionManager.beginDelayedTransition(ll_song_list)
                    ll_song_list_tool.visibility = View.GONE

                    val params = ll_song_list_detail.layoutParams as LinearLayout.LayoutParams
                    params.bottomMargin = 0
                    ll_song_list_detail.layoutParams = params
                }
                scrollY+=dy
            }
        })

    }

    private fun initAnimation() {
        window.transitionBackgroundFadeDuration = 600
        ll_song_list.backgroundColor = intent.extras?.getInt("color") as Int
        tv_song_list_album.text = intent.extras?.getString("title")
        img_song_list_album_icon.setImageResource(intent.extras?.getInt("resource")!!)
        val slide = Slide()
        slide.removeTarget(ll_song_list_detail)
        slide.removeTarget(ll_song_list_tool)
        val fadeEnter = Fade()
        fadeEnter.removeTarget(ll_song_list_recycler)

        window.enterTransition = android.transition.TransitionSet().addTransition(slide).addTransition(fadeEnter).clone()
        val fade = Fade()
        window.returnTransition = fade


    }

    private var rawX = 0f
    private var rawY = 0f
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            rawX = event.x
            rawY = event.y
        }

        if (event?.action == MotionEvent.ACTION_MOVE) {
            if (event.y - rawY > -100) {
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
