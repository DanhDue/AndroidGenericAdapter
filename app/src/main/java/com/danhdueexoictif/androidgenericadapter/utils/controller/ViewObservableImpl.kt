package com.danhdueexoictif.androidgenericadapter.utils.controller

import android.view.View

class ViewObservableImpl : ViewObservable {

    private val listEventControllers = mutableListOf<OnEventController>()
    private var message: Any? = null
    private var changed: Boolean = false
    private val MUTEX = Any()

    override fun register(obj: OnEventController?) {
        if (obj == null) throw NullPointerException("Null Observable")
        synchronized(MUTEX) {
            if (!listEventControllers.contains(obj)) listEventControllers.add(obj)
        }
    }

    override fun unregister(obj: OnEventController?) {
        synchronized(MUTEX) {
            listEventControllers.remove(obj)
        }
    }

    override fun notifyObservers(
        @OnEventController.Companion.EventDef eventType: Int, view: View?,
        data: Any?
    ) {
        var observersLocal: MutableList<OnEventController>?
        //synchronization is used to make sure any observer registered
        // after message is received is not notified
        synchronized(MUTEX) {
            if (!changed) return
            observersLocal = mutableListOf()
            observersLocal?.addAll(listEventControllers)
            changed = false
        }
        observersLocal?.let {
            for (obj in it) {
                obj.onEvent(eventType, view, data)
            }
        }
    }

    //method to post message to the topic
    override fun postMessage(
        @OnEventController.Companion.EventDef eventType: Int,
        msg: Any?
    ) {
        message = msg
        changed = true
        notifyObservers(eventType, null, msg)
    }
}
