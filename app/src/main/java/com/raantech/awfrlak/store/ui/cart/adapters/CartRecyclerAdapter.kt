package com.raantech.awfrlak.store.ui.cart.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raantech.awfrlak.store.data.models.home.AccessoriesItem
import com.raantech.awfrlak.store.data.models.home.MobilesItem
import com.raantech.awfrlak.databinding.RowAccessoryCartBinding
import com.raantech.awfrlak.databinding.RowMobileCartBinding
import com.raantech.awfrlak.store.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.awfrlak.store.ui.base.adapters.BaseViewHolder
import com.raantech.awfrlak.store.utils.extensions.setSlideAnimation

class CartRecyclerAdapter(
        context: Context
) : BaseBindingRecyclerViewAdapter<Any>(context) {

    companion object {
        const val MOBILES = 1
        const val ACCESSORIES = 2
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position]) {
            is MobilesItem -> MOBILES
            else -> ACCESSORIES
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MOBILES -> MobileCartViewHolder(
                    RowMobileCartBinding.inflate(
                            LayoutInflater.from(context), parent, false
                    )
            )
            else -> AccessoriesCartViewHolder(
                    RowAccessoryCartBinding.inflate(
                            LayoutInflater.from(context), parent, false
                    )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setSlideAnimation(position)
        if (holder is MobileCartViewHolder) {
            holder.bind(items[position] as MobilesItem)
        } else if (holder is AccessoriesCartViewHolder) {
            holder.bind(items[position] as AccessoriesItem)
        }
    }

    inner class MobileCartViewHolder(private val binding: RowMobileCartBinding) :
            BaseViewHolder<MobilesItem>(binding.root) {

        override fun bind(item: MobilesItem) {
            binding.item = item
            binding.count = item.count
            binding.imgPlus.setOnClickListener {
                binding.count = binding.count?.plus(1)
                item.count = binding.count
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
            binding.imgMinus.setOnClickListener {
                if (binding.count ?: 0 > 1)
                    binding.count = binding.count?.minus(1)
                item.count = binding.count
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
            binding.cvDelete.setOnClickListener {
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }

    inner class AccessoriesCartViewHolder(private val binding: RowAccessoryCartBinding) :
            BaseViewHolder<AccessoriesItem>(binding.root) {

        override fun bind(item: AccessoriesItem) {
            binding.item = item
            binding.count = item.count
            binding.imgPlus.setOnClickListener {
                binding.count = binding.count?.plus(1)
                item.count = binding.count
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
            binding.imgMinus.setOnClickListener {
                if (binding.count ?: 0 > 1)
                    binding.count = binding.count?.minus(1)
                item.count = binding.count
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
            binding.cvDelete.setOnClickListener {
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
        }
    }

}