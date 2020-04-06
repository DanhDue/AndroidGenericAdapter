package com.danhdueexoictif.androidgenericadapter.utils.controller

import androidx.fragment.app.Fragment

class BackPressImpl(private val parentFragment: Fragment?) : OnBackPressListener {

    override fun onBackPressed(): Boolean {

        if (parentFragment == null) return false

        val childCount = parentFragment.childFragmentManager.backStackEntryCount

        if (childCount == 0) {
            // it has no child Fragment
            // can not handle the onBackPressed task by itself
            return false

        } else {
            // get the child Fragment
            val childFragmentManager = parentFragment.childFragmentManager

            // propagate onBackPressed method call to the child Fragment
            if (childFragmentManager.fragments.size > 0) {
                val childFragment =
                    childFragmentManager.fragments[0] as OnBackPressListener
                if (!childFragment.onBackPressed()) {
                    // child Fragment was unable to handle the task
                    // It could happen when the child Fragment is last last leaf of a chain
                    // removing the child Fragment from stack
                    childFragmentManager.popBackStackImmediate()

                }
            }

            // either this Fragment or its child handled the task
            // either way we are successful and done here
            return true
        }
    }
}
