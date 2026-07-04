package com.example.chefstable.ui.notifications

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.chefstable.R
import com.example.chefstable.databinding.FragmentNotificationsBinding
import com.example.chefstable.ui.common.NotificationAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NotificationsViewModel by viewModel()

    private lateinit var adapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = NotificationAdapter { notification ->
            if (!notification.isRead) {
                viewModel.markAsRead(notification.id)
            }
        }
        binding.rvNotifications.adapter = adapter
    }

    private fun setupListeners() {
        binding.chipGroupFilter.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) return@setOnCheckedStateChangeListener
            when (checkedIds[0]) {
                R.id.chip_all -> viewModel.setFilter(NotificationFilter.ALL)
                R.id.chip_unread -> viewModel.setFilter(NotificationFilter.UNREAD)
                R.id.chip_read -> viewModel.setFilter(NotificationFilter.READ)
            }
        }

        binding.btnClear.setOnClickListener {
            viewModel.clearAll()
        }
    }

    private fun observeViewModel() {
        viewModel.notifications.observe(viewLifecycleOwner) { notifications ->
            adapter.submitList(notifications)
            if (notifications.isEmpty()) {
                binding.layoutEmpty.visibility = View.VISIBLE
                binding.rvNotifications.visibility = View.GONE
            } else {
                binding.layoutEmpty.visibility = View.GONE
                binding.rvNotifications.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}