package com.example.chefstable.ui.schedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chefstable.R
import com.example.chefstable.data.remote.dto.ChefDto
import com.example.chefstable.databinding.FragmentScheduleBinding
import com.example.chefstable.ui.common.ClassAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class ScheduleFragment : Fragment() {

    private var _binding: FragmentScheduleBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ScheduleViewModel by viewModel()

    private lateinit var adapter: ClassAdapter
    private var chefs: List<ChefDto> = emptyList()
    private var selectedChef: ChefDto? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScheduleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupListeners()
        observeViewModel()
    }

    private fun setupRecyclerView() {
        adapter = ClassAdapter { cls ->
            findNavController().navigate(
                R.id.navigation_class_detail,
                bundleOf("classId" to cls.id)
            )
        }
        binding.rvClasses.adapter = adapter
    }

    private fun setupListeners() {
        binding.swipeRefresh.setOnRefreshListener {
            viewModel.loadClasses()
        }

        binding.btnResetFilter.setOnClickListener {
            selectedChef = null
            binding.actvChefFilter.setText("")
            viewModel.resetFilters()
        }

        binding.actvChefFilter.setOnItemClickListener { _, _, position, _ ->
            if (position < chefs.size) {
                selectedChef = chefs[position]
                viewModel.filterByChef(selectedChef!!.id)
            }
        }

        binding.btnRetry.setOnClickListener {
            viewModel.loadClasses()
        }
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ScheduleState.Loading -> {
                    binding.swipeRefresh.isRefreshing = true
                    binding.progressBar.visibility = View.GONE
                    binding.layoutEmpty.visibility = View.GONE
                    binding.layoutError.visibility = View.GONE
                }
                is ScheduleState.Content -> {
                    binding.swipeRefresh.isRefreshing = false
                    binding.progressBar.visibility = View.GONE
                    binding.layoutEmpty.visibility = View.GONE
                    binding.layoutError.visibility = View.GONE
                    adapter.submitList(state.classes)
                }
                is ScheduleState.Empty -> {
                    binding.swipeRefresh.isRefreshing = false
                    binding.progressBar.visibility = View.GONE
                    binding.layoutEmpty.visibility = View.VISIBLE
                    binding.layoutError.visibility = View.GONE
                    adapter.submitList(emptyList())
                }
                is ScheduleState.Error -> {
                    binding.swipeRefresh.isRefreshing = false
                    binding.progressBar.visibility = View.GONE
                    binding.layoutEmpty.visibility = View.GONE
                    binding.layoutError.visibility = View.VISIBLE
                    binding.tvErrorMessage.text = state.message
                }
            }
        }

        viewModel.chefs.observe(viewLifecycleOwner) { chefList ->
            chefs = chefList
            val chefNames = chefList.map { it.name }
            val arrayAdapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                chefNames
            )
            binding.actvChefFilter.setAdapter(arrayAdapter)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
