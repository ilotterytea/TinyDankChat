package com.flxrs.dankchat.data.api.tiny.dto

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class TinyBasicUserDto(
    @SerialName(value = "id") val id: String,
    @SerialName(value = "username") val username: String
)
