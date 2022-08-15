package com.raantech.awfrlak.store.ui.citypicker.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.raantech.awfrlak.databinding.RowCityBinding
import com.raantech.awfrlak.store.data.models.City
import com.raantech.awfrlak.store.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.awfrlak.store.ui.base.adapters.BaseViewHolder
import com.raantech.awfrlak.store.utils.extensions.gone
import com.raantech.awfrlak.store.utils.extensions.visible

class CitiesRecyclerAdapter(
    context: Context
) : BaseBindingRecyclerViewAdapter<City>(context) {

    fun getSelectedItem(): City? {
        return items.singleOrNull { it.selected }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(
            RowCityBinding.inflate(
                LayoutInflater.from(context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ViewHolder) {
            holder.bind(items[position])
        }
    }

    inner class ViewHolder(private val binding: RowCityBinding) :
        BaseViewHolder<City>(binding.root) {

        override fun bind(item: City) {
            binding.city = item
            binding.root.setOnClickListener {
                items.withIndex().singleOrNull { it.value.selected }?.let {
                    it.value.selected = false
                    notifyItemChanged(it.index)
                }
                item.selected = true
                notifyItemChanged(bindingAdapterPosition)
                itemClickListener?.onItemClick(it, bindingAdapterPosition, item)
            }
            if (item.selected) {
                binding.imgSelected.visible()
            } else {
                binding.imgSelected.gone()
            }
        }
    }
}