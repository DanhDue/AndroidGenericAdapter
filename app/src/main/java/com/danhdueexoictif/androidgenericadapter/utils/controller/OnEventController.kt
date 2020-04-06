package com.danhdueexoictif.androidgenericadapter.utils.controller

import android.view.View
import androidx.annotation.IntDef

interface OnEventController {

    /**
     * Send data and Handle Event in App
     *
     * @param eventType
     * @param view
     * @param data
     */
    fun onEvent(@EventDef eventType: Int, view: View?, data: Any?)

    /**
     * Send data and Handle Event in App for the method need to return a boolean.
     * This method is also used for Android DataBinding methods which needed a boolean returning such as: OnLongClick()
     *
     * @param eventType
     * @param view
     * @param data
     * @return : true(default is true => this view handle long click event and otherwise)
     */
    fun onBooleanEvent(@EventDef eventType: Int, view: View?, data: Any?): Boolean? = true

    companion object {
        const val SHOW_ERROR_DIALOG = 999999
        const val SHOW_UPGRADE_REQUIRED_DIALOG = 999998
        const val ITEM_NEWBIE_CLICKED = 999997

        @IntDef(
            SHOW_ERROR_DIALOG,
            SHOW_UPGRADE_REQUIRED_DIALOG,
            ITEM_NEWBIE_CLICKED
        )
        @Retention(AnnotationRetention.SOURCE)
        annotation class EventDef
    }

}
