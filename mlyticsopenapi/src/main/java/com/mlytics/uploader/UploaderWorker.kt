package com.mlytics.uploader

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ListenableWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.Operation
import androidx.work.WorkManager
import androidx.work.WorkRequest
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mlytics.mlyticsopenapi.UploaderTask
import kotlinx.coroutines.runBlocking
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.coroutines.suspendCoroutine

class UploaderWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    companion object {
        fun start(context: Context, workRequest: WorkRequest): Operation {
            return WorkManager.getInstance(context).enqueue(workRequest)
        }
    }

    lateinit var task: UploaderTask
    lateinit var file: File
    var id: String = ""
        get() = file.absolutePath
    val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
    lateinit var request: OneTimeWorkRequest

    fun start() {
        request = OneTimeWorkRequest.Builder(UploaderWorker::class.java).setConstraints(constraints)
            .addTag("mlytics-uploader:$id")
            .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS).build()
    }

    override fun doWork(): Result {
        task.start()
        return Result.success()
    }

    fun cancel() {
        WorkManager.getInstance(this.applicationContext).cancelWorkById(request.id)
    }

}