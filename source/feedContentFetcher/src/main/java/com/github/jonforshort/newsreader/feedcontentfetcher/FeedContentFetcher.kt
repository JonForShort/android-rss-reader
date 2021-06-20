//
// MIT License
//
// Copyright (c) 2021
//
// Permission is hereby granted, free of charge, to any person obtaining a copy
// of this software and associated documentation files (the "Software"), to deal
// in the Software without restriction, including without limitation the rights
// to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
// copies of the Software, and to permit persons to whom the Software is
// furnished to do so, subject to the following conditions:
//
// The above copyright notice and this permission notice shall be included in all
// copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
// IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
// FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
// AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
// LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
// OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
// SOFTWARE.
//
package com.github.jonforshort.newsreader.feedcontentfetcher

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.net.URL

class FeedContentFetcher(private val url: URL) {

    sealed class Result<out T : Any> {
        data class Success<out T : Any>(val result: T) : Result<T>()
        data class InvalidXmlFormatError(val error: String) : Result<Nothing>()
        data class ServerError(val error: String) : Result<Nothing>()
    }

    suspend fun fetch(): Result<FeedContent> {
        val client = OkHttpClient.Builder().build()
        val request = Request.Builder().url(url).build()
        return withContext(Dispatchers.IO) {
            client.newCall(request).execute().use { response ->
                handleFetchResponse(response)
            }
        }
    }

    private fun handleFetchResponse(response: Response) =
        if (response.isSuccessful && response.body != null) {
            val responseBody = response.body!!.string()
            try {
                val parsedRss = RssParser().parse(responseBody)
                Result.Success(parsedRss)
            } catch (e: Exception) {
                Result.InvalidXmlFormatError("failed to parse rss, ${e.message}")
            }
        } else {
            Result.ServerError("failed to fetch rss, ${response.code}")
        }
}
