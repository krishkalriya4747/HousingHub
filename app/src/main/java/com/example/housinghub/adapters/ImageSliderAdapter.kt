package com.example.housinghub.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.housinghub.databinding.ItemImagePreviewBinding

class ImageSliderAdapter(
    private val onImageClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<ImageSliderAdapter.ImageViewHolder>() {

    private val images = mutableListOf<String>()

    inner class ImageViewHolder(private val binding: ItemImagePreviewBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(imageUri: String, position: Int) {
            Glide.with(binding.root.context)
                .load(imageUri)
                .centerCrop()
                .into(binding.imageView)

            binding.root.setOnClickListener { onImageClick(position) }
            binding.btnDelete.setOnClickListener { onDeleteClick(position) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val binding = ItemImagePreviewBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(images[position], position)
    }

    override fun getItemCount(): Int = images.size

    fun submitList(list: List<String>) {
        images.clear()
        images.addAll(list)
        notifyDataSetChanged()
    }
}

