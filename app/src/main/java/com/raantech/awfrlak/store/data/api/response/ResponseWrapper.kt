package com.raantech.awfrlak.store.data.api.response

import com.squareup.moshi.Json

data class ResponseWrapper<RETURN_MODEL>(
    @field:Json(name = "errors")
    val errors: List<GeneralError>,
    @field:Json(name = "code")
    val code: Int,
    @field:Json(name = "message")
    val message: String,
    @field:Json(name = "body")
    val body: RETURN_MODEL?
)