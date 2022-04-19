package org.laolittle.plugin

import io.ktor.client.call.*
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlinx.coroutines.runBlocking
import org.junit.Test
import java.io.File

class Test {
    @Test
    fun getQr(): Unit = runBlocking {
        println("start")
        while (isActive) {
            when (readlnOrNull()) {
                "login" -> {
                    val verf = TestClient.getQrVerf()

                    val qr = TestClient.loginQrCode(verf)

                    File("code.png").writeBytes(qr)

                    TestClient.waitQrScan(verf)
                }

                "user" -> {
                    TestClient.get("https://www.zhihu.com/api/v4/me?include=ad_type%2Cavailable_message_types%2Cdefault_notifications_count%2Cfollow_notifications_count%2Cvote_thank_notifications_count%2Cmessages_count%2Cemail%2Caccount_status%2Cis_bind_phone%2Cfollowing_question_count%2Cis_force_renamed%2Crenamed_fullname").body<String>().also(::println)
                }

                "recommend" -> TestClient.getRecommend(1)

                "stop" -> cancel()
            }
        }
    }

    @Test
    fun decode() {
        println("\\u8FBD\\u5B81\\u76D8\\u9526\\u4E00\\u5973\\u5B50\\u516C\\u56ED\\u5185\\u7A7F\\u548C\\u670D\\u62CD\\u7167\\u906D\\u8DEF\\u4EBA\\u56F4\\u529D\\uFF0C\\u7A7F\\u8863\\u81EA\\u7531\\u5E94\\u5982\\u4F55\\u754C\\u5B9A\\uFF1F".decodeUnicode())
    }
}
  /*  private suspend fun matchUDID() = httpClient.get("https://www.zhihu.com/udid") {
        userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36")
        httpClient.cookies(url.build()).forEach {
            cookie(it.name, it.value)
        }
    }.apply {
        setCookie().also(::println)
    }

    private fun getCookie(): List<String> = runBlocking {
        httpClient.get("https://www.zhihu.com/signin?next=%2F") {
            userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.127 Safari/537.36")
        }.let {
            //it.setCookie().also(::println)
            httpClient.cookies("https://www.zhihu.com").also(::println)
            for ((name, value) in it.headers.toMap()) {
                if (name == "set-cookie")
                    return@runBlocking value.onEach(::println)
            }
            //httpClient.plugin(HttpCookies).get(Url("/")).also(::println)
            throw IllegalStateException("未找到Cookie")
        }


    }*/