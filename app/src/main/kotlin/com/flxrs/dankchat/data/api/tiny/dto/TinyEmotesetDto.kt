package com.flxrs.dankchat.data.api.tiny.dto

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class TinyEmotesetDto(
    val id: String,
    val name: String,
    val owner: TinyBasicUserDto,
    val emotes: List<TinyEmoteDto>
)
