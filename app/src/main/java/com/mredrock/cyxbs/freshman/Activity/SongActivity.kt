package com.mredrock.cyxbs.freshman.Activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.transition.ChangeBounds
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.mredrock.cyxbs.common.ui.BaseViewModelActivity
import com.mredrock.cyxbs.freshman.*
import com.mredrock.cyxbs.freshman.Bean.FavouriteBean
import com.mredrock.cyxbs.freshman.Bean.MessageBean
import com.mredrock.cyxbs.freshman.Event.*
import com.mredrock.cyxbs.freshman.View.MusicProcessView
import com.mredrock.cyxbs.freshman.ViewModel.SongViewModel
import com.mredrock.cyxbs.freshman.ViewModel.favouriteObject
import com.transitionseverywhere.ChangeText
import com.transitionseverywhere.Rotate
import kotlinx.android.synthetic.main.activity_song.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

class SongActivity : BaseViewModelActivity<SongViewModel>(), SbuscribeMusicEventAble {
    private var animator: ObjectAnimator? = null
    private var server: MusicService.MyBinder? = null
    private var bean: FavouriteBean? = null
    private val addSongListListener = object : AddSongListFragment.AddSongListListener {
        override fun addSongList() {
            bean?.let { it1 -> CrateSongListFragment(it1).show(supportFragmentManager, "CrateSongListFragment") }

        }

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
        Glide.with(this).load(R.drawable.ic_music_play_small).into(img_pause)
        pauseAnimation()
    }

    @Subscribe
    override fun onMusicReplayEvent(event: MusicReplayEvent) {
        Glide.with(this).load(R.drawable.ic_pause_big).into(img_pause)
        restartAnimation()
    }

    @Subscribe
    override fun onMusicChangedEvent(event: MusicChangeEvent) {
        initStates()
        restartAnimation()
        Glide.with(this).load(R.drawable.ic_collection).into(img_collection)

    }

    @Subscribe
    override fun onMusicProgressAddingEvent(event: MusicProgressAddingEvent) {
        if (!pointCanMove)
            mv_song.setOutPathDegree(event.getProgress())
    }

    @Subscribe
    override fun onMusicStartEvent(event: MusicStartEvent) {
    }

    private var pointCanMove = false
    override val viewModelClass: Class<SongViewModel> = SongViewModel::class.java
    override val isFragmentActivity: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)


        initTransition()
        initStates()
        if (getMusicState()) {
            initAnimation()
        }
        initService()
        initListener()
    }

    private fun initTransition() {
        window.transitionBackgroundFadeDuration = 600
        window.sharedElementEnterTransition = ChangeBounds().apply {
            interpolator = DecelerateInterpolator()
            duration = 500
        }
//        window.enterTransition = Slide().apply {
//            android.view.Gravity.BOTTOM
//            removeTarget(ll_song)}
//        window.sharedElementEnterTransition = Slide().apply { slideEdge = android.view.Gravity.BOTTOM }
//        window.sharedElementReturnTransition = null
    }

    private fun initStates() {
        bean = intent.extras?.getSerializable("bean") as FavouriteBean
        androidx.transition.TransitionManager.beginDelayedTransition(ll_song, ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN))
        Glide.with(this)
                .load(getPic()).apply(RequestOptions().circleCrop()).into(img_song_rotate)
        mv_song.setColor(intent.extras?.getInt("color", 0xffffff))
        sl_pause.setmBackGroundColor(intent.extras?.getInt("color", 0xffffff)!!)

        tv_song_name.text = com.mredrock.cyxbs.freshman.getTitle()
        tv_song_author.text = getAuthor()
        tv_song_album.text = intent.extras?.getString("title")
        if (getMusicState()) {

            Glide.with(this).load(R.drawable.ic_pause_big).into(img_pause)
        } else {
            Glide.with(this).load(R.drawable.ic_music_play_small).into(img_pause)
        }

    }

    private fun initListener() {
        mv_song.toucheventCallback {
            pointCanMove = it
        }
        mv_song.setOnProgressChangedListener(object : MusicProcessView.ProgressChangedListener {
            override fun onProgressChanged(percentage: Float) {
                EventBus.getDefault().post(MusicProgressChangedEvent(percentage))
            }

        })
        fl_back_song.setOnClickListener {
            finish()
        }
        sl_pause.setOnClickListener {
            if (getMusicState()) {
                EventBus.getDefault().post(MusicPauseEvent())
            } else {
                EventBus.getDefault().post(MusicReplayEvent())
            }
        }
        img_last_song.setOnClickListener {
            androidx.transition.TransitionManager.beginDelayedTransition(rl_song_control, Rotate())
            img_last_song.rotation = img_last_song.rotation + 360f

            EventBus.getDefault().post(NextSongEvent())
        }
        img_next_song.setOnClickListener {
            androidx.transition.TransitionManager.beginDelayedTransition(rl_song_control, Rotate())
            img_next_song.rotation = img_next_song.rotation + 360f

            EventBus.getDefault().post(NextSongEvent())
        }
        img_collection.setOnClickListener {
            Glide.with(this).load(R.drawable.ic_collection_choose).into(img_collection)
            bean.apply {
                if (this?.list == null)
                    this?.list = ArrayList<FavouriteBean.FavouriteItemBean>()
                if (this?.list!!.size == 0) {
                    this.list?.add(FavouriteBean.FavouriteItemBean().apply {
                        url = getPic()
                        title = "我喜欢的音乐"
                        detail = "共1首"
                        songList = ArrayList<MessageBean.ResultBean>()
                        songList.add(MessageBean.ResultBean().apply {
                            url = com.mredrock.cyxbs.freshman.getUrl()
                            pic = com.mredrock.cyxbs.freshman.getPic()
                            title = com.mredrock.cyxbs.freshman.getTitle()
                            author = com.mredrock.cyxbs.freshman.getAuthor()
                        })
                    })
                } else {
                    list[0].detail = "共${list[0].songList.size}首"
                    list[0].addSong(MessageBean.ResultBean().apply {
                        url = com.mredrock.cyxbs.freshman.getUrl()
                        pic = com.mredrock.cyxbs.freshman.getPic()
                        title = com.mredrock.cyxbs.freshman.getTitle()
                        author = com.mredrock.cyxbs.freshman.getAuthor()
                    })
                }
            }

            favouriteObject?.put("favouriteBean", bean)
            favouriteObject?.saveInBackground()?.subscribe()
        }
        img_random.setOnClickListener {
            val animatorx = ObjectAnimator.ofFloat(img_random, "scaleX", 1f, 1.5f, 1f)
            val animatory = ObjectAnimator.ofFloat(img_random, "scaleY", 1f, 1.5f, 1f)
            AnimatorSet().apply {
                playTogether(animatorx, animatory)
                duration = 300
                start()
            }
            EventBus.getDefault().post(RepeatModeChangeEvent(false))
        }
        img_loop.setOnClickListener {
            ObjectAnimator.ofFloat(img_loop, "rotation", 0f, 360f, 0f).apply {
                duration = 800
                start()
            }
            EventBus.getDefault().post(RepeatModeChangeEvent(true))
        }
        img_add_song_list.setOnClickListener {
            bean?.let { it1 -> AddSongListFragment(it1, addSongListListener).show(supportFragmentManager, "AddSongListFragment") }
        }
    }

    private fun initAnimation() {
        animator = ObjectAnimator.ofFloat(img_song_rotate, "rotation", 0f, 359f)
        mv_song.startAnimation()

        animator?.duration = 20000
        animator?.interpolator = LinearInterpolator()
        animator?.repeatCount = ObjectAnimator.INFINITE
        animator?.startDelay = 500
        animator?.start()
    }

    private fun pauseAnimation() {
        animator?.pause()
        mv_song.pauseAnimation()
    }

    private fun restartAnimation() {
        animator?.resume()
        mv_song.startAnimation()
    }

    override fun onDestroy() {
        animator?.cancel()
        animator = null
        mv_song.destroyAnimation()
        unbindService(conn)
        super.onDestroy()
    }

    override fun onBackPressed() {

        finish()
    }

    private fun initService() {
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, conn, Context.BIND_AUTO_CREATE)
    }


}
