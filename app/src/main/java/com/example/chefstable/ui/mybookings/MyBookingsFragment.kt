package com.example.chefstable.ui.mybookings

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.chefstable.R
import com.example.chefstable.data.remote.dto.BookingDto
import com.example.chefstable.databinding.FragmentMyBookingsBinding
import com.example.chefstable.ui.common.BookingAdapter
import com.example.chefstable.ui.review.ReviewDialogFragment
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class MyBookingsFragment : Fragment() {

    private var _binding: FragmentMyBookingsBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MyBookingsViewModel by viewModel()

    private lateinit var adapter: BookingAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyBookingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = BookingAdapter(
            onCancelClick = { booking -> showCancelDialog(booking) },
            onRateClick = { booking -> showReviewDialog(booking) },
            onBookingClick = {}
        )
        binding.rvBookings.adapter = adapter
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadBookings()
        }

        binding.chipGroupFilter.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isEmpty()) return@setOnCheckedStateChangeListener
            when (checkedIds[0]) {
                R.id.chip_all -> viewModel.filterBy(null)
                R.id.chip_upcoming -> viewModel.filterBy("upcoming")
                R.id.chip_past -> viewModel.filterBy("past")
                R.id.chip_cancelled -> viewModel.filterBy("cancelled")
            }
        }

        binding.btnRetry.setOnClickListener {
            viewModel.loadBookings()
        }
    }

    private fun showCancelDialog(booking: BookingDto) {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.my_bookings_cancel_confirm)
            .setPositiveButton(R.string.my_bookings_cancel_yes) { _, _ ->
                viewModel.cancelBooking(booking.id)
            }
            .setNegativeButton(R.string.my_bookings_cancel_no, null)
            .show()
    }

    private fun showReviewDialog(booking: BookingDto) {
        val chefId = booking.cookingClass.chef.id
        ReviewDialogFragment.newInstance(
            bookingId = booking.id,
            chefId = chefId,
            onSubmitted = {
                Snackbar.make(binding.root, R.string.review_success, Snackbar.LENGTH_LONG).show()
            }
        ).show(parentFragmentManager, "review_dialog")
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is MyBookingsState.Loading -> {
                    binding.swipeRefresh.isRefreshing = true
                    binding.progressBar.visibility = View.GONE
                    binding.layoutEmpty.visibility = View.GONE
                    binding.layoutError.visibility = View.GONE
                }
                is MyBookingsState.Content -> {
                    binding.swipeRefresh.isRefreshing = false
                    binding.progressBar.visibility = View.GONE
                    binding.layoutEmpty.visibility = View.GONE
                    binding.layoutError.visibility = View.GONE
                    adapter.submitList(state.bookings)
                }
                is MyBookingsState.Empty -> {
                    binding.swipeRefresh.isRefreshing = false
                    binding.progressBar.visibility = View.GONE
                    binding.layoutEmpty.visibility = View.VISIBLE
                    binding.layoutError.visibility = View.GONE
                    adapter.submitList(emptyList())
                }
                is MyBookingsState.Error -> {
                    binding.swipeRefresh.isRefreshing = false
                    binding.progressBar.visibility = View.GONE
                    binding.layoutEmpty.visibility = View.GONE
                    binding.layoutError.visibility = View.VISIBLE
                    binding.tvErrorMessage.text = state.message
                }
            }
        }

        viewModel.cancelMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                viewModel.onCancelMessageShown()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
