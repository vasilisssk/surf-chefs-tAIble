package com.example.chefstable.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chefstable.databinding.ItemNotificationBinding
import com.example.chefstable.ui.notifications.NotificationItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.concurrent.TimeUnit

class NotificationAdapter(
    private val onNotificationClick: (NotificationItem) -> Unit
) : ListAdapter<NotificationItem, NotificationAdapter.NotificationViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val binding = ItemNotificationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class NotificationViewHolder(
        private val binding: ItemNotificationBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onNotificationClick(getItem(position))
                }
            }
        }

        fun bind(item: NotificationItem) {
            binding.tvNotificationTitle.text = item.title
            binding.tvNotificationMessage.text = item.message
            binding.tvNotificationTime.text = formatTimeAgo(item.timestamp)

            if (item.isRead) {
                binding.viewUnreadIndicator.visibility = View.GONE
            } else {
                binding.viewUnreadIndicator.visibility = View.VISIBLE
            }
        }

        private fun formatTimeAgo(timestamp: Long): String {
            val now = System.currentTimeMillis()
            val diff = now - timestamp

            return when {
                diff < TimeUnit.MINUTES.toMillis(1) -> "Только что"
                diff < TimeUnit.HOURS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toMinutes(diff)} мин назад"
                diff < TimeUnit.DAYS.toMillis(1) -> "${TimeUnit.MILLISECONDS.toHours(diff)} ч назад"
                diff < TimeUnit.DAYS.toMillis(7) -> "${TimeUnit.MILLISECONDS.toDays(diff)} дн назад"
                else -> {
                    val format = SimpleDateFormat("d MMMM", Locale("ru"))
                    format.format(timestamp)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<NotificationItem>() {
            override fun areItemsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: NotificationItem, newItem: NotificationItem): Boolean =
                oldItem == newItem
        }
    }
}
