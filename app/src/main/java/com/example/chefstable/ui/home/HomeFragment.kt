package com.example.chefstable.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.chefstable.R
import com.example.chefstable.data.remote.dto.BookingDto
import com.example.chefstable.data.remote.dto.CookingClassDto
import com.example.chefstable.databinding.FragmentHomeBinding
import com.example.chefstable.ui.common.BookingAdapter
import com.example.chefstable.ui.common.ClassAdapter
import com.google.android.material.bottomnavigation.BottomNavigationView
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModel()

    private lateinit var bookingAdapter: BookingAdapter
    private lateinit var classAdapter: ClassAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerViews()
        setupListeners()
        observeViewModel()
        viewModel.loadData()
    }

    private fun setupRecyclerViews() {
        bookingAdapter = BookingAdapter(
            onCancelClick = {},
            onRateClick = {},
            onBookingClick = {},
            onGoToBookingsClick = { navigateToMyBookings() }
        )
        binding.rvUpcomingBookings.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvUpcomingBookings.adapter = bookingAdapter

        classAdapter = ClassAdapter { cls ->
            navigateToClassDetail(cls.id)
        }
        binding.rvRecommendedClasses.adapter = classAdapter
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadData()
        }

        binding.btnRetry.setOnClickListener {
            viewModel.loadData()
        }
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HomeState.Loading -> {
                    binding.swipeRefresh.isRefreshing = true
                    binding.progressBar.visibility = View.GONE
                    binding.layoutError.visibility = View.GONE
                }
                is HomeState.Content -> {
                    binding.swipeRefresh.isRefreshing = false
                    binding.progressBar.visibility = View.GONE
                    binding.layoutError.visibility = View.GONE

                    if (state.bookings.isEmpty()) {
                        binding.tvNoBookings.visibility = View.VISIBLE
                        binding.rvUpcomingBookings.visibility = View.GONE
                    } else {
                        binding.tvNoBookings.visibility = View.GONE
                        binding.rvUpcomingBookings.visibility = View.VISIBLE
                        bookingAdapter.submitList(state.bookings)
                    }

                    if (state.classes.isEmpty()) {
                        binding.tvNoClasses.visibility = View.VISIBLE
                        binding.rvRecommendedClasses.visibility = View.GONE
                    } else {
                        binding.tvNoClasses.visibility = View.GONE
                        binding.rvRecommendedClasses.visibility = View.VISIBLE
                        classAdapter.submitList(state.classes)
                    }
                }
                is HomeState.Empty -> {
                    binding.swipeRefresh.isRefreshing = false
                    binding.tvNoBookings.visibility = View.VISIBLE
                    binding.rvUpcomingBookings.visibility = View.GONE
                    binding.tvNoClasses.visibility = View.VISIBLE
                    binding.rvRecommendedClasses.visibility = View.GONE
                }
                is HomeState.Error -> {
                    binding.swipeRefresh.isRefreshing = false
                    binding.layoutError.visibility = View.VISIBLE
                    binding.tvErrorMessage.text = state.message
                }
            }
        }
    }

    private fun navigateToClassDetail(classId: String) {
        findNavController().navigate(
            R.id.navigation_class_detail,
            bundleOf("classId" to classId)
        )
    }

    private fun navigateToMyBookings() {
        val navView = requireActivity().findViewById<BottomNavigationView>(R.id.nav_view)
        navView.selectedItemId = R.id.navigation_bookings
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}