package com.danhdueexoictif.androidgenericadapter.utils.sharedpreference

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers

@Suppress("UNCHECKED_CAST")
class LivePreference<T> constructor(
    private val updates: Observable<String>,
    private val preferences: SharedPreferences,
    private val key: String,
    private val defaultValue: T
) : MutableLiveData<T>() {

    private var firstSet = true
    private var disposable: Disposable? = null

    override fun onActive() {
        super.onActive()
        // Prevent setValue is recalled when onPause() -> onResume...
        if (firstSet) {
            value = (preferences.all[key] as T) ?: defaultValue
            firstSet = false
        }
        // Observe when SharedPreference value is changed.
        disposable = updates.filter { t -> t == key }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeWith(object : DisposableObserver<String>() {
                override fun onComplete() {

                }

                override fun onNext(t: String) {
                    postValue((preferences.all[t] as T) ?: defaultValue)
                }

                override fun onError(e: Throwable) {

                }
            })
    }

    override fun onInactive() {
        super.onInactive()
        disposable?.dispose()
    }
}
