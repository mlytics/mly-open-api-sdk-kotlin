package com.mlytics.mlyticsopenapi

import com.mlytics.uploader.MediaTypes
import java.io.File
import java.net.URL

class Client(id: String) {
    fun uploadAsset(
        url: URL, file: File, closure: (bytesWritten: Long, contentLength: Long) -> Unit
    ) {
        val task = UploaderTask(url, file, closure)
        task.mediaType = MediaTypes.MediaTypeVideo
        task.start()
    }
}