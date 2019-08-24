package com.mredrock.cyxbs.freshman.Activity

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.transition.ChangeBounds
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionManager
import android.view.MotionEvent
import android.view.View
import android.widget.LinearLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mredrock.cyxbs.common.ui.BaseViewModelActivity
import com.mredrock.cyxbs.freshman.*
import com.mredrock.cyxbs.freshman.Adapter.SongRecyclerViewAdapter
import com.mredrock.cyxbs.freshman.Bean.FavouriteBean
import com.mredrock.cyxbs.freshman.Bean.MessageBean
import com.mredrock.cyxbs.freshman.Event.*
import com.mredrock.cyxbs.freshman.ViewModel.SongListViewModel
import kotlinx.android.synthetic.main.activity_songlist.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.jetbrains.anko.backgroundColor

class SongListActivity : BaseViewModelActivity<SongListViewModel>(), SbuscribeMusicEventAble {
    private var server: MusicService.MyBinder? = null
    private val layoutManager = LinearLayoutManager(this).apply {
        isSmoothScrollbarEnabled = false
    }
    private val conn = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            server = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            server = service as MusicService.MyBinder
        }

    }

    @Subscribe
    override fun onMusicPauseEvent(event: MusicPauseEvent) {
        TransitionManager.beginDelayedTransition(fl_tab_state)
        Glide.with(this).load(R.drawable.ic_music_play_small).into(img_music_playing_state)
    }

    @Subscribe
    override fun onMusicReplayEvent(event: MusicReplayEvent) {
        TransitionManager.beginDelayedTransition(fl_tab_state)
        Glide.with(this).load(R.drawable.ic_pause_small).into(img_music_playing_state)
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

        initService()
        initAnimation()
        setPlayingTab()
        fl_back_song_list.setOnClickListener {
            finishAfterTransition()
        }
        fl_search_song_list.setOnClickListener {
            //            startActivity<SearchActivity>("color" to intent.extras?.getInt("color"))
            startActivity(Intent(this, SearchActivity::class.java).apply {
                putExtra("color", intent.extras?.getInt("color"))
                putExtra("bean", intent.extras?.getSerializable("bean"))
            },
                    ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair.create(fl_search_song_list as View, "fl_search"), Pair.create(ll_song_list_recycler as View, "ll_recycler")).toBundle())
        }
        initRecyclerView()

        if (intent.extras?.getInt("position", -1) == -1) {

            viewModel.getBean(intent.extras?.getString("title")!!)
        } else {
            dataRefresh(MessageBean().apply { result = (intent.extras?.getSerializable("bean") as FavouriteBean).list[intent.extras?.getInt("position", 0)!!].songList })
        }
        addTabClickListener()

    }

    private fun addTabClickListener() {
        cl_song_playing.setOnClickListener {

            startActivity(Intent(this, SongActivity::class.java).apply {
                putExtra("color", intent.extras?.getInt("color"))
                putExtra("title", intent.extras?.getString("title"))
                putExtra("bean", intent.extras?.getSerializable("bean"))
            }
                    , ActivityOptionsCompat.makeSceneTransitionAnimation(this, Pair(img_music_playing, "shared_pic")).toBundle())
//            startActivity<SongActivity>("color" to intent.extras?.getInt("color"),"title" to intent.extras?.getString("title"))
        }
        fl_tab_state.setOnClickListener {

            if (getMusicState())
                EventBus.getDefault().post(getMusicPauseEvent())
            else
                EventBus.getDefault().post(MusicReplayEvent())

        }
    }

    private fun setPlayingTab() {
        TransitionManager.beginDelayedTransition(fl_song_list, Slide().apply { slideEdge = android.view.Gravity.BOTTOM })
        if (getUrl() != "") {
            cl_song_playing.visibility = View.VISIBLE
            tv_music_playing_author.text = getAuthor()
            tv_music_playing_name.text = com.mredrock.cyxbs.freshman.getTitle()
            Glide.with(this).load(getPic()).apply(RequestOptions().circleCrop()).into(img_music_playing)
            if (getMusicState()) {
                Glide.with(this).load(R.drawable.ic_pause_small).into(img_music_playing_state)
            } else {
                Glide.with(this).load(R.drawable.ic_music_play_small).into(img_music_playing_state)
            }
        } else
            cl_song_playing.visibility = View.GONE
    }

    private fun initRecyclerView() {

        rv_song_list.layoutManager = layoutManager
        viewModel.data.observe(this, Observer {

            dataRefresh(it)
        })


        rv_song_list.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            private var scrollY = 0
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val manager = recyclerView.layoutManager as LinearLayoutManager
                if (dy > 0 && ll_song_list_tool.visibility != View.GONE) {
                    rv_song_list.stopScroll()
                    TransitionManager.beginDelayedTransition(ll_song_list, ChangeBounds().apply { duration = 100 })
                    ll_song_list_tool.visibility = View.GONE

                    val params = ll_song_list_detail.layoutParams as LinearLayout.LayoutParams
                    params.bottomMargin = 0
                    ll_song_list_detail.layoutParams = params
                }
                scrollY += dy
            }
        })

    }

    private fun dataRefresh(message: MessageBean) {
        if (rv_song_list.adapter == null) {
            rv_song_list.adapter = SongRecyclerViewAdapter(message)
            (rv_song_list.adapter as SongRecyclerViewAdapter).setOnSongClickListener(object : SongRecyclerViewAdapter.OnSongClickListener {
                override fun onclick(position: Int) {
                    EventBus.getDefault().post(MusicChangeEvent(message, position))
                }

            })
        } else {

            (rv_song_list.adapter as SongRecyclerViewAdapter).setBean(message)
        }
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
            if (event.y - rawY > -10) {
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

    private fun initService() {
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, conn, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(conn)
    }
}
