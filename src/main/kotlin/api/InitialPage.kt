package org.laolittle.plugin.api

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class InitialPage(
    val initialState: InitialState,
    val subAppName: AppName,
) {
    @Serializable
    data class InitialState(
        val common: JsonObject,
        val loading: JsonObject,
        val club: JsonObject,
        val entities: JsonObject,
    ) {

    }

    @Serializable
    enum class AppName {
        @SerialName("main")
        Main,
    }
}