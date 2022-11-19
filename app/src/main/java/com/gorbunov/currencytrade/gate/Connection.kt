package com.gorbunov.currencytrade.gate

import com.gorbunov.currencytrade.model.RegisterRequestBody
import com.gorbunov.currencytrade.model.UnapprovedUserResponse
import com.gorbunov.currencytrade.model.UserLoginResponseBody
import com.gorbunov.currencytrade.model.UserResponseBody
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit


class Connection {
    private val httpClient: OkHttpClient
    private val cookieJar: CookieJar
    private var cookie: MutableList<Cookie> = mutableListOf()

    var user: String
        get() {return userHolder[0]}
        set(value) {userHolder[0] = value}
    var pass: String
        get() {return passHolder[0]}
        set(value) {passHolder[0] = value}
    var baseUrl: String = "http://192.168.1.102:8000/users/"
    private var tokenHolder = arrayOf("")
    private var userHolder = arrayOf("")
    private var passHolder = arrayOf("")

    fun setComponents(user: String = "", pass: String = "") {
        this.pass = pass
        this.user = user
    }


    val format = Json {
        prettyPrint = true//Удобно печатает в несколько строчек
        ignoreUnknownKeys = true// Неизвестные значение
        coerceInputValues = true// Позволяет кодировать в параметрах null
        explicitNulls = true// Позволяет декодировать в параметрах null
    }


    init {
        cookieJar = object : CookieJar {


            override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
                if (cookie.isNotEmpty()) {
                    return cookie
                }
                return mutableListOf()
            }

            override fun saveFromResponse(url: HttpUrl, cookies: List<Cookie>) {
                cookie = cookies as MutableList<Cookie>
            }
        }


        val b = OkHttpClient.Builder()
            .cookieJar(cookieJar)
            .connectTimeout(15000, TimeUnit.MILLISECONDS)
            .writeTimeout(15000, TimeUnit.MILLISECONDS)
            .readTimeout(15000, TimeUnit.MILLISECONDS)


        b.authenticator(
            TokenAuthenticator(
                format,
                baseUrl + "token",
                userHolder,
                passHolder,
                tokenHolder
            )
        )


        httpClient = b.build()

    }


    inline fun <reified T> makePostRequest(url: String, body: T): String {
        val json = format.encodeToString(value = body)
        return makeRequestImpl(json, url)
    }


    fun makeRequestImpl(
        json: String,
        url: String
    ): String {
        val contentType = "application/json".toMediaTypeOrNull()
        val b = json.toRequestBody(contentType)


        val request = Request.Builder()
            .post(b)
            .url("$baseUrl$url")
            .build()

        val r = httpClient.newCall(request).execute()

        if (r.isSuccessful) {
            return r.body!!.string()
        } else {
            throw RuntimeException("Ошибка запроса ${r.message}")
        }
    }

    fun makeGetRequest(
        url: String
    ): String {
        val contentType = "application/json".toMediaTypeOrNull()


        val request = Request.Builder()
            .get()
            .url("$baseUrl$url")
            .build()

        val r = httpClient.newCall(request).execute()

        if (r.isSuccessful) {
            return r.body!!.string()
        } else {
            throw RuntimeException("Ошибка запроса ${r.body}")
        }
    }


    fun getUnapprovedList(): List<UnapprovedUserResponse> {
        val result = makeGetRequest("unapproved")
        val list: List<UnapprovedUserResponse> = format.decodeFromString(result)
        return list
    }

    fun registerUser(body: RegisterRequestBody): UserResponseBody {
        val result = makePostRequest(
            "register",
            body
        )
        return format.decodeFromString(result)
    }

    fun tempLogin(name: String): UserLoginResponseBody {
        val reslt = makeGetRequest("login/$name")
        return format.decodeFromString(reslt)
    }

}
