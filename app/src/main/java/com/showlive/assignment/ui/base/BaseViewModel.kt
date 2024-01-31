package com.showlive.assignment.ui.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.showlive.assignment.ui.util.livedata.Event
import io.reactivex.disposables.CompositeDisposable

abstract class BaseViewModel : ViewModel() {
    protected val _isLoading = MutableLiveData(Event(false))
    val isLoading: LiveData<Event<Boolean>> get() = _isLoading

    protected val _showToast = MutableLiveData<Event<String>>()
    val showToast: LiveData<Event<String>> get() = _showToast

    protected val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.dispose()
    }
}