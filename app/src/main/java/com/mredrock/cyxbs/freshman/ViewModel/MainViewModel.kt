package com.mredrock.cyxbs.freshman.ViewModel

import androidx.lifecycle.MutableLiveData
import cn.leancloud.AVObject
import cn.leancloud.AVQuery
import com.google.gson.Gson
import com.mredrock.cyxbs.common.BaseApp
import com.mredrock.cyxbs.common.utils.LogUtils
import com.mredrock.cyxbs.common.utils.extensions.defaultSharedPreferences
import com.mredrock.cyxbs.common.utils.extensions.editor
import com.mredrock.cyxbs.common.viewmodel.BaseViewModel
import com.mredrock.cyxbs.freshman.Bean.FavouriteBean
import com.mredrock.cyxbs.freshman.R
import io.reactivex.Observer
import io.reactivex.disposables.Disposable


var favouriteObject: AVObject? = null

class MainViewModel : BaseViewModel() {
    val data = MutableLiveData<FavouriteBean>()

    private val bean = FavouriteBean()
    fun getBean(): FavouriteBean {
        return bean
    }

    init {

        val query = AVQuery<AVObject>("AlbumList")
        query.findInBackground().subscribe(object : Observer<List<AVObject>> {
            override fun onComplete() {

            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: List<AVObject>) {
                val list = mutableListOf<FavouriteBean.AlbumBean>()
                for (it in t) {
                    val album = FavouriteBean.AlbumBean()
                    album.albumName = it.getString("name")
                    list.add(album)
                }
                LogUtils.d("MyTag", list[2].albumName)
                bean.albumList = list
                data.value = bean
            }

            override fun onError(e: Throwable) {
                toastEvent.value = R.string.network_failed
            }

        })

//    val a = AVQuery<AVObject>("AlbumClass")
//
//        a.getInBackground("5d5b4f07c05a800073b256ad").subscribe(object :Observer<AVObject>{
//            override fun onNext(t: AVObject) {
//                LogUtils.d("MyTag","${t.getJSONArray("bce")}")
//
//            }
//
//            override fun onComplete() {
//
//            }
//
//            override fun onSubscribe(d: Disposable) {
//            }
//
//
//
//            override fun onError(e: Throwable) {
//            }
//
//        })

        val objId = BaseApp.context.defaultSharedPreferences.getString("objectId", "")
        LogUtils.d("MyTag", objId)
        if (objId == "") {
            val obj = AVObject("FavouriteList")
            obj.saveInBackground().subscribe(object : Observer<AVObject> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: AVObject) {
                    favouriteObject = t
                    BaseApp.context.defaultSharedPreferences.editor {
                        putString("objectId", t.objectId)
                    }
                }

                override fun onError(e: Throwable) {
                }

            })
        } else {
            LogUtils.d("MyTag", "doing")
            val albumQuary = AVQuery<AVObject>("FavouriteList")
            albumQuary.getInBackground(objId).subscribe(object : Observer<AVObject> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: AVObject) {
//                    t.put("favouriteBean",FavouriteBean().apply { list = ArrayList<FavouriteBean.FavouriteItemBean>().apply { add(FavouriteBean.FavouriteItemBean().apply { title = "我喜欢的音乐" }) } })
                    //放入favoriteBean
//                    LogUtils.d("MyTag","doing")
                    favouriteObject = t
                    t.getJSONObject("favouriteBean") ?: return
                    val gson = Gson()
                    bean.list = gson.fromJson(t.getJSONObject("favouriteBean").toJSONString(), FavouriteBean::class.java).list
//                    LogUtils.d("MyTag","${bean.list[0].title}")
                    data.value = bean
                }

                override fun onError(e: Throwable) {
                    LogUtils.d("MyTag", "err")
                }

            })
        }
    }

    fun resreshData() {
        val objId = BaseApp.context.defaultSharedPreferences.getString("objectId", "")
        LogUtils.d("MyTag", objId)
        if (objId == "") {
            val obj = AVObject("FavouriteList")
            obj.saveInBackground().subscribe(object : Observer<AVObject> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: AVObject) {
                    favouriteObject = t
                    BaseApp.context.defaultSharedPreferences.editor {
                        putString("objectId", t.objectId)
                    }
                }

                override fun onError(e: Throwable) {
                }

            })
        } else {
            LogUtils.d("MyTag", "doing")
            val albumQuary = AVQuery<AVObject>("FavouriteList")
            albumQuary.getInBackground(objId).subscribe(object : Observer<AVObject> {
                override fun onComplete() {

                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(t: AVObject) {
//                    t.put("favouriteBean",FavouriteBean().apply { list = ArrayList<FavouriteBean.FavouriteItemBean>().apply { add(FavouriteBean.FavouriteItemBean().apply { title = "我喜欢的音乐" }) } })
                    //放入favoriteBean
//                    LogUtils.d("MyTag","doing")
                    favouriteObject = t
                    t.getJSONObject("favouriteBean") ?: return
                    val gson = Gson()
                    bean.list = gson.fromJson(t.getJSONObject("favouriteBean").toJSONString(), FavouriteBean::class.java).list
//                    LogUtils.d("MyTag","${bean.list[0].title}")
                    data.value = bean
                }

                override fun onError(e: Throwable) {
                    LogUtils.d("MyTag", "err")
                }

            })
        }
    }
}