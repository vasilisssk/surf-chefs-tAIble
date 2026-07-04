package com.example.chefstable.ui.notifications

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NotificationsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: NotificationsViewModel

    @Before
    fun setup() {
        viewModel = NotificationsViewModel()
    }

    @Test
    fun `initial notifications are loaded`() {
        assertEquals(2, viewModel.notifications.value?.size)
    }

    @Test
    fun `initial filter is ALL`() {
        assertEquals(NotificationFilter.ALL, viewModel.filter.value)
    }

    @Test
    fun `filter by unread shows only unread`() {
        viewModel.setFilter(NotificationFilter.UNREAD)

        val notifications = viewModel.notifications.value ?: emptyList()
        assertEquals(1, notifications.size)
        assertEquals(false, notifications[0].isRead)
        assertEquals("n1", notifications[0].id)
    }

    @Test
    fun `filter by read shows only read`() {
        viewModel.setFilter(NotificationFilter.READ)

        val notifications = viewModel.notifications.value ?: emptyList()
        assertEquals(1, notifications.size)
        assertEquals(true, notifications[0].isRead)
        assertEquals("n2", notifications[0].id)
    }

    @Test
    fun `filter by all shows all notifications`() {
        viewModel.setFilter(NotificationFilter.UNREAD)
        viewModel.setFilter(NotificationFilter.ALL)

        assertEquals(2, viewModel.notifications.value?.size)
    }

    @Test
    fun `mark as read updates notification`() {
        viewModel.setFilter(NotificationFilter.ALL)
        viewModel.markAsRead("n1")

        val notifications = viewModel.notifications.value ?: emptyList()
        val n1 = notifications.find { it.id == "n1" }
        assertTrue(n1?.isRead == true)
    }

    @Test
    fun `mark as read moves notification out of unread filter`() {
        viewModel.setFilter(NotificationFilter.UNREAD)
        assertEquals(1, viewModel.notifications.value?.size)

        viewModel.markAsRead("n1")

        viewModel.setFilter(NotificationFilter.UNREAD)
        assertEquals(0, viewModel.notifications.value?.size)
    }

    @Test
    fun `clear all removes all notifications`() {
        viewModel.clearAll()

        assertEquals(0, viewModel.notifications.value?.size)
    }

    @Test
    fun `clear all shows empty list in all filters`() {
        viewModel.clearAll()

        viewModel.setFilter(NotificationFilter.ALL)
        assertEquals(0, viewModel.notifications.value?.size)

        viewModel.setFilter(NotificationFilter.UNREAD)
        assertEquals(0, viewModel.notifications.value?.size)

        viewModel.setFilter(NotificationFilter.READ)
        assertEquals(0, viewModel.notifications.value?.size)
    }
}
