package com.seoulventure.melonbox.data.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class YtMusicOAuthResultResponse(
    @SerialName("access_token")
    val accessToken: String?,
    @SerialName("expires_in")
    val expireIn: Long?,
    @SerialName("scope")
    val scope: String?,
    @SerialName("token_type")
    val tokenType: String?,
)
