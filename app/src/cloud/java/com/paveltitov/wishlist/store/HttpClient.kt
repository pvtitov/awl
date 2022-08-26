package com.paveltitov.wishlist.store

import java.io.*
import java.net.HttpURLConnection
import java.net.HttpURLConnection.*
import java.net.URL
import java.nio.charset.Charset

class HttpClient {
    private val host = "http://www.mywishlist.ru"

    fun login(login: String, password: String): Response {

        var response: Response

        val path = "/login/login"
        val payload = "login[login]=$login&login[password]=$password"

        var con: HttpURLConnection = URL(host + path).openConnection() as HttpURLConnection

        con.requestMethod = "POST"
        setFollowRedirects(true)
        con.instanceFollowRedirects = true
        con.doOutput = true
        val writer = OutputStreamWriter(con.outputStream, "UTF-8")
        writer.write(payload)
        writer.flush()
        writer.close()
        con.connect()

        if (con.responseCode == HTTP_MOVED_TEMP
            || con.responseCode == HTTP_MOVED_TEMP
            || con.responseCode == HTTP_MOVED_TEMP) {

            val redirectedUrl = con.getHeaderField("Location")
            val cookies = con.getHeaderField("Set-Cookie")

            con = URL(redirectedUrl).openConnection() as HttpURLConnection
            con.setRequestProperty("Cookie", cookies)
        }

        try {
            val inputStream: InputStream = BufferedInputStream(con.inputStream)
            val stringBuilder = StringBuilder()
            val reader: Reader = BufferedReader(
                InputStreamReader(inputStream, Charset.forName("UTF-8"))
            )

            try {
                var ch: Int
                while (reader.read().also { ch = it } != -1) {
                    stringBuilder.append(ch.toChar())
                }
                response = Response.Success(stringBuilder.toString())
            } catch (e: IOException) {
                response = Response.Error(e.message ?: "IOException")
            } finally {
                reader.close()
            }
        } catch (e: IOException) {
            response = Response.Error(e.message ?: "IOException")
        } finally {
            con.disconnect()
        }

        return response
    }

    sealed class Response {
        data class Success(val response: String) : Response()
        data class Error(val message: String) : Response()
    }
}