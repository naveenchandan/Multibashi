package com.assignment.multibashi.ui.video

import android.content.Context
import android.os.Handler
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import com.assignment.multibashi.R
import com.assignment.multibashi.data.remote.VideoIdsData
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerCallback
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.YouTubePlayerFullScreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import kotlinx.android.synthetic.main.item_rv_video.view.*

class RvVideoAdapter(
    private val context: Context,
    private val callback:RvVideoCallback
) : RecyclerView.Adapter<RvVideoViewHolder>() {

    val listVideoIds = arrayListOf<VideoIdsData>()

    private var currentFocusedPosition = 0

    private var isVideoPlaying = false

    private var youTubePlayerView: YouTubePlayerView? = null

    private var youTubePlayer:YouTubePlayer? = null

    private var flItemVideo :FrameLayout? = null

    private val handler = Handler()
    private val runnable = Runnable {
        isVideoPlaying = false
        currentFocusedPosition = changedPosition
        notifyDataSetChanged()
    }

    private var changedPosition = 0

    init {
        youTubePlayerView = YouTubePlayerView(context)
        youTubePlayerView?.getYouTubePlayerWhenReady(object :YouTubePlayerCallback{
            override fun onYouTubePlayer(youTubePlayer: YouTubePlayer) {
                this@RvVideoAdapter.youTubePlayer = youTubePlayer
                notifyDataSetChanged()
            }
        })
        youTubePlayerView?.addFullScreenListener(object : YouTubePlayerFullScreenListener{
            override fun onYouTubePlayerEnterFullScreen() {
                flItemVideo?.removeAllViews()
                callback.addVideoView(youTubePlayerView!!)
            }

            override fun onYouTubePlayerExitFullScreen() {
                callback.removeVideoView()
                val params = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT
                )
                params.topMargin = 30
                params.gravity = Gravity.CENTER
                youTubePlayerView?.layoutParams = params
                flItemVideo?.addView(youTubePlayerView)
            }
        })
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RvVideoViewHolder =
        RvVideoViewHolder(
            LayoutInflater.from(context).inflate(
                R.layout.item_rv_video,
                parent,
                false
            )
        )

    override fun getItemCount(): Int = listVideoIds.size

    override fun onBindViewHolder(holder: RvVideoViewHolder, position: Int) {
        holder.setIsRecyclable(false)
        bind(holder.itemView, listVideoIds[position], position)
    }

    private fun bind(itemView: View, videoIdsData: VideoIdsData, position: Int) {
        Glide.with(itemView.image_item_thumbnail.context)
            .load("https://img.youtube.com/vi/${videoIdsData.videoId}/sddefault.jpg")
            .into(itemView.image_item_thumbnail)
        itemView.image_item_thumbnail.visibility = View.VISIBLE
        if (position == currentFocusedPosition) {
            if (!isVideoPlaying && youTubePlayer != null) {
                flItemVideo = itemView.fl_item_container
                isVideoPlaying = true
                itemView.image_item_thumbnail.visibility = View.INVISIBLE
                flItemVideo?.addView(youTubePlayerView)
                youTubePlayer?.loadVideo(videoIdsData.videoId, 0.0F)
            }
        }
        itemView.text_item_title.text = videoIdsData.title
    }

    infix fun changePlayer(pos: Int) {
        handler.removeCallbacks(runnable)
        youTubePlayer?.pause()
        youTubePlayer?.seekTo(0.0F)
        flItemVideo?.removeAllViews()
        changedPosition = pos
        handler.postDelayed(runnable,750L)
    }

    fun exitFullScreen() = youTubePlayerView?.exitFullScreen()

    fun releasePlayer(){
        flItemVideo?.removeAllViews()
        youTubePlayerView?.release()
    }

}

class RvVideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

interface RvVideoCallback{

    fun addVideoView(youTubePlayerView: YouTubePlayerView)

    fun removeVideoView()
}