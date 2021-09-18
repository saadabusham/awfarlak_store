package com.raantech.awfrlak.store.ui.notifications.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raantech.awfrlak.store.data.models.notification.Notification
import com.raantech.awfrlak.databinding.RowNotificationBinding
import com.raantech.awfrlak.store.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.awfrlak.store.ui.base.adapters.BaseViewHolder
import com.raantech.awfrlak.store.utils.extensions.setSlideAnimation

class NotificationsRecyclerAdapter constructor(
        context: Context
) : BaseBindingRecyclerViewAdapter<Notification>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
                RowNotificationBinding.inflate(
                        LayoutInflater.from(context), parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setSlideAnimation(position)
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowNotificationBinding) :
            BaseViewHolder<Notification>(binding.root) {

        override fun bind(item: Notification) {
            binding.item = item
        }
    }
}