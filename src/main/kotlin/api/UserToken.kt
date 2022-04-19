package org.laolittle.plugin.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserToken(
    @SerialName("user_id")
    val userId: Long,
    val uid: String,
    @SerialName("access_token")
    val accessToken: String,
    @SerialName("expires_in")
    val expiresIn: Long,
    @SerialName("token_type")
    val tokenType: TokenType,
    @SerialName("refresh_token")
    val refreshToken: String,
) {
    @Serializable
    enum class TokenType {
        @SerialName("bearer")
        Bearer
    }
}