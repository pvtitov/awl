package com.paveltitov.wishlist

import com.paveltitov.wishlist.data.HttpClient
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        val result = HttpClient().login("pavel_titov", "just4E")
        assert(result is HttpClient.Response.Success)
    }
}