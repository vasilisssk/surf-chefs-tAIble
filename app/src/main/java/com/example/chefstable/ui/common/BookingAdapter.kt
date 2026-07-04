package com.example.chefstable.ui.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chefstable.R
import com.example.chefstable.data.remote.dto.BookingDto
import com.example.chefstable.databinding.ItemBookingBinding
import java.text.SimpleDateFormat
import java.util.Locale

class BookingAdapter(
    private val onCancelClick: (BookingDto) -> Unit,
    private val onRateClick: (BookingDto) -> Unit,
    private val onBookingClick: (BookingDto) -> Unit = {}
) : ListAdapter<BookingDto, BookingAdapter.BookingViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookingViewHolder {
        val binding = ItemBookingBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BookingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BookingViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class BookingViewHolder(
        private val binding: ItemBookingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onBookingClick(getItem(position))
                }
            }
            binding.btnCancelBooking.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onCancelClick(getItem(position))
                }
            }
            binding.btnRateChef.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onRateClick(getItem(position))
                }
            }
        }

        fun bind(booking: BookingDto) {
            val cls = booking.cookingClass
            binding.tvBookingClassTitle.text = cls.title
            binding.tvBookingDate.text = formatDate(cls.dateTime)
            binding.tvBookingChef.text = "Шеф: ${cls.chef.name}"

            val statusText = when (booking.status) {
                "CONFIRMED" -> "Подтверждено"
                "CANCELLED_BY_CLIENT" -> "Отменено клиентом"
                "CANCELLED_BY_STUDIO" -> "Отменено студией"
                "ATTENDED" -> "Посещено"
                "NO_SHOW" -> "Неявка"
                else -> booking.status
            }
            binding.tvBookingStatus.text = statusText

            val statusColor = when (booking.status) {
                "CONFIRMED" -> R.color.status_confirmed
                "CANCELLED_BY_CLIENT", "CANCELLED_BY_STUDIO" -> R.color.status_cancelled
                "ATTENDED" -> R.color.status_attended
                "NO_SHOW" -> R.color.status_no_show
                else -> R.color.text_light
            }
            binding.tvBookingStatus.setTextColor(
                binding.root.context.getColor(statusColor)
            )

            when (booking.status) {
                "CONFIRMED" -> {
                    binding.btnCancelBooking.visibility = View.VISIBLE
                    binding.btnRateChef.visibility = View.GONE
                }
                "ATTENDED" -> {
                    binding.btnCancelBooking.visibility = View.GONE
                    binding.btnRateChef.visibility = View.VISIBLE
                }
                else -> {
                    binding.layoutBookingActions.visibility = View.GONE
                }
            }
        }

        private fun formatDate(isoDate: String): String {
            return try {
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
                val outputFormat = SimpleDateFormat("d MMMM, HH:mm", Locale("ru"))
                val date = inputFormat.parse(isoDate)
                outputFormat.format(date ?: return isoDate)
            } catch (e: Exception) {
                isoDate
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<BookingDto>() {
            override fun areItemsTheSame(oldItem: BookingDto, newItem: BookingDto): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: BookingDto, newItem: BookingDto): Boolean =
                oldItem == newItem
        }
    }
}
