package com.mredrock.cyxbs.freshman.ViewModel

import androidx.lifecycle.MutableLiveData
import com.mredrock.cyxbs.common.viewmodel.BaseViewModel
import com.mredrock.cyxbs.freshman.Bean.MessageBean
import com.mredrock.cyxbs.freshman.Model.Model
import com.mredrock.cyxbs.freshman.Model.ModelCallback

class SearchViewModel : BaseViewModel() {
    val data = MutableLiveData<MessageBean>()
    private val callback = object : ModelCallback {
        override fun onFailure() {

        }

        override fun onSuccess(bean: MessageBean?) {
            data.value = bean
        }

    }
    private val model = Model(callback)
    fun getBean(name: String) {
        model.getBean(name)
    }
}