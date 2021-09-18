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

import android.content.res.Resources
import android.webkit.WebView
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.github.jonforshort.rssreader.feedcontentfetcher.FeedItem
import com.github.jonforshort.rssreader.feedsource.repo.Feed
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

internal data class FeedArticle(

    val title: String = "",

    val titleImageUrl: String? = null,

    val link: String = "",

    val description: String = "",

    val publishDate: String = "",

    val publishTimeInMs: Long = 0,

    val providerName: String,

    val providerIconUrl: String
)

private val FEED_ENCLOSURE_HEIGHT_PX by lazy {
    (200 * Resources.getSystem().displayMetrics.density).toInt()
}

private val FEED_ENCLOSURE_WIDTH_PX by lazy {
    Resources.getSystem().displayMetrics.widthPixels
}

private val FEED_ARTICLE_DATE_STRING_FORMATTER by lazy {
    DateTimeFormatter.ofPattern("HH:mm:ss MM/dd/yyyy")
}

@BindingAdapter("loadImage")
internal fun loadImage(view: ImageView, url: String) {
    Glide.with(view.context)
        .load(url)
        .override(FEED_ENCLOSURE_WIDTH_PX, FEED_ENCLOSURE_HEIGHT_PX)
        .centerCrop()
        .into(view)
}

@BindingAdapter("loadFeedIcon")
internal fun loadFeedIcon(view: ImageView, iconUrl: String) {
    Glide.with(view.context)
        .load(iconUrl)
        .into(view)
}

@BindingAdapter("loadHtml")
internal fun loadHtml(view: WebView, html: String) {
    view.loadData(html, "text/html; charset=UTF-8", null)
    view.settings.loadWithOverviewMode = true
}

internal fun createFeedArticle(feed: Feed, feedItem: FeedItem) =
    FeedArticle(
        title = feedItem.title,
        titleImageUrl = feedItem.enclosure?.url,
        link = feedItem.link,
        description = feedItem.description,
        publishDate = convertTimeInMsToDateString(feedItem.publishTimeInMs),
        publishTimeInMs = feedItem.publishTimeInMs,
        providerName = feed.providerName,
        providerIconUrl = feed.providerIconUrl
    )

private fun convertTimeInMsToDateString(timeInMs: Long) =
    Instant.ofEpochMilli(timeInMs)
        .atZone(ZoneId.systemDefault())
        .format(FEED_ARTICLE_DATE_STRING_FORMATTER)
