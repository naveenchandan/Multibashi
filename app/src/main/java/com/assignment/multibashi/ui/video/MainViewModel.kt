package com.assignment.multibashi.ui.video

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.assignment.multibashi.repository.MainRepository

class MainViewModel(application: Application) : AndroidViewModel(application){

    private val mainRepository by lazy {
        MainRepository()
    }

    fun loadVideoIds(){
        mainRepository.loadVideoData()
    }

    fun getVideoIds() = mainRepository.getVideoIds()

    override fun onCleared() {
        super.onCleared()
        mainRepository.clear()
    }
}