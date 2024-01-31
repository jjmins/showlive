package com.showlive.assignment.ui.util.livedata

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer

inline fun <T> Fragment.observe(liveData: LiveData<T>, crossinline runnable: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner, Observer {
        runnable.invoke(it)
    })
}

inline fun <T> Fragment.event(liveData: LiveData<Event<T>>, crossinline runnable: (T) -> Unit) {
    liveData.observe(viewLifecycleOwner, EventObserver {
        runnable.invoke(it)
    })
}

inline fun <T> AppCompatActivity.observe(liveData: LiveData<T>, crossinline runnable: (T) -> Unit) {
    liveData.observe(this, Observer {
        runnable.invoke(it)
    })
}

inline fun <T> AppCompatActivity.event(
    liveData: LiveData<Event<T>>,
    crossinline runnable: (T) -> Unit
) {
    liveData.observe(this, EventObserver {
        runnable.invoke(it)
    })
}