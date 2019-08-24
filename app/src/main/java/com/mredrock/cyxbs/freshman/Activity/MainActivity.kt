package com.mredrock.cyxbs.freshman.Activity

import android.animation.Animator
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.view.ViewAnimationUtils
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.mredrock.cyxbs.common.ui.BaseViewModelActivity
import com.mredrock.cyxbs.common.utils.LogUtils
import com.mredrock.cyxbs.freshman.Adapter.MainRecyclerViewAdapter
import com.mredrock.cyxbs.freshman.MusicService
import com.mredrock.cyxbs.freshman.R
import com.mredrock.cyxbs.freshman.Util.dp2px
import com.mredrock.cyxbs.freshman.Util.getAlbumColor
import com.mredrock.cyxbs.freshman.Util.getAlbumIconRes
import com.mredrock.cyxbs.freshman.ViewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseViewModelActivity<MainViewModel>() {
    override val viewModelClass: Class<MainViewModel> = MainViewModel::class.java
    override val isFragmentActivity: Boolean = false
    private val conn = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            server = null
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            server = service as MusicService.MyBinder
        }

    }
    private var server: MusicService.MyBinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


//        val obj = AVObject("AlbumList")
//        obj.put("name","")
//
//        obj.saveInBackground().subscribe()

        viewModel.data.observe(this, Observer {
            (res_main.adapter as MainRecyclerViewAdapter).setBean(it)
        })
        initRecycler()
        initService()
    }

    override fun onStart() {
        super.onStart()
        LogUtils.d("eMyTag", "start")
    }

    override fun onStop() {
        LogUtils.d("eMyTag", "stop")
        super.onStop()
    }

    override fun onPause() {
        LogUtils.d("eMyTag", "pause")
        super.onPause()
    }

    override fun onResume() {
        LogUtils.d("eMyTag", "resume")
        viewModel.resreshData()
        super.onResume()
    }

    override fun onRestart() {
        LogUtils.d("eMyTag", "restart")
        super.onRestart()
    }

    private fun initRecycler() {
        res_main.itemAnimator = DefaultItemAnimator()
        res_main.adapter = MainRecyclerViewAdapter(this, viewModel.getBean(), object : MainRecyclerViewAdapter.OnMainRecyclerClick {
            override fun onItemClicked(position: Int) {
                LogUtils.d("MyTag", "onItemClick$position")
                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle()
                val intent = Intent(this@MainActivity, SongListActivity::class.java)
                intent.putExtra("color", getAlbumColor(position))
                intent.putExtra("title", viewModel.getBean().list[position].title)
                intent.putExtra("resource", getAlbumIconRes(position))
                intent.putExtra("bean", viewModel.getBean())

                intent.putExtra("position", position)
                startActivity(intent, bundle)
            }

            override fun onAlbumOptionsClicked(position: Int, x: Int, y: Int, color: Int) {
            }

            override fun onAlbumClicked(position: Int, x: Int, y: Int, color: Int) {
                val view = View.inflate(this@MainActivity, R.layout.placeholder, null)
                cl_main.addView(view)
                view.setBackgroundColor(color)
                val circularReveal = ViewAnimationUtils.createCircularReveal(view, x + dp2px(this@MainActivity, 80), y + dp2px(this@MainActivity, 90), 0f, 3000f)
//                mImage_bg.setBackgroundColor(Color.BLACK)
                circularReveal.duration = 600
                circularReveal.start()
                val bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this@MainActivity).toBundle()
                val intent = Intent(this@MainActivity, SongListActivity::class.java)
                intent.putExtra("color", color)
                intent.putExtra("title", viewModel.getBean().albumList[position].albumName)
                intent.putExtra("resource", getAlbumIconRes(position))
                intent.putExtra("bean", viewModel.getBean())
                startActivity(intent, bundle)

                circularReveal.addListener(object : Animator.AnimatorListener {
                    override fun onAnimationRepeat(animation: Animator?) {

                    }

                    override fun onAnimationEnd(animation: Animator?) {
                        cl_main.removeView(view)
                    }

                    override fun onAnimationCancel(animation: Animator?) {
                        cl_main.removeView(view)
                    }

                    override fun onAnimationStart(animation: Animator?) {
                    }

                })

            }

        })
        res_main.layoutManager = LinearLayoutManager(this)
    }

    private fun initService() {
        val intent = Intent(this, MusicService::class.java)
        bindService(intent, conn, Context.BIND_AUTO_CREATE)
    }

    override fun onDestroy() {
        super.onDestroy()
        unbindService(conn)
    }
}
