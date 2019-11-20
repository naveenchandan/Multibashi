package com.assignment.multibashi.data.remote

import android.util.Log
import com.assignment.multibashi.util.ApiService
import com.assignment.multibashi.util.getRetrofitInstance
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class VideoIdsData(

    @SerializedName("video_id")
    val videoId: String,

    @SerializedName("title")
    val title: String
)

class VideoIdsApi {

    private var call: Call<List<VideoIdsData>>? = null

    fun getVideoIds(url: String, callback: (List<VideoIdsData>?) -> Unit) {
        call = getRetrofitInstance()
            .create(ApiService::class.java)
            .getVideoIds(url)
        call!!.enqueue(object : Callback<List<VideoIdsData>> {
            override fun onFailure(call: Call<List<VideoIdsData>>, t: Throwable) {
                Log.e(TAG, "failed")
            }

            override fun onResponse(
                call: Call<List<VideoIdsData>>,
                response: Response<List<VideoIdsData>>
            ) {
                callback(
                    if (response.isSuccessful && response.body() != null) {
                        Log.e(TAG, "success")
                        response.body()!!
                    } else {
                        Log.e(TAG, "error")
                        null
                    }
                )
            }
        })
    }

    fun cancel() = call?.cancel()

    companion object {

        private val TAG = "Retrofit ${VideoIdsApi::class.java.simpleName}"
    }
}