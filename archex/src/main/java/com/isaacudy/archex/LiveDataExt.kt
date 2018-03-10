package com.isaacudy.archex

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer
import android.os.Handler
import android.os.Looper

interface LiveObject<out T : Any> {
    fun observe(owner: LifecycleOwner, observer: LiveObserver<T>)
    fun observeForever(observer: LiveObserver<T>)
    fun removeObserver(observer: LiveObserver<T>)
    fun removeObservers(owner: LifecycleOwner)
    fun hasObservers(): Boolean
    fun hasActiveObservers(): Boolean
}

sealed class Live<T : Any>(initialState: T) : LiveObject<T> {
    internal val backing = MutableLiveData<T>().apply { value = initialState }

    open val state get() = backing.value!!

    private val observeForeverMap = mutableMapOf<LiveObserver<T>, Observer<T>>()

    override fun observe(owner: LifecycleOwner, observer: LiveObserver<T>) {
        backing.observe(owner, createObserver(observer))
    }

    override fun observeForever(observer: LiveObserver<T>) {
        val forever = createObserver(observer)
        observeForeverMap[observer] = forever
        backing.observeForever(forever)
    }

    override fun removeObserver(observer: LiveObserver<T>) {
        val forever = observeForeverMap[observer] ?: return
        observeForeverMap.remove(observer)
        backing.removeObserver(forever)
    }

    override fun removeObservers(owner: LifecycleOwner) = backing.removeObservers(owner)

    override fun hasObservers(): Boolean = backing.hasObservers()

    override fun hasActiveObservers(): Boolean = backing.hasActiveObservers()

    private fun createObserver(observer: LiveObserver<T>): Observer<T> {
        return Observer { t ->
            if (t == null) return@Observer
            observer(t)
        }
    }
}

class MutableLive<T : Any>(initialState: T) : Live<T>(initialState) {
    private val handler = Handler(Looper.getMainLooper())

    override var state
        get() = backing.value!!
        set(value) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                backing.value = value
            }
            else {
                throw RuntimeException()
            }
        }

    fun post(state: T) {
        handler.post {
            this.state = state
        }
    }
}

open class LiveEvent<T : Any> : LiveObject<T> {
    protected val backing = MutableLiveData<T>()

    private val observeForeverMap = mutableMapOf<LiveObserver<T>, Observer<T>>()

    override fun observe(owner: LifecycleOwner, observer: LiveObserver<T>) {
        backing.observe(owner, createObserver(observer))
    }

    override fun observeForever(observer: LiveObserver<T>) {
        val forever = createObserver(observer)
        observeForeverMap[observer] = forever
        backing.observeForever(forever)
    }

    override fun removeObserver(observer: LiveObserver<T>) {
        val forever = observeForeverMap[observer] ?: return
        observeForeverMap.remove(observer)
        backing.removeObserver(forever)
    }

    override fun removeObservers(owner: LifecycleOwner) = backing.removeObservers(owner)

    override fun hasObservers(): Boolean = backing.hasObservers()

    override fun hasActiveObservers(): Boolean = backing.hasActiveObservers()

    private fun createObserver(observer: LiveObserver<T>): Observer<T> {
        return Observer { t ->
            if (t == null) return@Observer
            observer(t)
        }
    }
}

class MutableLiveEvent<T :Any> : LiveEvent<T>() {
    private val handler = Handler(Looper.getMainLooper())

    fun set(state: T){
        backing.value = state
        backing.value = null
    }

    fun post(state: T) {
        handler.post {
            backing.value = state
            backing.value = null
        }
    }
}

typealias LiveObserver<T> = (T) -> Unit