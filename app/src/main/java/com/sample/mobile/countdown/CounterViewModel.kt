package com.sample.mobile.countdown

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposables
import java.util.concurrent.TimeUnit

internal class CounterViewModel : ViewModel() {
    private var disposable = Disposables.disposed()

    private val defaultState = State()
    private val mutableLiveData = MutableLiveData<State>()

    val state: LiveData<State> = mutableLiveData

    init {
        mutableLiveData.value = defaultState
        debug("init $this")
    }

    override fun onCleared() {
        super.onCleared()
        disposable.dispose()
        debug("onCleared $this")
    }

    fun triggerCounter() {
        val currentState = currentState()

        if (currentState.counterRunning) {
            stop()
        } else {
            start()
        }
    }

    private fun start() {
        mutableLiveData.value = currentState().copy(counterRunning = true)

        disposable = Observable
            .interval(1, TimeUnit.MILLISECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { millis ->
                val currentState = currentState()
                val totalMillis = currentState.millis + millis

                mutableLiveData.value = currentState.copy(millis = totalMillis)
            }
    }

    private fun stop() {
        disposable.dispose()
        mutableLiveData.value = currentState().copy(counterRunning = false)
    }

    private fun currentState(): State {
        return mutableLiveData.value ?: State()
    }

    fun reset() {
        disposable.dispose()
        mutableLiveData.value = defaultState
    }

    data class State(
        val counterRunning: Boolean = false,
        val millis: Long = 0L
    ) {
        val counter: String get() {
            val hours = TimeUnit.MILLISECONDS.toHours(millis)
            val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
            val seconds = TimeUnit.MILLISECONDS.toSeconds(millis)

            return String.format("%02d:%02d:%02d",
                hours,
                minutes - TimeUnit.HOURS.toMinutes(hours),
                seconds - TimeUnit.MINUTES.toSeconds(minutes)
            )
        }

        @get:StringRes
        val startButtonText: Int
            get() = if (counterRunning) {
                R.string.stop
            } else {
                R.string.start
            }
    }
}
