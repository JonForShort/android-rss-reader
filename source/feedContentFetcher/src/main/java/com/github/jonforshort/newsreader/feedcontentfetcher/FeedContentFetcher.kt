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

import com.github.jonforshort.newsreader.feedcontentfetcher.model.Feed
import com.github.jonforshort.newsreader.feedcontentfetcher.util.getBaseUrl
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url
import java.net.URL

class FeedContentFetcher(private val url: URL) {

    suspend fun fetch(): List<FeedContent> {
        val urlPath = url.path.trimStart('/')
        val retrofit = Retrofit.Builder()
            .baseUrl(url.getBaseUrl())
            .addConverterFactory(SimpleXmlConverterFactory.create())
            .build()
        val rssFeed = retrofit.create(RssFeed::class.java)
        val feedContent = rssFeed.getContent(urlPath)
        return feedContent.items.map {
            FeedContent(it.title, it.link, it.description, it.publishDate.toDate())
        }
    }
}

internal interface RssFeed {

    @GET
    suspend fun getContent(@Url url: String): Feed
}
