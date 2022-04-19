package org.laolittle.plugin.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QrVerf(
    @SerialName("expires_at")
    val expiresTime: Long,
    val token: String
)