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
package com.github.jonforshort.rssreader.ui.main

import com.github.jonforshort.rssreader.feedcontentfetcher.FeedItem
import com.github.jonforshort.rssreader.feedsource.repo.Feed
import kotlinx.html.a
import kotlinx.html.body
import kotlinx.html.h3
import kotlinx.html.html
import kotlinx.html.img
import kotlinx.html.p
import kotlinx.html.stream.appendHTML
import kotlinx.html.unsafe
import java.net.URL
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

internal object FeedArticleFactory {

    private val FEED_ARTICLE_DATE_STRING_FORMATTER by lazy {
        DateTimeFormatter.ofPattern("HH:mm:ss MM/dd/yyyy")
    }

    fun from(feed: Feed, feedItem: FeedItem) =
        FeedArticle(
            contentHtml = createContentHtml(feedItem),
            linkUrl = URL(feedItem.link),
            publishDate = convertTimeInMsToDateString(feedItem.publishTimeInMs),
            publishTimeInMs = feedItem.publishTimeInMs,
            providerName = feed.providerName,
            providerIconUrl = URL(feed.providerIconUrl)
        )

    private fun createContentHtml(feedItem: FeedItem) =
        when {
            feedItem.mediaContent?.url?.isNotEmpty() == true -> {
                buildHtmlWithMediaContent(feedItem)
            }
            feedItem.enclosure?.url?.isNotEmpty() == true -> {
                buildHtmlWithEnclosure(feedItem)
            }
            else -> {
                buildHtmlWithDescriptionContainsHtml(feedItem)
            }
        }

    private fun buildHtmlWithDescriptionContainsHtml(feedItem: FeedItem) = StringBuilder().appendHTML()
        .html {
            body {
                h3 {
                    text(feedItem.title)
                }
                unsafe {
                    +feedItem.description
                }
            }
        }
        .toString()

    private fun buildHtmlWithMediaContent(feedItem: FeedItem) = StringBuilder().appendHTML()
        .html {
            body {
                h3 {
                    text(feedItem.title)
                }
                img {
                    src = feedItem.mediaContent?.url!!
                }
                p {
                    text(feedItem.description)
                }
                a(href = feedItem.link) {
                    text("Read more...")
                }
            }
        }
        .toString()

    private fun buildHtmlWithEnclosure(feedItem: FeedItem) = StringBuilder().appendHTML()
        .html {
            body {
                h3 {
                    text(feedItem.title)
                }
                img {
                    src = feedItem.enclosure?.url!!
                }
                p {
                    text(feedItem.description)
                }
                a(href = feedItem.link) {
                    text("Read more...")
                }
            }
        }
        .toString()

    private fun convertTimeInMsToDateString(timeInMs: Long) =
        Instant.ofEpochMilli(timeInMs)
            .atZone(ZoneId.systemDefault())
            .format(FEED_ARTICLE_DATE_STRING_FORMATTER)
}
