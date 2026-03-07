package com.flxrs.dankchat.data.api.tiny

import com.flxrs.dankchat.data.UserId
import com.flxrs.dankchat.data.api.recoverNotFoundWith
import com.flxrs.dankchat.data.api.throwApiErrorOnFailure
import com.flxrs.dankchat.data.api.tiny.dto.TinyEmotesetDto
import com.flxrs.dankchat.data.api.tiny.dto.TinyResponseDto
import com.flxrs.dankchat.data.api.tiny.dto.TinyUserDto
import io.ktor.client.call.body
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
class TinyEmotesApiClient(private val tinyApi: TinyEmotesApi, private val json: Json) {

    suspend fun getTinyUser(instanceUrl: String, channelId: UserId): Result<TinyResponseDto<TinyUserDto>?> = runCatching {
        tinyApi.getUser(instanceUrl, channelId)
            .throwApiErrorOnFailure(json)
            .body<TinyResponseDto<TinyUserDto>>()
    }.recoverNotFoundWith(null)

    suspend fun getTinyGlobalEmoteset(instanceUrl: String): Result<TinyResponseDto<TinyEmotesetDto>> = runCatching {
        tinyApi.getGlobalEmoteset(instanceUrl)
            .throwApiErrorOnFailure(json)
            .body()
    }
}
