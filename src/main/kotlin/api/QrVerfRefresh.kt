package org.laolittle.plugin.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QrVerfRefresh(
    val status: Int,
    @SerialName("new_token")
    val verf: QrVerf,
)