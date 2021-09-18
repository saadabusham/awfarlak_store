package com.raantech.awfrlak.store.data.models.home

import com.raantech.awfrlak.store.data.enums.CategoriesEnum

data class Category(
    val categoryEnum: CategoriesEnum,
    val title: String
)
