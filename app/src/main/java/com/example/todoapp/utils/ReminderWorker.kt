package com.example.todoapp.utils

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.todoapp.fragments.list.NotificationHelper

class ReminderWorker(context: Context, params: WorkerParameters): Worker(context,params) {
    override fun doWork(): Result {
        NotificationHelper.sendNotification(
            applicationContext,
            taskId = 1,
            "Nhắc nhở hàng tuần 🕒",
            "Hãy kiểm tra và cập nhật danh sách công việc của bạn nhé!"
        )
        return Result.success()
    }
}