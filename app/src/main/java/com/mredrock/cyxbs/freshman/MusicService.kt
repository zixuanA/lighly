package com.mredrock.cyxbs.freshman

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.os.SystemClock
import com.mredrock.cyxbs.common.utils.LogUtils
import com.mredrock.cyxbs.freshman.Event.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.concurrent.Executors

private var musicPlaying = false
private var isRepeatMode = false
private var pic = ""
private var title = ""
private var author = ""
private var url = ""
fun getPic():String{
    return pic
}
fun getTitle():String{
    return title
}
fun getAuthor():String{
    return author
}
fun setRepeatMode(mode:Boolean){
    isRepeatMode = mode
}
fun getUrl():String{
    return url
}

fun getMusicState()= musicPlaying
class MusicService : Service() {
    private val threadPool = Executors.newFixedThreadPool(1)
    private val player = MediaPlayer()
    private val myBinder = MyBinder()
    private val runnable = Runnable {
        val event = getMusicProgressAddingEvent()
        while (musicPlaying) {
            event.setProgress(player.currentPosition.toFloat()/player.duration)
            EventBus.getDefault().post(event)
        }
        SystemClock.sleep(500)
    }
    private fun playMusic(url:String){

        musicPlaying = true

        player.reset()
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener {
            player.start()

            EventBus.getDefault().post(MusicStartEvent())
            threadPool.execute(runnable)
        }

    }

    @Subscribe
    fun onMusicPauseEvent(event:MusicPauseEvent){
        musicPlaying  = false
        player.pause()
    }
    @Subscribe
    fun onMusicReplayEvent(event:MusicReplayEvent){
        musicPlaying = true
        player.start()
    }

    @Subscribe(priority = 100)
    fun onMusicChangedEvent(event:MusicChangeEvent){

        playMusic(event.music.url)
        url = event.music.url
        pic = event.music.pic
        title = event.music.title
        author = event.music.author

    }
    @Subscribe
    fun onMusicProgressChangedEvent(event:MusicProgressChangedEvent){
        player.seekTo((player.duration * event.progress).toInt())
    }

    override fun onCreate() {
        EventBus.getDefault().register(this)
        super.onCreate()
    }

    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    override fun onBind(intent: Intent): IBinder {

        return myBinder
    }

    override fun onUnbind(intent: Intent?): Boolean {

        return super.onUnbind(intent)
    }
    class MyBinder: Binder() {

    }
}
