package com.showlive.assignment.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import kotlin.reflect.KClass

class AutoBindHolderFactory<S : Any> {
    private val creatorMap: MutableMap<Int, (ViewGroup, HolderEvent) -> AutoBindViewHolder<*, *>> = mutableMapOf()
    private val eventMap = mutableMapOf<Int, HolderEvent>()
    private val diffMap = mutableMapOf<KClass<out S>, DiffUtil.ItemCallback<out S>>()
    private val typeMap = mutableMapOf<KClass<out S>, Int>()

    fun <T : S> add(
        clazz: KClass<out S>,
        diffCallback: DiffUtil.ItemCallback<T>,
        event: HolderEvent,
        creator: (ViewGroup, HolderEvent) -> AutoBindViewHolder<T, *>
    ): AutoBindHolderFactory<S> {

        val type = clazz.hashCode()

        creatorMap[type] = creator
        eventMap[type] = event
        diffMap[clazz] = diffCallback
        typeMap[clazz] = type

        return this
    }

    @Suppress("UNCHECKED_CAST")
    fun getDiffUtilCallback(clazz: KClass<out S>): DiffUtil.ItemCallback<S>? {
        return diffMap[clazz] as? DiffUtil.ItemCallback<S>
    }

    fun getItemType(clazz: KClass<out S>): Int {
        return typeMap[clazz] ?: 0
    }

    fun createViewHolder(type: Int, parent: ViewGroup): AutoBindViewHolder<*, *> {
        return creatorMap[type]!!.invoke(parent, eventMap[type]!!)
    }
}

fun <T : Any> AutoBindHolderFactory<T>.buildAdapter(): AutoBindListAdapter<T> {
    return AutoBindListAdapter(this)
}
