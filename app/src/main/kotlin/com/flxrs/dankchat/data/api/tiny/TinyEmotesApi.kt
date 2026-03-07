package com.flxrs.dankchat.data.api.tiny

import com.flxrs.dankchat.data.UserId
import com.flxrs.dankchat.data.repo.emote.withLeadingHttps
import io.ktor.client.HttpClient
import io.ktor.client.request.get

class TinyEmotesApi(private val ktorClient: HttpClient) {

    suspend fun getUser(instanceUrl: String, channelId: UserId) = ktorClient.get("${instanceUrl.withLeadingHttps}/users.php?alias_id=$channelId")

    suspend fun getGlobalEmoteset(instanceUrl: String) = ktorClient.get("${instanceUrl.withLeadingHttps}/emotesets/?id=global")
}
