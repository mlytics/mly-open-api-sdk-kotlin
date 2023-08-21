package com.mlytics.uploader

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

class ProgressInterceptor(private val progressListener: ProgressRequestBody.Listener) :
    Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest: Request = chain.request()
        if (originalRequest.body == null) {
            return chain.proceed(originalRequest)
        }
        val progressRequest = originalRequest.newBuilder().method(
            originalRequest.method, ProgressRequestBody(
                originalRequest.body!!, progressListener
            )
        ).build()
        return chain.proceed(progressRequest)
    }
}