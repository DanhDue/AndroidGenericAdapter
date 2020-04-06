package com.danhdueexoictif.androidgenericadapter.utils.extension

import android.view.View
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.ScaleAnimation

/**
 * Make a View Blink for a desired duration
 *
 * @param T view that will be animated
 * @param duration for how long in ms will it blink
 * @param offset   start offset of the animation
 * @return returns the same view with animation properties
 */
fun <T : View> T.makeMeBlink(duration: Long, offset: Int): T? {
    val anim: Animation = AlphaAnimation(0.0f, 1.0f)
    anim.duration = duration.toLong()
    anim.startOffset = offset.toLong()
    anim.repeatMode = Animation.REVERSE
    anim.repeatCount = Animation.INFINITE
    this.startAnimation(anim)
    return this
}

/**
 * Make a View scale up for a desired duration
 *
 * @param T view that will be animated
 * @param duration for how long in ms will it blink
 * @param offset   start offset of the animation
 * @return returns the same view with animation properties
 */
fun <T : View> T.makeScaleUp(duration: Long, offset: Long): T? {
    val anim: Animation = ScaleAnimation(0.5f, 1.0f, 0.5f, 1.0f)
    anim.duration = duration
    anim.startOffset = offset
    anim.repeatMode = Animation.RESTART
    anim.repeatCount = Animation.INFINITE
    this.startAnimation(anim)
    return this
}

/**
 * Make a View fade out for a desired duration
 *
 * @param T view that will be animated
 * @param duration for how long in ms will it blink
 * @param offset   start offset of the animation
 * @return returns the same view with animation properties
 */
fun <T : View> T.makeFadeOut(duration: Long, offset: Long): T? {
    val anim: Animation = AlphaAnimation(1.0f, 0f)
    anim.duration = duration
    anim.startOffset = offset
    anim.repeatMode = Animation.RESTART
    anim.repeatCount = Animation.INFINITE
    this.startAnimation(anim)
    return this
}
