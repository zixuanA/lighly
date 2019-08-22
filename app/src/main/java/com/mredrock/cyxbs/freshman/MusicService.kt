package com.mredrock.cyxbs.freshman

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.Binder
import android.os.IBinder
import android.os.SystemClock
import com.mredrock.cyxbs.common.utils.LogUtils
import com.mredrock.cyxbs.freshman.Bean.MessageBean
import com.mredrock.cyxbs.freshman.Event.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.concurrent.Executors
import kotlin.random.Random

private var musicPlaying = false
private var isRepeatMode = false
private var pic = ""
private var title = ""
private var author = ""
private var url = ""
private var position = 0
private var bean : MessageBean? = null
fun getPic():String{
    return pic
}
fun getTitle():String{
    return title
}
fun getAuthor():String{
    return author
}
fun getRepeatMode():Boolean= isRepeatMode
fun getUrl():String{
    return url
}
fun getPosition()= position
fun getMusicState()= musicPlaying
class MusicService : Service() {
    private var musicLoading = false
    private val threadPool = Executors.newFixedThreadPool(1)
    private val player = MediaPlayer()
    private val myBinder = MyBinder()
    private val runnable = Runnable {
        val event = getMusicProgressAddingEvent()
        while (musicPlaying && !musicLoading) {
            event.setProgress(player.currentPosition.toFloat()/player.duration)
            EventBus.getDefault().post(event)
        }
        SystemClock.sleep(500)
    }
    private fun playMusic(url:String){

        musicPlaying = true
        musicLoading = true
        player.stop()
        player.reset()
        LogUtils.d("eMyTag",url)
        player.setDataSource(url)
        player.prepareAsync()
        player.setOnPreparedListener {
            musicLoading = false
            player.start()

            EventBus.getDefault().post(MusicStartEvent())

            threadPool.execute(runnable)
        }
        player.isLooping = isRepeatMode
    }
    @Subscribe
    fun onNextsongEvent(event:NextSongEvent){
        if(bean!=null){
            var random = position
            while (random== position){
                random = Random.nextInt(0, bean!!.result.size-1)
            }
            EventBus.getDefault().post(MusicChangeEvent(bean!!,random))
        }
    }
    @Subscribe(priority = 1)
    fun onMusicPauseEvent(event:MusicPauseEvent){
        musicPlaying  = false

        player.pause()
    }
    @Subscribe(priority = 1)
    fun onMusicReplayEvent(event:MusicReplayEvent){
        musicPlaying = true
        threadPool.execute(runnable)
        player.start()
    }

    @Subscribe(priority = 100)
    fun onMusicChangedEvent(event:MusicChangeEvent){
        bean = event.music
        position = event.position
        playMusic(event.music.result[event.position].url)
        url = event.music.result[event.position].url
        pic = event.music.result[event.position].pic
        title = event.music.result[event.position].title
        author = event.music.result[event.position].author

    }
    @Subscribe(priority = 1)
    fun onMusicProgressChangedEvent(event:MusicProgressChangedEvent){
        player.seekTo((player.duration * event.progress).toInt())
    }
    @Subscribe(priority = 1)
    fun onRepeatModeChangeEvent(event:RepeatModeChanEvent){
        isRepeatMode = event.isRepeatMode
        player.isLooping = isRepeatMode
    }

    override fun onCreate() {
        EventBus.getDefault().register(this)

        super.onCreate()
        player.setOnCompletionListener {
            if(!isRepeatMode && bean!=null){
                EventBus.getDefault().post(MusicChangeEvent(bean!!,Random.nextInt(0, bean!!.result.size)))

            }
        }
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
