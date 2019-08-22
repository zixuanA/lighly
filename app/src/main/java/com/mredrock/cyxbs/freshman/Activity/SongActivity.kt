package com.mredrock.cyxbs.freshman.Activity

import android.animation.ObjectAnimator
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.os.PersistableBundle
import android.transition.ChangeBounds
import android.transition.Fade
import android.transition.Slide
import android.transition.TransitionManager
import android.view.View
import android.view.WindowManager
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import com.bumptech.glide.Glide
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.request.RequestOptions
import com.mredrock.cyxbs.common.ui.BaseViewModelActivity
import com.mredrock.cyxbs.common.utils.LogUtils
import com.mredrock.cyxbs.freshman.*
import com.mredrock.cyxbs.freshman.Event.*
import com.mredrock.cyxbs.freshman.View.MusicProcessView
import com.mredrock.cyxbs.freshman.ViewModel.SongViewModel
import com.transitionseverywhere.ChangeText
import com.transitionseverywhere.Rotate
import kotlinx.android.synthetic.main.activity_song.*
import kotlinx.android.synthetic.main.activity_songlist.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.backgroundColor
import com.mredrock.cyxbs.freshman.Util.MyAppGlideModel

class SongActivity : BaseViewModelActivity<SongViewModel>() ,SbuscribeMusicEventAble{
    private var animator:ObjectAnimator? = null
    private var server: MusicService.MyBinder?=null
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
    }
    @Subscribe
    override fun onMusicProgressAddingEvent(event: MusicProgressAddingEvent) {
        if(!pointCanMove)
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
        if(getMusicState()){
            initAnimation()
        }
        initService()
        initListener()
    }
    private fun initTransition(){
        window.sharedElementEnterTransition = ChangeBounds().apply { interpolator = DecelerateInterpolator()
            duration = 500}
//        window.sharedElementEnterTransition = Slide().apply { slideEdge = android.view.Gravity.BOTTOM }
//        window.sharedElementReturnTransition = null
    }

    private fun initStates(){
        androidx.transition.TransitionManager.beginDelayedTransition(ll_song,ChangeText().setChangeBehavior(ChangeText.CHANGE_BEHAVIOR_OUT_IN))
        Glide.with(this)
                .load(getPic()).into(img_song_rotate)
        mv_song.setColor(intent.extras?.getInt("color",0xffffff))
        sl_pause.setmBackGroundColor(intent.extras?.getInt("color",0xffffff)!!)

        tv_song_name.text = com.mredrock.cyxbs.freshman.getTitle()
        tv_song_author.text = getAuthor()
        tv_song_album.text = intent.extras?.getString("title")
        if(getMusicState()){

            Glide.with(this).load(R.drawable.ic_pause_big).into(img_pause)
        }else{
            Glide.with(this).load(R.drawable.ic_music_play_small).into(img_pause)
        }

    }
    private fun initListener(){
        mv_song.toucheventCallback {
            pointCanMove = it
        }
        mv_song.setOnProgressChangedListener(object : MusicProcessView.ProgressChangedListener{
            override fun onProgressChanged(percentage: Float) {
                EventBus.getDefault().post(MusicProgressChangedEvent(percentage))
            }

        })
        fl_back_song.setOnClickListener {
            finish()
        }
        sl_pause.setOnClickListener {
            if(getMusicState()){
                EventBus.getDefault().post(MusicPauseEvent())
            }else{
                EventBus.getDefault().post(MusicReplayEvent())
            }
        }
        img_last_song.setOnClickListener {
            androidx.transition.TransitionManager.beginDelayedTransition(rl_song_control,Rotate())
            img_last_song.rotation = img_last_song.rotation+360f

            EventBus.getDefault().post(NextSongEvent())
        }
        img_next_song.setOnClickListener {
            androidx.transition.TransitionManager.beginDelayedTransition(rl_song_control,Rotate())
            img_next_song.rotation = img_next_song.rotation +360f

            EventBus.getDefault().post(NextSongEvent())
        }
    }
    private fun initAnimation(){
        animator = ObjectAnimator.ofFloat(img_song_rotate,"rotation",0f,359f)
        mv_song.startAnimation()

        animator?.duration = 20000
        animator?.interpolator = LinearInterpolator()
        animator?.repeatCount = ObjectAnimator.INFINITE
        animator?.startDelay = 500
        animator?.start()
    }
    private fun pauseAnimation(){
        animator?.pause()
        mv_song.pauseAnimation()
    }
    private fun restartAnimation(){
        animator?.resume()
        mv_song.startAnimation()
    }

    override fun onDestroy() {
        animator?.cancel()
        animator = null
        mv_song.destroyAnimation()

        super.onDestroy()
    }

    override fun onBackPressed() {

        finish()
    }
    fun initService(){
        val intent = Intent(this, MusicService::class.java)
        bindService(intent,object : ServiceConnection {
            override fun onServiceDisconnected(name: ComponentName?) {
                server = null
            }

            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                server = service as MusicService.MyBinder
            }

        }, Context.BIND_AUTO_CREATE)
    }
}
