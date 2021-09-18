package com.raantech.awfrlak.store.ui.wishlist.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.stream.MalformedJsonException
import com.raantech.awfrlak.store.data.enums.CategoriesEnum
import com.raantech.awfrlak.store.data.models.home.AccessoriesItem
import com.raantech.awfrlak.store.data.models.home.MobilesItem
import com.raantech.awfrlak.store.data.models.home.Service
import com.raantech.awfrlak.store.data.models.home.Store
import com.raantech.awfrlak.store.data.models.wishlist.WishList
import com.raantech.awfrlak.databinding.RowAccessoryWishlistBinding
import com.raantech.awfrlak.databinding.RowMobileWishlistBinding
import com.raantech.awfrlak.databinding.RowServiceWishlistBinding
import com.raantech.awfrlak.databinding.RowStoreWishlistBinding
import com.raantech.awfrlak.store.ui.base.adapters.BaseBindingRecyclerViewAdapter
import com.raantech.awfrlak.store.ui.base.adapters.BaseViewHolder
import com.raantech.awfrlak.store.utils.extensions.setSlideAnimation

class WishListRecyclerAdapter(
        context: Context
) : BaseBindingRecyclerViewAdapter<WishList>(context) {

    companion object {
        const val MOBILE = 1
        const val ACCESSORY = 2
        const val SERVICE = 3
        const val STORE = 4
    }

    override fun getItemViewType(position: Int): Int {
        return when (items[position].entity_type ?: CategoriesEnum.MOBILES.value) {
            CategoriesEnum.MOBILES.value -> MOBILE
            CategoriesEnum.ACCESSORIES.value -> ACCESSORY
            CategoriesEnum.SERVICES.value -> SERVICE
            else -> STORE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MOBILE -> MobileViewHolder(
                    RowMobileWishlistBinding.inflate(
                            LayoutInflater.from(context), parent, false
                    )
            )
            ACCESSORY -> AccessoryViewHolder(
                    RowAccessoryWishlistBinding.inflate(
                            LayoutInflater.from(context), parent, false
                    )
            )
            SERVICE -> ServiceViewHolder(
                    RowServiceWishlistBinding.inflate(
                            LayoutInflater.from(context), parent, false
                    )
            )
            else -> StoreViewHolder(
                    RowStoreWishlistBinding.inflate(
                            LayoutInflater.from(context), parent, false
                    )
            )
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder.setSlideAnimation(position)
        when (holder) {
            is MobileViewHolder -> {
                items[position].let { holder.bind(it) }
            }
            is AccessoryViewHolder -> {
                items[position]?.let { holder.bind(it) }
            }
            is StoreViewHolder -> {
                items[position]?.let { holder.bind(it) }
            }
            is ServiceViewHolder -> {
                items[position]?.let { holder.bind(it) }
            }
        }
    }

    inner class MobileViewHolder(private val binding: RowMobileWishlistBinding) :
            BaseViewHolder<WishList>(binding.root) {

        override fun bind(item: WishList) {
            try {
                val mobile = Gson().fromJson(Gson().toJson(item.entity), MobilesItem::class.java)
                binding.item = mobile
                binding.imgFavorite.setOnClickListener {
                    mobile.isWishlist = mobile.isWishlist == false
                    binding.item = mobile
                    itemClickListener?.onItemClick(it, bindingAdapterPosition, mobile)
                }
                binding.root.setOnClickListener {
                    itemClickListener?.onItemClick(it, bindingAdapterPosition, mobile)
                }
            } catch (e: MalformedJsonException) {
                e
            } catch (e: JsonSyntaxException) {
                e
            }
        }
    }

    inner class AccessoryViewHolder(private val binding: RowAccessoryWishlistBinding) :
            BaseViewHolder<WishList>(binding.root) {

        override fun bind(item: WishList) {
            try {
                val accessory = Gson().fromJson(Gson().toJson(item.entity), AccessoriesItem::class.java)
                binding.item = accessory
                binding.imgFavorite.setOnClickListener {
                    accessory.isWishlist = accessory.isWishlist == false
                    binding.item = accessory
                    itemClickListener?.onItemClick(it, bindingAdapterPosition, accessory)
                }
                binding.root.setOnClickListener {
                    itemClickListener?.onItemClick(it, bindingAdapterPosition, accessory)
                }
            } catch (e: MalformedJsonException) {
                e
            } catch (e: JsonSyntaxException) {
                e
            }
        }
    }

    inner class StoreViewHolder(private val binding: RowStoreWishlistBinding) :
            BaseViewHolder<WishList>(binding.root) {

        override fun bind(item: WishList) {
            try {
                val store = Gson().fromJson(Gson().toJson(item.entity), Store::class.java)
                binding.item = store
                binding.imgFavorite.setOnClickListener {
                    store.isWishlist = store.isWishlist == false
                    binding.item = store
                    itemClickListener?.onItemClick(it, bindingAdapterPosition, store)
                }
                binding.root.setOnClickListener {
                    itemClickListener?.onItemClick(it, bindingAdapterPosition, store)
                }
            } catch (e: MalformedJsonException) {
                e
            } catch (e: JsonSyntaxException) {
                e
            }
        }
    }

    inner class ServiceViewHolder(private val binding: RowServiceWishlistBinding) :
            BaseViewHolder<WishList>(binding.root) {

        override fun bind(item: WishList) {
            try {
                val service = Gson().fromJson(Gson().toJson(item.entity), Service::class.java)
                binding.item = service
                binding.imgFavorite.setOnClickListener {
                    service.isWishlist = service.isWishlist == false
                    binding.item = service
                    itemClickListener?.onItemClick(it, bindingAdapterPosition, service)
                }
                binding.root.setOnClickListener {
                    itemClickListener?.onItemClick(it, bindingAdapterPosition, service)
                }
            } catch (e: MalformedJsonException) {
                e
            } catch (e: JsonSyntaxException) {
                e
            }

        }
    }
}