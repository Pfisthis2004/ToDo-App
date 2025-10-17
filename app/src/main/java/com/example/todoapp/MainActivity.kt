package com.example.todoapp

import android.Manifest
import android.app.AlertDialog
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.todoapp.databinding.ActivityMainBinding
import com.example.todoapp.fragments.list.NotificationHelper
import com.example.todoapp.utils.ReminderWorker
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBarWithNavController(findNavController(R.id.nav_host_fragment))

        askNotificationPermission()
        createNotificationChannel()
        setupBackgroundColor()
        setupWorkManager()
    }


    private fun setupBackgroundColor() {
        val prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val isDarkMode = prefs.getBoolean("dark_mode", false)
        val color = if (isDarkMode) Color.GRAY else Color.WHITE
        window.decorView.setBackgroundColor(color)
    }


    private fun toggleTheme() {
        val prefs = getSharedPreferences("app_settings", Context.MODE_PRIVATE)
        val isDark = prefs.getBoolean("dark_mode", false)
        val newMode = !isDark
        prefs.edit().putBoolean("dark_mode", newMode).apply()

        // Cập nhật màu ngay
        val color = if (newMode) Color.GRAY else Color.WHITE
        window.decorView.setBackgroundColor(color)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_settings -> {
                toggleTheme()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    private fun setupWorkManager() {
        val workRequest = PeriodicWorkRequestBuilder<ReminderWorker>(
            3, TimeUnit.DAYS
        ).build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "todo_reminder",
                ExistingPeriodicWorkPolicy.KEEP,
                workRequest
            )
    }


    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "todo_channel",
                "ToDo Notifications",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            channel.description = "Thông báo công việc sắp hoặc đã hết hạn"
            val manager = getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }


    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Cho phép gửi thông báo")
                    .setMessage("Ứng dụng cần quyền thông báo để nhắc bạn các công việc quan trọng.")
                    .setPositiveButton("Đồng ý") { _, _ ->
                        requestPermissions(
                            arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                            1001
                        )
                    }
                    .setNegativeButton("Hủy", null)
                    .show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
                || super.onSupportNavigateUp()
    }
    private fun applyThemeToViews(isDark: Boolean) {
        val bgColor = if (isDark) Color.parseColor("#303030") else Color.WHITE
        val textColor = if (isDark) Color.WHITE else Color.BLACK

        // Màu nền chính
        window.decorView.setBackgroundColor(bgColor)
        binding.root.setBackgroundColor(bgColor)

        // Màu thanh action bar (nếu có)
        supportActionBar?.setBackgroundDrawable(android.graphics.drawable.ColorDrawable(bgColor))
        supportActionBar?.title = if (isDark) "🌙 Chế độ tối" else "☀️ Chế độ sáng"

        // Nếu fragment có RecyclerView hoặc TextView — gửi broadcast để fragment tự đổi màu
        val intent = android.content.Intent("THEME_CHANGED")
        intent.putExtra("isDark", isDark)
        sendBroadcast(intent)
    }
}