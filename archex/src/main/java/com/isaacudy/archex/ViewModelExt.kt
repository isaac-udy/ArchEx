package com.isaacudy.archex

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.ViewModel


open class LiveViewModel<T : Any>(initialState: T) : ViewModel(), LiveObject<T> {
    private val backing : MutableLive<T> = MutableLive(initialState)

    protected var state : T
        get() = backing.state
        set(value) {
            backing.state = value
        }

    override fun observe(owner: LifecycleOwner, observer: LiveObserver<T>) = backing.observe(owner, observer)
    override fun observeForever(observer: LiveObserver<T>)  = backing.observeForever(observer)
    override fun removeObserver(observer: LiveObserver<T>) = backing.removeObserver(observer)
    override fun removeObservers(owner: LifecycleOwner) = backing.removeObservers(owner)
    override fun hasObservers(): Boolean  = backing.hasObservers()
    override fun hasActiveObservers(): Boolean = backing.hasActiveObservers()
}