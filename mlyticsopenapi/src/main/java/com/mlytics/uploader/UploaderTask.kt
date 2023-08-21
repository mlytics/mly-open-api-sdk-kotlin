package com.mlytics.mlyticsopenapi

import android.util.Log
import com.mlytics.uploader.ProgressRequestBody
import com.mlytics.uploader.StreamRequestBody
import com.mlytics.uploader.ProgressInterceptor
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.net.URL
import java.util.concurrent.TimeUnit

class UploaderTask(
    var url: URL,
    private var file: File,
    var closure: (bytesWritten: Long, contentLength: Long) -> Unit
) : Callback, ProgressRequestBody.Listener {

//    private val interceptor = ProgressInterceptor(this)
    private val client: OkHttpClient =
        OkHttpClient.Builder().connectTimeout(1000, TimeUnit.SECONDS).callTimeout(1, TimeUnit.DAYS)
            .retryOnConnectionFailure(true)
//            .addNetworkInterceptor(interceptor)
            .build()

    var mediaType: String? = null
    var success: Boolean? = null
    private var call: Call? = null
    var contentLength: Long = -1
    var bytesWritten: Long = 0

    fun start() {
        val type = mediaType?.toMediaTypeOrNull()
        val stream = StreamRequestBody(type, file)
        val body = ProgressRequestBody(stream, this)
        val builder = Request.Builder()
        val req = builder.url(url).put(body).build()
        call = client.newCall(req)
        call!!.enqueue(this)
    }

    fun cancel() {
        call?.cancel()
    }

    override fun onFailure(call: Call, e: IOException) {
        success = false
        Log.e("mly-api", "onFailure", e)
    }

    override fun onResponse(call: Call, response: Response) {
        success = true
        Log.d("mly-api", "onResponse ${response.body}")
    }

    override fun onRequestProgress(bytesWritten: Long, contentLength: Long) {
        closure(bytesWritten, contentLength)
        this.bytesWritten = bytesWritten
        this.contentLength = contentLength
    }
}