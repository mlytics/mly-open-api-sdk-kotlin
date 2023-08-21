package com.mlytics.mlyticsopenapi

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.io.File
import java.net.URL

class MainActivity : Activity() {

    private val client = Client("YOUR_CLIENT_ID")
    lateinit var uploadButton: Button
    lateinit var infoText: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        uploadButton = findViewById(R.id.uploadButton)
        infoText = findViewById(R.id.infoText)
        uploadButton.setOnClickListener {
            val url = URL("http://192.168.31.46:9090/upload")
            getDemoMp4()?.let { file ->
                client.uploadAsset(url, file) { bytesWritten, contentLength ->
                    Log.d("mly-api", "task:${file.absolutePath}:$bytesWritten/$contentLength")
                    this@MainActivity.runOnUiThread {
                        infoText.text =
                            "file:${file.absolutePath} \nuploaded: $bytesWritten \ntotal: $contentLength"
                    }
                }
            }
        }

    }

    private fun getDemoMp4(): File? {

        val dir = this.applicationContext.getExternalFilesDir("Download")
        return File(dir, "test.mp4")
    }
}