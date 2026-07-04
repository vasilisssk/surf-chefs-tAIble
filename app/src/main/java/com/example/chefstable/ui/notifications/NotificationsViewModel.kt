package com.example.chefstable.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

data class NotificationItem(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: Long,
    val isRead: Boolean
)

class NotificationsViewModel : ViewModel() {

    private val _notifications = MutableLiveData<List<NotificationItem>>(emptyList())
    val notifications: LiveData<List<NotificationItem>> = _notifications

    private val _filter = MutableLiveData(NotificationFilter.ALL)
    val filter: LiveData<NotificationFilter> = _filter

    private val allNotifications = mutableListOf<NotificationItem>()

    init {
        seedDemoNotifications()
    }

    fun setFilter(filter: NotificationFilter) {
        _filter.value = filter
        applyFilter()
    }

    fun markAsRead(id: String) {
        val index = allNotifications.indexOfFirst { it.id == id }
        if (index >= 0) {
            val item = allNotifications[index]
            allNotifications[index] = item.copy(isRead = true)
            applyFilter()
        }
    }

    fun clearAll() {
        allNotifications.clear()
        applyFilter()
    }

    private fun applyFilter() {
        val currentFilter = _filter.value ?: NotificationFilter.ALL
        val filtered = when (currentFilter) {
            NotificationFilter.ALL -> allNotifications.toList()
            NotificationFilter.UNREAD -> allNotifications.filter { !it.isRead }
            NotificationFilter.READ -> allNotifications.filter { it.isRead }
        }
        _notifications.value = filtered
    }

    private fun seedDemoNotifications() {
        val now = System.currentTimeMillis()
        allNotifications.addAll(
            listOf(
                NotificationItem(
                    id = "n1",
                    title = "Напоминание о классе",
                    message = "Ваш кулинарный класс начнется через час",
                    timestamp = now - 3600000,
                    isRead = false
                ),
                NotificationItem(
                    id = "n2",
                    title = "Бронирование подтверждено",
                    message = "Ваше бронирование успешно создано",
                    timestamp = now - 86400000,
                    isRead = true
                )
            )
        )
        applyFilter()
    }
}

enum class NotificationFilter { ALL, UNREAD, READ }