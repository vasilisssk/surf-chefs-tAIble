package com.example.chefstable.ui.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.chefstable.MainActivity
import com.example.chefstable.R
import com.example.chefstable.databinding.ActivityRegistrationBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistrationBinding
    private val viewModel: RegistrationViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
        observeViewModel()
    }

    private fun setupListeners() {
        binding.btnRegister.setOnClickListener {
            val firstName = binding.etFirstName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val phone = binding.etPhone.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            viewModel.register(firstName, email, phone, password)
        }

        binding.tvLoginLink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.btnRegister.isEnabled = !isLoading
            binding.btnRegister.text = if (isLoading) getString(R.string.loading) else getString(R.string.reg_button)
            binding.etFirstName.isEnabled = !isLoading
            binding.etEmail.isEnabled = !isLoading
            binding.etPhone.isEnabled = !isLoading
            binding.etPassword.isEnabled = !isLoading
        }

        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                viewModel.onErrorMessageShown()
            }
        }

        viewModel.fieldErrors.observe(this) { errors ->
            errors["firstName"]?.let { binding.tilFirstName.error = it } ?: run { binding.tilFirstName.error = null }
            errors["email"]?.let { binding.tilEmail.error = it } ?: run { binding.tilEmail.error = null }
            errors["phone"]?.let { binding.tilPhone.error = it } ?: run { binding.tilPhone.error = null }
            errors["password"]?.let { binding.tilPassword.error = it } ?: run { binding.tilPassword.error = null }
        }

        viewModel.registrationSuccess.observe(this) { success ->
            if (success) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}
