package com.flxrs.dankchat.data.api.tiny.dto

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class TinyEmoteDto(
    val id: String,
    val code: String,
    @SerialName(value = "original_code") val originalCode: String?,
    val ext: String,
    @SerialName(value = "uploaded_by") val uploadedBy: TinyBasicUserDto?
)

