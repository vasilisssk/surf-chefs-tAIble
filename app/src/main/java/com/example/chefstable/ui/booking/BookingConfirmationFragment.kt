package com.example.chefstable.ui.booking

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.chefstable.R
import com.example.chefstable.data.remote.dto.RentalPackageDto
import com.example.chefstable.databinding.FragmentBookingBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class BookingConfirmationFragment : Fragment() {

    private var _binding: FragmentBookingBinding? = null
    private val binding get() = _binding!!

    private val viewModel: BookingViewModel by viewModel()

    private var classId: String? = null
    private var classPrice: Double = 0.0
    private var rentalPackages: List<RentalPackageDto> = emptyList()
    private var selectedPackage: RentalPackageDto? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBookingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadArguments()
        setupListeners()
        observeViewModel()
        viewModel.loadRentalPackages()
        viewModel.loadProfileAllergies()
    }

    private fun loadArguments() {
        classId = arguments?.getString("classId")
        classPrice = arguments?.getDouble("classPrice", 0.0) ?: 0.0

        binding.tvClassTitle.text = arguments?.getString("classTitle") ?: ""
        binding.tvClassDatetime.text = formatDate(arguments?.getString("classDateTime") ?: "")
        binding.tvClassChef.text = "Шеф: ${arguments?.getString("chefName") ?: ""}"
        binding.tvClassPrice.text = "${classPrice.toInt()} руб"
        updateTotal()
    }

    private fun setupListeners() {
        binding.actvRentalPackage.setOnItemClickListener { _, _, position, _ ->
            if (position < rentalPackages.size) {
                selectedPackage = rentalPackages[position]
                val pkg = selectedPackage!!
                binding.tvPackagePrice.visibility = View.VISIBLE
                binding.tvPackagePrice.text = "+${pkg.price.toInt()} руб"
                updateTotal()
            }
        }

        binding.btnConfirm.setOnClickListener {
            val cid = classId ?: return@setOnClickListener
            val pkgId = selectedPackage?.id
                ?: rentalPackages.firstOrNull()?.id
                ?: run {
                    Snackbar.make(binding.root, "Выберите пакет проката", Snackbar.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            val allergies = binding.etAllergies.text?.toString()?.takeIf { it.isNotBlank() }
            viewModel.createBooking(cid, pkgId, allergies)
        }
    }

    private fun updateTotal() {
        val total = classPrice + (selectedPackage?.price ?: 0.0)
        binding.tvTotal.text = getString(R.string.booking_total, total.toInt().toString())
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.btnConfirm.isEnabled = !isLoading
            binding.btnConfirm.text = if (isLoading)
                getString(R.string.loading)
            else
                getString(R.string.booking_confirm)
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                viewModel.onErrorMessageShown()
            }
        }

        viewModel.bookingSuccess.observe(viewLifecycleOwner) { booking ->
            if (booking != null) {
                Snackbar.make(binding.root, getString(R.string.booking_success), Snackbar.LENGTH_LONG).show()
                viewModel.onBookingSuccessHandled()
                findNavController().popBackStack(R.id.navigation_home, false)
            }
        }

        viewModel.allergies.observe(viewLifecycleOwner) { allergies ->
            if (!allergies.isNullOrEmpty()) {
                binding.etAllergies.setText(allergies)
            }
        }

        viewModel.rentalPackages.observe(viewLifecycleOwner) { packages ->
            rentalPackages = packages
            val packageNames = packages.map { "${it.packageName} (+${it.price.toInt()} руб)" }
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                packageNames
            )
            binding.actvRentalPackage.setAdapter(adapter)

            if (packages.isNotEmpty()) {
                binding.actvRentalPackage.setText(packageNames[0], false)
                selectedPackage = packages[0]
                binding.tvPackagePrice.visibility = View.VISIBLE
                binding.tvPackagePrice.text = "+${packages[0].price.toInt()} руб"
                updateTotal()
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
