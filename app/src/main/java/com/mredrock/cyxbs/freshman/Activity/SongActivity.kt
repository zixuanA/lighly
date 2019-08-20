package com.mredrock.cyxbs.freshman.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mredrock.cyxbs.common.ui.BaseViewModelActivity
import com.mredrock.cyxbs.freshman.Event.*
import com.mredrock.cyxbs.freshman.R
import com.mredrock.cyxbs.freshman.ViewModel.SongViewModel

class SongActivity : BaseViewModelActivity<SongViewModel>() ,SbuscribeMusicEventAble{
    override fun onMusicPauseEvent(event: MusicPauseEvent) {

    }

    override fun onMusicReplayEvent(event: MusicReplayEvent) {
    }

    override fun onMusicChangedEvent(event: MusicChangeEvent) {
    }

    override fun onMusicProgressAddingEvent(event: MusicProgressAddingEvent) {
    }

    override fun onMusicStartEvent(event: MusicStartEvent) {
    }

    override val viewModelClass: Class<SongViewModel> = SongViewModel::class.java
    override val isFragmentActivity: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_song)


    }
}
