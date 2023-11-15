package com.example.project9

import androidx.recyclerview.widget.DiffUtil

class ImageDiffUtil : DiffUtil.ItemCallback<Image>() {
    override fun areItemsTheSame(oldItem: Image, newItem: Image)
            = (oldItem.imageId == newItem.imageId)
    override fun areContentsTheSame(oldItem: Image, newItem: Image) = (oldItem == newItem)
}