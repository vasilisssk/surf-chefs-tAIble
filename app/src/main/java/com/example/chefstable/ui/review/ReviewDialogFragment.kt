package com.example.chefstable.ui.review

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.LifecycleOwner
import com.example.chefstable.R
import com.example.chefstable.databinding.DialogReviewBinding
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReviewDialogFragment : DialogFragment() {

    private var _binding: DialogReviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ReviewViewModel by viewModel()

    private var bookingId: String? = null
    private var chefId: String? = null
    private var onReviewSubmitted: (() -> Unit)? = null

    companion object {
        fun newInstance(bookingId: String, chefId: String, onSubmitted: () -> Unit): ReviewDialogFragment {
            return ReviewDialogFragment().apply {
                this.bookingId = bookingId
                this.chefId = chefId
                this.onReviewSubmitted = onSubmitted
            }
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogReviewBinding.inflate(LayoutInflater.from(requireContext()))

        observeViewModel()

        binding.btnSubmit.setOnClickListener {
            val rating = binding.ratingBar.rating.toInt()
            val comment = binding.etComment.text?.toString()?.takeIf { it.isNotBlank() }
            val bid = bookingId ?: return@setOnClickListener
            val cid = chefId ?: return@setOnClickListener
            viewModel.submitReview(bid, cid, rating, comment)
        }

        return AlertDialog.Builder(requireContext())
            .setView(binding.root)
            .create()
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->
            binding.btnSubmit.isEnabled = !isLoading
            binding.progressBar.visibility = if (isLoading) android.view.View.VISIBLE else android.view.View.GONE
        }

        viewModel.errorMessage.observe(this) { message ->
            message?.let {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                viewModel.onErrorMessageShown()
            }
        }

        viewModel.success.observe(this) { success ->
            if (success) {
                onReviewSubmitted?.invoke()
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
