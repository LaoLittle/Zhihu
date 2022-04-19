package org.laolittle.plugin.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TopSearch(
    @SerialName("display_query")
    val displayQuery: String,
    val query: String,
)