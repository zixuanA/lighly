package com.mredrock.cyxbs.freshman.ViewModel

import androidx.lifecycle.MutableLiveData
import com.mredrock.cyxbs.common.viewmodel.BaseViewModel
import com.mredrock.cyxbs.freshman.Bean.MessageBean
import com.mredrock.cyxbs.freshman.Model.Model
import com.mredrock.cyxbs.freshman.Model.ModelCallback
import com.mredrock.cyxbs.freshman.R

class SongListViewModel : BaseViewModel() {
    val data = MutableLiveData<MessageBean>()
    private val model: Model = Model(object : ModelCallback {
        override fun onFailure() {
            toastEvent.value = R.string.network_failed
        }

        override fun onSuccess(bean: MessageBean?) {
            data.value = bean
        }

    })

    fun getBean(name: String) {
        model.getBean(name)
    }
}