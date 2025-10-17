package com.example.todoapp.fragments.list.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.R
import com.example.todoapp.data.models.Priority
import com.example.todoapp.data.models.ToDoData
import com.example.todoapp.databinding.RowLayoutBinding
import com.example.todoapp.fragments.list.NotificationHelper
import java.sql.Date
import java.text.SimpleDateFormat
import java.util.Locale

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    var dataList = emptyList<ToDoData>()

    class ListViewHolder(private val binding: RowLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(toDoData: ToDoData) {
            binding.toDoData = toDoData
            binding.executePendingBindings()
            val priorityView = binding.priorityIndicator

            when (toDoData.priority) {
                Priority.High -> priorityView.setBackgroundResource(R.color.red)
                Priority.Medium -> priorityView.setBackgroundResource(R.color.yellow)
                Priority.Low -> priorityView.setBackgroundResource(R.color.green)
            }
            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            val currentDate = sdf.parse(sdf.format(java.util.Date()))

            try {
                val endDate = sdf.parse(toDoData.endDate)
                if (endDate != null) {
                    when {
                        endDate.before(currentDate) -> {
                            // QUÁ HẠN → đỏ nhạt
                            binding.root.setBackgroundColor(Color.parseColor("#FFCDD2"))
                            NotificationHelper.sendNotification(
                                binding.root.context,
                                toDoData.id,
                                "Công việc đã hết hạn",
                                "Công việc '${toDoData.title}' đã quá hạn!"
                            )
                        }
                        sdf.format(currentDate) == toDoData.endDate -> {
                            // Hôm nay hết hạn → vàng
                            binding.root.setBackgroundColor(Color.parseColor("#FFF9C4"))
                            NotificationHelper.sendNotification(
                                binding.root.context,
                                toDoData.id,
                                "Công việc sắp hết hạn",
                                "Công việc '${toDoData.title}' hết hạn hôm nay!"
                            )
                        }
                        else -> {
                            // Còn thời gian → xanh lá nhạt
                            binding.root.setBackgroundColor(Color.parseColor("#C8E6C9"))
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

    }

        companion object {
            fun from(parent: ViewGroup): ListViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = RowLayoutBinding.inflate(layoutInflater, parent, false)
                return ListViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        return ListViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(dataList[position])

    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    fun setData(toDoData: List<ToDoData>) {
        val toDoDiffUtil = ToDoDiffUtil(dataList, toDoData)
        val toDoDiffResult = DiffUtil.calculateDiff(toDoDiffUtil)
        this.dataList = toDoData
        toDoDiffResult.dispatchUpdatesTo(this)
    }
}