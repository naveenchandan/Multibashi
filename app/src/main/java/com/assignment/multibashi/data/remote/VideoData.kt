package com.assignment.multibashi.data.remote

import android.util.Log
import com.assignment.multibashi.util.ApiService
import com.assignment.multibashi.util.getRetrofitInstance
import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

data class VideoDataResponseModel(

    @SerializedName("requests")
    val listRequestData: List<VideoIdRequestData>
)

data class VideoIdRequestData(

    @SerializedName("url")
    val url: String
)

class VideoDataApi {

    private var call: Call<VideoDataResponseModel>? = null

    fun getVideoData(callback: (List<VideoIdRequestData>?) -> Unit) {
        call = getRetrofitInstance()
            .create(ApiService::class.java)
            .getVideoData()
        call!!.enqueue(object : Callback<VideoDataResponseModel> {
            override fun onFailure(call: Call<VideoDataResponseModel>, t: Throwable) {
                Log.e(TAG, "failed")
                callback(null)
            }

            override fun onResponse(
                call: Call<VideoDataResponseModel>,
                response: Response<VideoDataResponseModel>
            ) {
                callback(
                    if (response.isSuccessful && response.body() != null) {
                        Log.e(TAG, "success")
                        response.body()!!.listRequestData
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

        private val TAG = "Retrofit ${VideoDataApi::class.java.simpleName}"
    }

}