package org.laolittle.plugin

import kotlinx.serialization.json.Json

internal val Json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
    isLenient = true
    allowStructuredMapKeys = true
}

val chars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
fun getSessionID() = buildString {

    repeat(16) {
        append(chars.random())
    }
}

fun String.decodeUnicode(): String {
    var start = 0
    var end: Int
    val buffer = StringBuffer()
    while (start > -1) {
        end = indexOf("\\u", start + 2)
        val charStr: String = if (end == -1) {
            substring(start + 2, length)
        } else {
            substring(start + 2, end)
        }
        val letter = charStr.toInt(16).toChar()
        buffer.append(letter.toString())
        start = end
    }
    return buffer.toString()
}