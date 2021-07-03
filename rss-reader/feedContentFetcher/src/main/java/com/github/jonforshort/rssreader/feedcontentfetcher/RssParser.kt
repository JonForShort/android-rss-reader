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
package com.github.jonforshort.rssreader.feedcontentfetcher

import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.github.jonforshort.rssreader.feedcontentfetcher.model.RssVersion
import com.github.jonforshort.rssreader.feedcontentfetcher.model.rsstwo.Rss as RssTwo

internal class RssParser {

    fun parse(rssContent: String): FeedContent {
        val rss = createXmlMapper().readValue(rssContent, RssVersion::class.java)
        return when (rss.version) {
            "2.0" -> parseRssTwo(rssContent)
            else -> throw RuntimeException("invalid version for rss content")
        }
    }

    private fun parseRssTwo(rssContent: String): FeedContent {
        val rss = createXmlMapper().readValue(rssContent, RssTwo::class.java)
        val channel = rss.channel!!
        return FeedContent(
            version = "2.0",
            channel = FeedChannel(
                channel.title,
                channel.link,
                channel.description,
                channel.item.map { item ->
                    FeedItem(
                        item.title,
                        item.link,
                        item.description,
                        item.pubDate,
                        item.source,
                        item.enclosure.let {
                            FeedItemEnclosure(
                                it.url,
                                it.lengthInBytes?.toInt() ?: 0,
                                it.mimeType
                            )
                        })
                })
        )
    }

    private fun createXmlMapper() =
        XmlMapper().disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
}
