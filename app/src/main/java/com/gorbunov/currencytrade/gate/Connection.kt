package com.gorbunov.currencytrade.gate

import com.gorbunov.currencytrade.model.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit


class Connection {
    private val httpClient: OkHttpClient
    private val cookieJar: CookieJar
    private var cookie: MutableList<Cookie> = mutableListOf()

    var user: String
        get() {
            return userHolder[0]
        }
        set(value) {
            userHolder[0] = value
        }
    var pass: String
        get() {
            return passHolder[0]
        }
        set(value) {
            passHolder[0] = value
        }
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


    inline fun <reified T> makePostRequest(
        url: String,
        body: T,
        query: Map<String, String> = mapOf()
    ): String {
        val json = format.encodeToString(value = body)
        return makePostRequestImpl(json, url, query)
    }


    fun makePostRequestImpl(
        json: String,
        url: String,
        query: Map<String, String>
    ): String {
        val contentType = "application/json".toMediaTypeOrNull()
        val b = json.toRequestBody(contentType)

        var urlWithParams: String? = null

        if (query.isNotEmpty()) {
            val urlBuilder = "$baseUrl$url".toHttpUrlOrNull()?.newBuilder()

            query.forEach { (k, v) ->
                urlBuilder?.addQueryParameter(k, v)
            }

            urlWithParams = urlBuilder?.build().toString()
        }

        val request = Request.Builder()
            .post(b)
            .url(urlWithParams ?: "$baseUrl$url")
            .build()

        val r = httpClient.newCall(request).execute()

        if (r.isSuccessful) {
            return r.body!!.string()
        } else {
            throw RuntimeException("Ошибка запроса ${r.message}")
        }
    }

    private fun makeGetRequest(
        url: String,
        query: Map<String, String> = mapOf()
    ): String {

        var urlWithParams: String? = null

        if (query.isNotEmpty()) {
            val urlBuilder = "$baseUrl$url".toHttpUrlOrNull()?.newBuilder()

            query.forEach { (k, v) ->
                urlBuilder?.addQueryParameter(k, v)
            }

            urlWithParams = urlBuilder?.build().toString()
        }




        val request = Request.Builder()
            .get()
            .url(urlWithParams ?: "$baseUrl$url")
            .build()

        val r = httpClient.newCall(request).execute()

        if (r.isSuccessful) {
            return r.body!!.string()
        } else {
            throw RuntimeException("Ошибка запроса ${r.message}")
        }
    }

    private inline fun <reified T> makePatchRequest(url: String, body: T, query: Map<String, String> = mapOf()): String {
        val json = format.encodeToString(value = body)
        return makePatchRequestImpl(json, url, query)
    }

    private fun makePatchRequestImpl(
        json: String,
        url: String,
        query: Map<String, String>
    ): String {
        val contentType = "application/json".toMediaTypeOrNull()
        val b = json.toRequestBody(contentType)

        var urlWithParams: String? = null

        if (query.isNotEmpty()) {
            val urlBuilder = "$baseUrl$url".toHttpUrlOrNull()?.newBuilder()

            query.forEach { (k, v) ->
                urlBuilder?.addQueryParameter(k, v)
            }

            urlWithParams = urlBuilder?.build().toString()
        }

        val request = Request.Builder()
            .patch(b)
            .url(urlWithParams ?: "$baseUrl$url")
            .build()

        val r = httpClient.newCall(request).execute()

        if (r.isSuccessful) {
            return r.body!!.string()
        } else {
            throw RuntimeException("Ошибка запроса ${r.message}")
        }
    }

    private inline fun <reified T> makePutRequest(url: String, body: T, query: Map<String, String> = mapOf()): String {
        val json = format.encodeToString(value = body)
        return makePutRequestImpl(json, url, query)
    }

    private fun makePutRequestImpl(
        json: String,
        url: String,
        query: Map<String, String>
    ): String {
        val contentType = "application/json".toMediaTypeOrNull()
        val b = json.toRequestBody(contentType)

        var urlWithParams: String? = null

        if (query.isNotEmpty()) {
            val urlBuilder = "$baseUrl$url".toHttpUrlOrNull()?.newBuilder()

            query.forEach { (k, v) ->
                urlBuilder?.addQueryParameter(k, v)
            }

            urlWithParams = urlBuilder?.build().toString()
        }

        val request = Request.Builder()
            .put(b)
            .url(urlWithParams ?: "$baseUrl$url")
            .build()

        val r = httpClient.newCall(request).execute()

        if (r.isSuccessful) {
            return r.body!!.string()
        } else {
            throw RuntimeException("Ошибка запроса ${r.message}")
        }
    }


    fun getUnapprovedList(): List<AdminUserResponse> {
        val result = makeGetRequest("unapproved")
        return format.decodeFromString(result)
    }

    fun getSelf(): User {
        val result = makeGetRequest("me")
        return format.decodeFromString(result)
    }

    fun getChecks(): List<Check> {
        val result = makeGetRequest("checks")
        return format.decodeFromString(result)
    }

    fun getCurrencies(): List<String> {
        val result = makeGetRequest("currency_types")
        val ct = format.decodeFromString<CurrencyTypes>(result).currencies

        return ct
//        return format.decodeFromString(result)
    }

    fun createCheckWith(cur: String) {
        val result = makePostRequest("create_check", "", mapOf(Pair("currency", cur)))
    }

    fun fillCheckWith(value: Int) {
        val result = makePatchRequest("refill", "", mapOf(Pair("amount", "$value"), Pair("currency", "RUB")))
    }

    fun getApprovedList(): List<AdminUserResponse> {
        val result = makeGetRequest("approved")
        return format.decodeFromString(result)
    }

    fun approveUser(id: Long) {
        val result = makePatchRequest("approve/$id", "")
    }

    fun changeBlockStatusBy(id: Long) {
        makePatchRequest("block/$id", "")
    }


    fun registerUser(body: RegisterRequestBody): UserResponseBody {
        val result = makePostRequest(
            "register",
            body
        )
        return format.decodeFromString(result)
    }

    fun convert(from: String, to: String, value: String){
        val result = makePutRequest("convert", "", mapOf("type_from" to from, "type_to" to to, "value" to value))
    }

    fun getCurrencyValue(from: String, to: String): CurrencyPrice{
        val result = makeGetRequest("get_price", mapOf("type_from" to from, "type_to" to to, "value" to "1"))
        return format.decodeFromString(result)
    }

    fun getChecksHistory(): List<ChecksHistory>{
        val result = makeGetRequest("history")
        return format.decodeFromString(result)
    }

    fun tempLogin(name: String): UserLoginResponseBody {
        val reslt = makeGetRequest("login/$name")
        return format.decodeFromString(reslt)
    }

}
