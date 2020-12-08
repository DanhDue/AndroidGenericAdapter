package com.danhdueexoictif.androidgenericadapter.ui.base.mvi

interface IntentProcessor<in I : ViewIntent, out A : ViewAction> {
    fun intentToAction(intent: I): A
}
