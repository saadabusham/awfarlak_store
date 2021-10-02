package com.raantech.awfrlak.store.data.daos.remote.media

import com.raantech.awfrlak.store.data.models.media.Media
import com.raantech.awfrlak.store.data.api.response.ResponseWrapper
import com.raantech.awfrlak.store.data.common.NetworkConstants
import okhttp3.MultipartBody
import retrofit2.http.*

interface MediaRemoteDao {

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @GET("merchant/media")
    suspend fun getMedia(
        @Query("skip") skip: Int
    ): ResponseWrapper<List<Media>>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @Multipart
    @POST("merchant/media/store")
    suspend fun uploadMedia(
        @Part mediaFile: MultipartBody.Part
    ): ResponseWrapper<Any>

    @Headers("${NetworkConstants.SKIP_AUTHORIZATION_HEADER}:false")
    @DELETE("merchant/media/{mediaId}/destroy")
    suspend fun deleteMedia(
        @Path("mediaId") mediaId: Int
    ): ResponseWrapper<Any>


}