package com.flxrs.dankchat.data.api.tiny.dto

import androidx.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class TinyUserDto(
    @SerialName(value = "id") val id: String,
    @SerialName(value = "username") val username: String,
    @SerialName(value = "active_emote_set_id") val activeEmoteSetId: String?,
    @SerialName(value = "emote_sets") val emoteSets: List<TinyEmotesetDto>
)
