package com.example.project9

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.project9.R
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.project9.databinding.ImageItemBinding
import kotlin.reflect.KFunction1
import com.bumptech.glide.Glide

class ImageAdapter (
    private val clickListener: KFunction1<Image, Unit>
    ) : ListAdapter<Image, ImageAdapter.ImageItemViewHolder>(ImageDiffUtil()) {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageItemViewHolder {
            return ImageItemViewHolder.inflateFrom(parent)
        }

        override fun onBindViewHolder(holder: ImageItemViewHolder, position: Int) {
            val item = getItem(position)
            holder.bind(item, clickListener)
        }

        class ImageItemViewHolder(private val binding: ImageItemBinding) :
            RecyclerView.ViewHolder(binding.root) {

            companion object {

                fun inflateFrom(parent: ViewGroup): ImageItemViewHolder {
                    val layoutInflater = LayoutInflater.from(parent.context)
                    val binding = ImageItemBinding.inflate(layoutInflater, parent, false)
                    return ImageItemViewHolder(binding)
                }

            }

            fun bind( // for each saved image, setup click listeners so can view
                item: Image,
                clickListener: KFunction1<Image, Unit>
            ) {
                //set each item image with glide
                Glide.with(binding.root)
                    .load(item.url)
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(binding.imageSrc)

                binding.root.setOnClickListener {
                    clickListener(item)
                }
            }

            }
}