package com.raantech.awfrlak.store.data.repos.media

import com.raantech.awfrlak.store.data.api.response.APIResource
import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.models.media.Media
import okhttp3.MultipartBody

interface MediaRepo {
    suspend fun getMedia(
            skip: Int
    ): APIResource<ResponseWrapper<List<Media>>>

    suspend fun uploadMedia(
        mediaFile : MultipartBody.Part
    ): APIResource<ResponseWrapper<Any>>

    suspend fun deleteMedia(
        mediaId: Int
    ): APIResource<ResponseWrapper<Any>>
}