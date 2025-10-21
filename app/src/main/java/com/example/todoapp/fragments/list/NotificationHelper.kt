package com.example.todoapp.fragments.list

import android.app.NotificationChannel
import android.content.Context
import androidx.core.app.NotificationCompat
import com.example.todoapp.R
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationManagerCompat
import com.example.todoapp.MainActivity

object NotificationHelper {
    private const val CHANNEL_ID = "todo_channel"

//
    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "ToDo Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Thông báo nhắc nhở công việc"
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
    fun sendNotification(context: Context, taskId: Int, title: String, message: String) {

        val prefs = context.getSharedPreferences("notified_tasks", Context.MODE_PRIVATE)
        val alreadySent = prefs.getBoolean(taskId.toString(), false)

        if (alreadySent) {
            // Đã gửi rồi => bỏ qua
            return
        }

        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            taskId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(context, "todo_channel")
            .setSmallIcon(R.drawable.outline_event_note_24) // thêm icon nhỏ trong res/drawable
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
            .build()

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(System.currentTimeMillis().toInt(), notification)

        //Đánh dấu task này đã gửi thông báo
        prefs.edit().putBoolean(taskId.toString(), true).apply()
    }
    //gọi khi người dùng chỉnh sửa hoặc thay đổi endDate.
    fun resetNotificationStatus(context: Context, taskId: Int) {
        val prefs = context.getSharedPreferences("notified_tasks", Context.MODE_PRIVATE)
        prefs.edit().remove(taskId.toString()).apply()
    }
}