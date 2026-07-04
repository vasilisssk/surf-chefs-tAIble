package com.example.chefstable.ui.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.chefstable.data.remote.dto.CookingClassDto
import com.example.chefstable.databinding.ItemCookingClassBinding
import java.text.SimpleDateFormat
import java.util.Locale

class ClassAdapter(
    private val onClassClick: (CookingClassDto) -> Unit
) : ListAdapter<CookingClassDto, ClassAdapter.ClassViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassViewHolder {
        val binding = ItemCookingClassBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ClassViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ClassViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ClassViewHolder(
        private val binding: ItemCookingClassBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onClassClick(getItem(position))
                }
            }
        }

        fun bind(cls: CookingClassDto) {
            binding.tvClassTitle.text = cls.title
            binding.tvClassDatetime.text = formatDate(cls.dateTime)
            binding.tvClassChef.text = "Шеф: ${cls.chef.name}"
            binding.tvClassSeats.text = "Свободно: ${cls.availableSeats}/${cls.maxParticipants}"
            binding.tvClassPrice.text = "${cls.price.toInt()} руб"
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
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CookingClassDto>() {
            override fun areItemsTheSame(oldItem: CookingClassDto, newItem: CookingClassDto): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: CookingClassDto, newItem: CookingClassDto): Boolean =
                oldItem == newItem
        }
    }
}
