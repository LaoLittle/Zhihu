package org.laolittle.plugin

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.cookies.*
import io.ktor.client.plugins.websocket.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.*
import org.laolittle.plugin.api.QrVerf
import org.laolittle.plugin.api.QrVerfRefresh
import org.laolittle.plugin.api.TopSearch
import org.laolittle.plugin.api.UserToken
import java.io.Closeable
import java.io.File
import java.util.concurrent.TimeUnit
import kotlin.coroutines.CoroutineContext

typealias Cookies = Map<String, String>

object TestClient: CoroutineScope, Closeable {
    private var sessionID = getSessionID()
    private const val BASE_URL = "https://www.zhihu.com"
    private fun recommendUrl(page: Int) =
        "https://www.zhihu.com/api/v3/feed/topstory/recommend?session_token=$sessionID&desktop=true&page_number=3&limit=6&action=down&after_id=${5 + (page - 2) * 6}&ad_interval=-10"

    private val cookieFile = File(".").resolve("cookies.json").also(File::createNewFile)
    private val userInfo = File(".").resolve("user.json").also(File::createNewFile)

    val httpClient: HttpClient = HttpClient(OkHttp) {
        install(WebSockets)

        install(HttpCookies) {
            default {
                kotlin.runCatching {
                    val cookies: Cookies = Json.decodeFromString(cookieFile.readText())

                    cookies.forEach { (name, value) ->
                        addCookie(Url(BASE_URL), Cookie(
                            name = name,
                            value = value,
                            encoding = CookieEncoding.DQUOTES
                        ))
                    }
                }
            }
        }

        install(ContentNegotiation) {
            json()
        }

        BrowserUserAgent()

        engine {
            config {
                readTimeout(30, TimeUnit.SECONDS)
            }
        }
    }

    suspend fun getTopSearch(): Array<TopSearch> {
        val raw = get("https://www.zhihu.com/api/v4/search/top_search").body<JsonObject>()

        val words = raw["top_search"]!!.jsonObject["words"]!!
        return Json.decodeFromJsonElement(words)
    }

    private val tags = Regex("""<p.*</p>|""")
    private val session = Regex("session_token=(.*?)&")
    suspend fun getRecommend(page: Int) {
        require(page in 1..Int.MAX_VALUE)

        val body: String
        if (page == 1) {
            body = get(BASE_URL).body()

            val start = body.indexOf("{\"initialState")
            val end = body.lastIndexOf("}") + 1

            sessionID = session.find(body)?.groupValues?.get(1) ?: getSessionID()

            Json.decodeFromString<JsonObject>(body.substring(start, end)).also {
                println(it["initialState"]!!.jsonObject.map { j -> j.key }.joinToString(","))
            }
        } else {
            body = get(recommendUrl(page)).body()

            println(body)
        }

    }

    suspend fun getQrVerf(): QrVerf {
        get("https://www.zhihu.com/signin?next=%2F")
        post("https://www.zhihu.com/udid")
        return post("https://www.zhihu.com/api/v3/account/api/login/qrcode").body()
    }

    suspend fun loginQrCode(verf: QrVerf): ByteArray =
        get("https://www.zhihu.com/api/v3/account/api/login/qrcode/${verf.token}/image?utm_campaign=internal&utm_medium=qr&utm_source=login&utm_term=force_launch&utm_content=qrscand")
            .body()

    suspend fun waitQrScan(verf: QrVerf) {
        var verify = verf
        var token: UserToken? = null

        while (isActive) {
            val ret =
                get("https://www.zhihu.com/api/v3/account/api/login/qrcode/${verify.token}/scan_info").body<JsonObject>()
            println(ret)
            when (ret["status"]?.jsonPrimitive?.intOrNull) {
                0 -> {}
                1 -> println("二维码已被扫描")
                5 -> {
                    verify = Json.decodeFromJsonElement<QrVerfRefresh>(ret).verf
                }
                else -> {
                    token = Json.decodeFromJsonElement(ret)
                    break
                }
            }
            delay(2_000)
        }

        token?.let {
            println(userInfo)
            userInfo.writeText(Json.encodeToString(it))
        } ?: throw IllegalStateException("获取用户信息失败!")
    }


    internal suspend inline fun get(
        urlString: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse = httpClient.get(urlString, block)

    internal suspend inline fun post(
        urlString: String,
        block: HttpRequestBuilder.() -> Unit = {}
    ): HttpResponse = httpClient.post(urlString, block)

    override val coroutineContext: CoroutineContext
        get() = httpClient.coroutineContext

    override fun close() {
        httpClient.close()
    }

    init {
        launch {
            buildMap {
                while (isActive) {
                    httpClient.plugin(HttpCookies).get(Url("https://www.zhihu.com")).forEach {
                        this[it.name] = it.value
                    }
                    delay(10_000)
                    val str = Json.encodeToString(this)
                    cookieFile.writeText(str)
                }
            }
        }
    }

}