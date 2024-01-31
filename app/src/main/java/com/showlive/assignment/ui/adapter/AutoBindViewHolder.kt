package com.showlive.assignment.ui.adapter

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class AutoBindViewHolder<T, V : HolderEvent>(
    containerView: View,
    holderEvent: HolderEvent
) : RecyclerView.ViewHolder(containerView) {

    @Suppress("UNCHECKED_CAST")
    val event = holderEvent as V
    val context: Context? = itemView.context

    abstract fun bind(item: T)
}
