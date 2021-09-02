package com.raantech.awfrlak.ui.main.home.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raantech.awfrlak.data.models.home.MobilesItem
import com.raantech.awfrlak.databinding.RowPhoneBinding
import com.raantech.awfrlak.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.awfrlak.ui.base.adapters.BaseViewHolder
import com.raantech.awfrlak.utils.extensions.setPopUpAnimation

class PhonesRecyclerAdapter(
    context: Context
) : BaseBindingRecyclerViewAdapter<MobilesItem>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowPhoneBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setPopUpAnimation(position)
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowPhoneBinding) :
        BaseViewHolder<MobilesItem>(binding.root) {

        override fun bind(item: MobilesItem) {
            binding.item = item
            binding.root.setOnClickListener {
                itemClickListener?.onItemClick(it, adapterPosition, item)
            }
        }
    }
}