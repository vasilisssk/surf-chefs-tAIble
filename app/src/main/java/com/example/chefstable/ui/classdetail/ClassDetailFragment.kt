package com.example.chefstable.ui.classdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chefstable.R
import com.example.chefstable.databinding.FragmentClassDetailBinding
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class ClassDetailFragment : Fragment() {

    private var _binding: FragmentClassDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ClassDetailViewModel by viewModel()

    private var classId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentClassDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        classId = arguments?.getString("classId")
        setupListeners()
        observeViewModel()
        classId?.let { viewModel.loadClass(it) }
    }

    private fun setupListeners() {
        binding.btnBook.setOnClickListener {
            val cid = classId ?: return@setOnClickListener
            val state = viewModel.state.value
            if (state is ClassDetailState.Content) {
                val cls = state.classData
                findNavController().navigate(
                    R.id.navigation_booking_confirmation,
                    bundleOf(
                        "classId" to cls.id,
                        "classTitle" to cls.title,
                        "classDateTime" to cls.dateTime,
                        "chefName" to cls.chef.name,
                        "classPrice" to cls.price
                    )
                )
            }
        }

        binding.btnRetry.setOnClickListener {
            classId?.let { viewModel.loadClass(it) }
        }
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ClassDetailState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.scrollContent.visibility = View.GONE
                    binding.layoutError.visibility = View.GONE
                    binding.layoutNotFound.visibility = View.GONE
                }
                is ClassDetailState.Content -> {
                    binding.progressBar.visibility = View.GONE
                    binding.scrollContent.visibility = View.VISIBLE
                    binding.layoutError.visibility = View.GONE
                    binding.layoutNotFound.visibility = View.GONE

                    val cls = state.classData
                    binding.tvClassTitle.text = cls.title
                    binding.tvClassDatetime.text = formatDate(cls.dateTime)
                    binding.tvClassDuration.text = getString(R.string.class_detail_duration, cls.duration)
                    binding.tvClassSeats.text = getString(
                        R.string.schedule_available_seats,
                        cls.availableSeats,
                        cls.maxParticipants
                    )
                    binding.tvClassDescription.text = cls.description
                    binding.tvClassPrice.text = "${cls.price.toInt()} руб"

                    binding.tvChefName.text = "Шеф: ${cls.chef.name}"
                    binding.tvChefSpecialization.text = cls.chef.specialization
                    binding.tvChefRating.text = "Рейтинг: ${cls.chef.rating} (${cls.chef.totalReviews} отзывов)"
                    cls.chef.bio?.let { binding.tvChefBio.text = it }

                    if (cls.availableSeats <= 0) {
                        binding.btnBook.text = getString(R.string.class_detail_no_seats)
                        binding.btnBook.isEnabled = false
                    }
                }
                is ClassDetailState.NotFound -> {
                    binding.progressBar.visibility = View.GONE
                    binding.scrollContent.visibility = View.GONE
                    binding.layoutError.visibility = View.GONE
                    binding.layoutNotFound.visibility = View.VISIBLE
                }
                is ClassDetailState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.scrollContent.visibility = View.GONE
                    binding.layoutError.visibility = View.VISIBLE
                    binding.layoutNotFound.visibility = View.GONE
                    binding.tvErrorMessage.text = state.message
                }
            }
        }
    }

    private fun formatDate(isoDate: String): String {
        return try {
            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
            val outputFormat = SimpleDateFormat("d MMMM yyyy, HH:mm", Locale("ru"))
            val date = inputFormat.parse(isoDate)
            outputFormat.format(date ?: return isoDate)
        } catch (e: Exception) {
            isoDate
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
