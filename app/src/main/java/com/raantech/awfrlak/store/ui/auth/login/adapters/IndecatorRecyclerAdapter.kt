package com.raantech.awfrlak.store.ui.auth.login.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raantech.awfrlak.R
import com.raantech.awfrlak.databinding.RowIndecatorBinding
import com.raantech.awfrlak.store.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.awfrlak.store.ui.base.adapters.BaseViewHolder

class IndecatorRecyclerAdapter(
    context: Context
) : BaseBindingRecyclerViewAdapter<Boolean>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowIndecatorBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowIndecatorBinding) :
        BaseViewHolder<Boolean>(binding.root) {

        override fun bind(item: Boolean) {
            if (item) {
                binding.imgDotImage.setCardBackgroundColor(context.resources.getColor(R.color.indecator_active))
                binding.imgDotImage.strokeWidth = context.resources.getDimension(R.dimen.dimen_zero).toInt()
            } else {
                binding.imgDotImage.setCardBackgroundColor(context.resources.getColor(R.color.indecator_disactive))
                binding.imgDotImage.strokeWidth = context.resources.getDimension(R.dimen._1sdp).toInt()
            }
        }
    }
}