package com.showlive.assignment.ui.favorite

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.showlive.assignment.data.db.FavoriteDatabase
import com.showlive.assignment.data.entity.Character
import com.showlive.assignment.ui.base.BaseViewModel
import com.showlive.assignment.ui.util.livedata.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@HiltViewModel
class FavoriteViewModel @Inject constructor(
    private val database: FavoriteDatabase
): BaseViewModel() {
    private val _items = MutableLiveData<ArrayList<Character>>(arrayListOf())
    val items: LiveData<ArrayList<Character>> get() = _items

    fun getAllFavoriteItems() {
        compositeDisposable += database.favoriteDao().getAll()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _isLoading.value = Event(true) }
            .subscribe ({
                _isLoading.value = Event(false)
                _items.value = _items.value?.apply {
                    clear()
                    addAll(it)
                }
            }, { e ->
                e.printStackTrace()
                _isLoading.value = Event(false)
            })
    }

    fun deleteFavoriteItem(item: Character) {
        compositeDisposable += database.favoriteDao().delete(item)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe ({
                _items.value = _items.value?.apply {
                    remove(item)
                }
            }, { e ->
                e.printStackTrace()
                _showToast.value = Event("캐릭터를 삭제하지 못했습니다.")
            })
    }
}