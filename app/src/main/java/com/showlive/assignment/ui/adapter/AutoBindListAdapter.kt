package com.showlive.assignment.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

open class AutoBindListAdapter<S : Any>(
    private val bindHolderFactory: AutoBindHolderFactory<S>
) : ListAdapter<S, AutoBindViewHolder<in S, in HolderEvent>>(
    object : DiffUtil.ItemCallback<S>() {
        override fun areItemsTheSame(oldItem: S, newItem: S): Boolean {
            if (oldItem::class != newItem::class) return false
            return bindHolderFactory.getDiffUtilCallback(newItem::class)
                ?.areItemsTheSame(oldItem, newItem) ?: false
        }

        override fun areContentsTheSame(oldItem: S, newItem: S): Boolean {
            if (oldItem::class != newItem::class) return false
            return bindHolderFactory.getDiffUtilCallback(newItem::class)
                ?.areContentsTheSame(oldItem, newItem) ?: false
        }
    }
) {
    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AutoBindViewHolder<in S, in HolderEvent> {
        return bindHolderFactory.createViewHolder(
            viewType,
            parent
        ) as AutoBindViewHolder<in S, in HolderEvent>
    }

    override fun onBindViewHolder(
        holder: AutoBindViewHolder<in S, in HolderEvent>,
        position: Int
    ) {
        holder.bind(getItem(position))
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        return bindHolderFactory.getItemType(item::class)
    }
}
