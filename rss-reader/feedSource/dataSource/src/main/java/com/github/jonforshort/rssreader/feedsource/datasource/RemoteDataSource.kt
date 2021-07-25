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
package com.github.jonforshort.rssreader.feedsource.datasource

import com.fasterxml.jackson.databind.json.JsonMapper
import com.github.jonforshort.rssreader.feedsource.datasource.DataSource.Companion.EMPTY_FEED_CHANNEL
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import timber.log.Timber.e

class RemoteDataSource : DataSource {

    companion object {
        private const val FEED_JSON_URL =
            "https://raw.githubusercontent.com/JonForShort/android-rss-reader/master/rss-feeds/feed.json"
    }

    override suspend fun get(): FeedChannel {
        val feedSchemaJson = fetch()
        return try {
            return JsonMapper().readValue(feedSchemaJson, FeedChannel::class.java)
        } catch (e: Exception) {
            e(e)
            EMPTY_FEED_CHANNEL
        }
    }

    private suspend fun fetch(): String? {
        val client = OkHttpClient.Builder().build()
        val request = Request.Builder().url(FEED_JSON_URL).build()
        withContext(Dispatchers.IO) {
            client.newCall(request).execute().use { response ->
                if (response.isSuccessful) {
                    response.body?.toString()?.let {
                        return@withContext it
                    }
                }
            }
        }
        return null
    }
}