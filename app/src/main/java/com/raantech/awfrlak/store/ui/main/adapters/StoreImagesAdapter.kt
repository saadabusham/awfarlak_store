package com.raantech.awfrlak.store.ui.main.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raantech.awfrlak.databinding.RowStoreImageBinding
import com.raantech.awfrlak.store.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.awfrlak.store.ui.base.adapters.BaseViewHolder

class StoreImagesAdapter(
        context: Context
) : BaseBindingRecyclerViewAdapter<String>(context) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
                RowStoreImageBinding.inflate(
                        LayoutInflater.from(context), parent, false
                )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowStoreImageBinding) :
            BaseViewHolder<String>(binding.root) {

        override fun bind(item: String) {
            binding.data = item
            binding.imgPicture.setOnClickListener {
                itemClickListener?.onItemClick(it,bindingAdapterPosition,item)
            }
        }
    }


}