package com.assignment.multibashi.ui.video

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RvVideoScrollListener(
    private val linearLayoutManager: LinearLayoutManager,
    private val callback: (Int) -> Unit
) : RecyclerView.OnScrollListener() {

    private var currentPosition = 0

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_IDLE) {
            val pos = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
            if (currentPosition != pos) {
                currentPosition = pos
                callback(currentPosition)
            }
        }
    }
}