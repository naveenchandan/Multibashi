package com.assignment.multibashi.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.assignment.multibashi.data.remote.VideoDataApi
import com.assignment.multibashi.data.remote.VideoIdsApi
import com.assignment.multibashi.data.remote.VideoIdsData

class MainRepository {

    private var videoDataApi: VideoDataApi? = null

    private var videoIdsApi: VideoIdsApi? = null

    private val listVideoIds by lazy {
        MutableLiveData<List<VideoIdsData>>()
    }

    fun getVideoIds():LiveData<List<VideoIdsData>> = listVideoIds

    fun loadVideoData(){
        videoDataApi = VideoDataApi()
        videoDataApi!!.getVideoData{
            if (!it.isNullOrEmpty()){
             loadVideoIds(it[0].url)
            }
        }
    }

    private fun loadVideoIds(url:String){
        videoIdsApi = VideoIdsApi()
        videoIdsApi!!.getVideoIds(url){
            if (!it.isNullOrEmpty())
                listVideoIds.value = it
        }
    }

    fun clear(){
        videoDataApi?.cancel()
        videoIdsApi?.cancel()
    }

}