package com.raantech.awfrlak.store.data.api.response

data class APIResource<out RESPONSE_DATA>(
    val status: RequestStatusEnum,
    val data: RESPONSE_DATA?,
    val messages: String?,
    val errors: List<GeneralError>?,
    val statusCode: Int?,
    val statusSubCode: ResponseSubErrorsCodeEnum?
) {

    companion object {
        fun <RESPONSE_DATA> success(data: RESPONSE_DATA?, messages: String? = null,
                                    errors: List<GeneralError>? = null,
                                    statusCode: Int? = null): APIResource<RESPONSE_DATA> {
            return APIResource(
                RequestStatusEnum.SUCCESS,
                data,
                messages,
                errors,
                statusCode,
                ResponseSubErrorsCodeEnum.Success
            )
        }

        fun <RESPONSE_DATA> error(
            msgs: String,
            data: RESPONSE_DATA?,
            errors: List<GeneralError>?,
            statusCode: Int,
            failedStatusSubCode: ResponseSubErrorsCodeEnum
        ): APIResource<RESPONSE_DATA> {
            return APIResource(
                RequestStatusEnum.FAILED,
                data,
                msgs,
                errors,
                statusCode,
                failedStatusSubCode
            )
        }

        fun <RESPONSE_DATA> loading(): APIResource<RESPONSE_DATA> {
            return APIResource(
                RequestStatusEnum.LOADING,
                null,
                null,
                null,
                null,
                null
            )
        }
    }
}