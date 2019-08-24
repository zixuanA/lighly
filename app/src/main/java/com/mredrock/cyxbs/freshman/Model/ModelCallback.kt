package com.mredrock.cyxbs.freshman.Model

import com.mredrock.cyxbs.freshman.Bean.MessageBean

interface ModelCallback {
    fun onFailure()
    fun onSuccess(bean: MessageBean?)
}