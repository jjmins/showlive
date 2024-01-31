package com.showlive.assignment.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.showlive.assignment.data.db.FavoriteDatabase
import com.showlive.assignment.data.entity.Character
import com.showlive.assignment.data.entity.CharacterItem
import com.showlive.assignment.data.entity.Loading
import com.showlive.assignment.data.repository.MarvelRepository
import com.showlive.assignment.ui.base.BaseViewModel
import com.showlive.assignment.ui.util.livedata.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject


@HiltViewModel
class SearchViewModel @Inject constructor(
    private val repository: MarvelRepository,
    private val database: FavoriteDatabase
) : BaseViewModel() {
    private val searchSubject: BehaviorSubject<String> = BehaviorSubject.create()

    private val _items = MutableLiveData<ArrayList<CharacterItem>>(arrayListOf())
    val items: LiveData<ArrayList<CharacterItem>> get() = _items

    private var offset = 0
    private var limit = 0
    private var total = 0
    var isPageable = true

    fun updateSearch(text: String) {
        searchSubject.onNext(text)
    }

    fun loadCharacters() {
        compositeDisposable += searchSubject
            .debounce(300, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext {
                offset = 0
                limit = 0
                total = 0
                isPageable = true
                _isLoading.value = Event(true)
            }
            .switchMapSingle { searchString ->
                if (searchString.length < 2) {
                    _isLoading.value = Event(false)
                    _items.value = _items.value?.apply {
                        clear()
                    }
                    Single.never()
                } else {
                    repository.getCharacters(searchString)
                        .doFinally { _isLoading.value = Event(false) }
                }
            }
            .subscribe({
                offset = it.offset + it.limit
                limit = it.limit
                total = it.total
                _items.value = _items.value?.apply {
                    clear()
                    addAll(it.results)
                }
            }, { e ->
                e.printStackTrace()
                _showToast.value = Event("서버에 문제가 발생하였습니다.")
            })
    }

    fun loadMoreCharacter() {
        isPageable = false
        if (total <= offset) return
        compositeDisposable += repository.getCharacters(searchSubject.value!!, offset, limit)
            .doOnSubscribe {
                _items.value = _items.value?.apply {
                    add(Loading)
                }
            }
            .doFinally {
                _items.value = _items.value?.apply {
                    remove(Loading)
                }
                isPageable = true
            }
            .subscribe({
                offset = it.offset + it.limit
                _items.value = _items.value?.apply {
                    addAll(it.results)
                }
            }, { e ->
                e.printStackTrace()
                _showToast.value = Event("서버에 문제가 발생하였습니다.")
            })
    }

    fun saveFavoriteItem(item: Character, position: Int) {
        val saveItem = item.copy(isFavorite = true, timestamp = System.currentTimeMillis())

        compositeDisposable += database.favoriteDao().canNotSaveCharacter(item.id)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMapCompletable { alreadySaved ->
                if (alreadySaved) {
                    _showToast.value = Event("이미 저장되었습니다.")
                    Completable.complete()
                } else {
                    deleteOldestItem().mergeWith(database.favoriteDao().insert(saveItem))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                }
            }
            .subscribe({
                _items.value = _items.value?.apply {
                    this[position] = saveItem
                }
            }, { e ->
                e.printStackTrace()
                _showToast.value = Event("캐릭터를 저장하지 못했습니다.")
            })
    }
    private fun deleteOldestItem(): Completable {
        return database.favoriteDao().areCharacterThan5()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .flatMapCompletable { charactersExceedLimit ->
                if (charactersExceedLimit) {
                    database.favoriteDao().deleteOldItem()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe()
                }
                Completable.complete()
            }
    }

    fun deleteFavoriteItem(item: Character, position: Int) {
        val saveItem = item.copy(isFavorite = false)
        compositeDisposable += database.favoriteDao().delete(saveItem)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                _items.value = _items.value?.apply {
                    this[position] = saveItem
                }
            }, { e ->
                e.printStackTrace()
                _showToast.value = Event("캐릭터를 삭제하지 못했습니다.")
                _items.value = _items.value?.apply {
                    this[position] =
                        this.filterIsInstance(Character::class.java)[position].copy(isFavorite = true)
                }
            })
    }
}