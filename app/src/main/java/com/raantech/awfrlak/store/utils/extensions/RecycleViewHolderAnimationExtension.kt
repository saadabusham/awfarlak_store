package com.raantech.awfrlak.store.utils.extensions

import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.ScaleAnimation
import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.ViewHolder.setPopUpAnimation(
    position: Int,
    lastPosition: Int = -1
): Int {
    if (position > lastPosition) {
        val animation = ScaleAnimation(
            0.5f,
            1.0f,
            0.5f,
            1.0f,
            Animation.RELATIVE_TO_SELF,
            0.5f,
            Animation.RELATIVE_TO_SELF,
            0.5f
        )
        animation.duration = 300
        this.itemView.startAnimation(animation)
        return position
    }
    return 0
}

fun RecyclerView.ViewHolder.setSlideAnimation(
    position: Int,
    lastPosition: Int = -1
): Int {
    if (position > lastPosition) {
        val animation: Animation =
            AnimationUtils.loadAnimation(this.itemView.context, android.R.anim.slide_in_left)
        animation.duration = 500
        this.itemView.startAnimation(animation)
        return position
    }
    return position
}

fun RecyclerView.ViewHolder.setFadeAnimation(
    position: Int,
    lastPosition: Int = -1
): Int {
    if (position > lastPosition) {
        val animation = AlphaAnimation(0.0f, 1.0f)
        animation.duration = 800
        this.itemView.startAnimation(animation)
        return position
    }
    return 0
}



