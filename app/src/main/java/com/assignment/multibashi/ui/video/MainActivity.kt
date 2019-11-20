package com.assignment.multibashi.ui.video

import android.os.Bundle
import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.assignment.multibashi.R
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), RvVideoCallback {

    private lateinit var mainViewModel: MainViewModel

    private lateinit var rvVideoAdapter: RvVideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_video.apply {
            val linearLayoutManager = LinearLayoutManager(
                this@MainActivity,
                RecyclerView.VERTICAL,
                false
            )
            layoutManager = linearLayoutManager
            addOnScrollListener(RvVideoScrollListener(linearLayoutManager){
                rvVideoAdapter changePlayer it
            })
            rvVideoAdapter = RvVideoAdapter(this@MainActivity,this@MainActivity)
            rv_video.adapter = rvVideoAdapter


        }
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        loadVideoData()
    }

    override fun onDestroy() {
        rvVideoAdapter.releasePlayer()
        rv_video.adapter = null
        super.onDestroy()
    }

    override fun onBackPressed() {
        if (fl_root.childCount == 3){
            rvVideoAdapter.exitFullScreen()
            return
        }
        super.onBackPressed()
    }

    override fun addVideoView(youTubePlayerView: YouTubePlayerView) {
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        youTubePlayerView.layoutParams = params
        fl_root.addView(youTubePlayerView)
    }

    override fun removeVideoView() {
        fl_root.removeViewAt(2)
    }

    private fun loadVideoData() {
        mainViewModel.loadVideoIds()
        mainViewModel.getVideoIds().observe(this, Observer {
            progress_bar.visibility = View.INVISIBLE
            rvVideoAdapter.listVideoIds.addAll(it)
            rvVideoAdapter.notifyDataSetChanged()
        })
    }

}
