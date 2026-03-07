package com.flxrs.dankchat.data.api.tiny.dto

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class TinyResponseDto<T>(
    @SerialName(value = "status_code") val statusCode: Int,
    @SerialName(value = "message") val message: String?,
    @SerialName(value = "data") val data: T
)
