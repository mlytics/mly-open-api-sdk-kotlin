package com.mlytics.uploader

import android.util.Log
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.internal.closeQuietly
import okio.BufferedSink
import okio.Source
import okio.source
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

class StreamRequestBody(val mediaType: MediaType?, val file: File) : RequestBody() {
    var skip: Long = 0L
    override fun contentType(): MediaType? {
        return mediaType
    }

    override fun contentLength(): Long {
        return try {
            file.length() - skip
        } catch (e: IOException) {
            Log.e("mly-api", "contentLength", e)
            -1
        }
    }

    @Throws(IOException::class)
    override fun writeTo(sink: BufferedSink) {
        val input = FileInputStream(file)
        if (skip > 0) {
            input.skip(skip)
        }
        input.source().use {
            sink.writeAll(it)
        }
    }

}