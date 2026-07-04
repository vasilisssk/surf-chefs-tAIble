package com.example.chefstable.ui.profile

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.chefstable.R
import com.example.chefstable.databinding.FragmentProfileBinding
import com.example.chefstable.ui.auth.LoginActivity
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()
        viewModel.loadProfile()
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener {
            val firstName = binding.etFirstName.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val allergies = binding.etAllergies.text?.toString()?.trim()?.takeIf { it.isNotEmpty() }
            viewModel.saveProfile(firstName, phone, allergies)
        }

        binding.btnLogout.setOnClickListener {
            showLogoutDialog()
        }

        binding.btnRetry.setOnClickListener {
            viewModel.loadProfile()
        }
    }

    private fun showLogoutDialog() {
        AlertDialog.Builder(requireContext())
            .setMessage(R.string.profile_logout_confirm)
            .setPositiveButton(R.string.profile_logout) { _, _ ->
                viewModel.logout()
            }
            .setNegativeButton(R.string.back, null)
            .show()
    }

    private fun observeViewModel() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                is ProfileState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.scrollContent.visibility = View.GONE
                    binding.layoutError.visibility = View.GONE
                }
                is ProfileState.Content -> {
                    binding.progressBar.visibility = View.GONE
                    binding.scrollContent.visibility = View.VISIBLE
                    binding.layoutError.visibility = View.GONE

                    val client = state.client
                    binding.etFirstName.setText(client.firstName)
                    binding.etEmail.setText(client.email)
                    binding.etPhone.setText(client.phone)
                    binding.etAllergies.setText(client.allergies ?: "")

                    binding.tvPermanentClient.text = getString(
                        R.string.profile_permanent_client,
                        if (client.isPermanentClient) getString(R.string.profile_yes) else getString(R.string.profile_no)
                    )
                    binding.tvClassesAttended.text = getString(
                        R.string.profile_classes_attended,
                        client.totalClassesAttended
                    )
                }
                is ProfileState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.scrollContent.visibility = View.GONE
                    binding.layoutError.visibility = View.VISIBLE
                    binding.tvErrorMessage.text = state.message
                }
            }
        }

        viewModel.isSaving.observe(viewLifecycleOwner) { isSaving ->
            binding.btnSave.isEnabled = !isSaving
            binding.btnSave.text = if (isSaving)
                getString(R.string.loading)
            else
                getString(R.string.profile_save)
        }

        viewModel.message.observe(viewLifecycleOwner) { message ->
            message?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                viewModel.onMessageShown()
            }
        }

        viewModel.logoutEvent.observe(viewLifecycleOwner) { shouldLogout ->
            if (shouldLogout) {
                viewModel.onLogoutEventHandled()
                val intent = Intent(requireContext(), LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                requireActivity().finish()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
