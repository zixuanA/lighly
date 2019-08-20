package com.mredrock.cyxbs.freshman.Activity

import com.mredrock.cyxbs.freshman.Event.*
import org.greenrobot.eventbus.Subscribe

interface SbuscribeMusicEventAble {

    @Subscribe
    fun onMusicPauseEvent(event: MusicPauseEvent)
    @Subscribe
    fun onMusicReplayEvent(event: MusicReplayEvent)
    @Subscribe
    fun onMusicChangedEvent(event: MusicChangeEvent)
    @Subscribe
    fun onMusicProgressAddingEvent(event: MusicProgressAddingEvent)
    @Subscribe
    fun onMusicStartEvent(event:MusicStartEvent)
}