package com.showlive.assignment.ui.character

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.bumptech.glide.Glide
import com.jakewharton.rxbinding4.view.clicks
import com.showlive.assignment.R
import com.showlive.assignment.data.entity.Character
import com.showlive.assignment.databinding.CharacterItemBinding
import com.showlive.assignment.ui.adapter.AutoBindViewHolder
import com.showlive.assignment.ui.adapter.HolderEvent
import java.util.concurrent.TimeUnit

class CharacterViewHolder(
    private val binding: CharacterItemBinding,
    event: HolderEvent
) : AutoBindViewHolder<Character, CharacterViewHolder.Event>(binding.root, event) {

    interface Event : HolderEvent {
        fun onClickItem(item: Character, position: Int)
    }

    override fun bind(item: Character) {
        with(binding) {
            card.setCardBackgroundColor(if (item.isFavorite) context!!.resources.getColor(R.color.teal_700) else context!!.resources.getColor(R.color.white))
            Glide.with(context).load(item.thumbnailUrl).into(ivCharacter)
            tvTitle.text = item.name
            tvDescription.text = item.description

            root.clicks().throttleFirst(500, TimeUnit.MICROSECONDS).subscribe {
                event.onClickItem(item, adapterPosition)
            }
        }
    }

    companion object {
        val CREATOR: (ViewGroup, HolderEvent) -> CharacterViewHolder = { parent, event ->
            val view = CharacterItemBinding.inflate(LayoutInflater.from(parent.context))
            CharacterViewHolder(view, event)
        }

        val DIFF = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Character, newItem: Character): Boolean {
                return oldItem == newItem
            }
        }
    }
}