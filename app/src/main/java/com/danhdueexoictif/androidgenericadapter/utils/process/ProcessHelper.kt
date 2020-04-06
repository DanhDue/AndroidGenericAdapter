package com.danhdueexoictif.androidgenericadapter.utils.process

import android.content.Context
import android.content.Intent

interface ProcessHelper {
    fun triggerRebirth(context: Context)
    fun triggerRebirth(context: Context, vararg nextIntents: Intent)
    fun isPhoenixProcess(context: Context): Boolean
}
