package com.mredrock.cyxbs.freshman.Event

private var event = MusicProgressAddingEvent()
fun getMusicProgressAddingEvent(): MusicProgressAddingEvent {
    return event
}

class MusicProgressAddingEvent {
    private var progress = 0f
    fun setProgress(progress: Float) {
        this.progress = progress
    }

    fun getProgress(): Float {
        return progress
    }
}