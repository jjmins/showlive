package com.showlive.assignment.ui.character

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.showlive.assignment.data.entity.CharacterItem
import com.showlive.assignment.databinding.LoadingItemBinding
import com.showlive.assignment.ui.adapter.AutoBindViewHolder
import com.showlive.assignment.ui.adapter.HolderEvent

class LoadingViewHolder(
    binding: LoadingItemBinding,
    event: HolderEvent,
) : AutoBindViewHolder<CharacterItem, HolderEvent>(binding.root, event) {

    override fun bind(item: CharacterItem) {
        // do nothing
    }

    companion object {
        val CREATOR: (ViewGroup, HolderEvent) -> LoadingViewHolder = { parent, event ->
            val binding = LoadingItemBinding.inflate(LayoutInflater.from(parent.context))
            LoadingViewHolder(binding, event)
        }

        val DIFF: DiffUtil.ItemCallback<CharacterItem> = object : DiffUtil.ItemCallback<CharacterItem>() {
            override fun areItemsTheSame(oldItem: CharacterItem, newItem: CharacterItem): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(oldItem: CharacterItem, newItem: CharacterItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}