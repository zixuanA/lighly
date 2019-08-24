package com.mredrock.cyxbs.freshman.Activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.transition.ChangeBounds
import android.transition.Fade
import androidx.recyclerview.widget.LinearLayoutManager
import com.mredrock.cyxbs.common.ui.BaseViewModelActivity
import com.mredrock.cyxbs.common.utils.LogUtils
import com.mredrock.cyxbs.freshman.Adapter.SearchRecyclerViewAdapter
import com.mredrock.cyxbs.freshman.Bean.MessageBean
import com.mredrock.cyxbs.freshman.Event.MusicChangeEvent
import com.mredrock.cyxbs.freshman.R
import com.mredrock.cyxbs.freshman.ViewModel.SearchViewModel
import kotlinx.android.synthetic.main.activity_search.*
import org.greenrobot.eventbus.EventBus
import org.jetbrains.anko.backgroundColor

class SearchActivity : BaseViewModelActivity<SearchViewModel>() {
    override val viewModelClass: Class<SearchViewModel> = SearchViewModel::class.java
    override val isFragmentActivity: Boolean = false
    private var bean: MessageBean? = null
    private val callback = object : SearchRecyclerViewAdapter.OnSearchClick {
        override fun itemClick(position: Int) {
            bean ?: return
            EventBus.getDefault().post(MusicChangeEvent(bean!!, position))
            startActivity(Intent(this@SearchActivity, SongActivity::class.java).apply {
                putExtra("color", intent.extras?.getInt("color"))
                putExtra("bean", intent.extras?.getSerializable("bean"))
            })
        }
    }
    private val adapter = SearchRecyclerViewAdapter(callback)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        cl_search.backgroundColor = intent.extras?.getInt("color", 0xffffff)!!
        initTransition()
        initRecycler()
        viewModel.data.observe {
            if (it != null) {
                adapter.setBean(it)
                bean = it
            }
        }
        initListener()
    }

    private fun initListener() {
        et_search.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                LogUtils.d("MyTag text", s.toString())
                if (s != null) {
                    viewModel.getBean(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        fl_search_back.setOnClickListener {
            finishAfterTransition()
        }
    }

    private fun initRecycler() {
        res_search.adapter = adapter
        res_search.layoutManager = LinearLayoutManager(this)
    }

    private fun initTransition() {
        window.sharedElementEnterTransition = ChangeBounds()
        window.enterTransition = ChangeBounds()
        window.returnTransition = Fade()
        window.sharedElementReturnTransition = ChangeBounds().apply { removeTarget(ll_search) }
    }
}
