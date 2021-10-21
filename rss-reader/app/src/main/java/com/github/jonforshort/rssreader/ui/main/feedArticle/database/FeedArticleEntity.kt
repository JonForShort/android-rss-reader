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
package com.github.jonforshort.rssreader.ui.main.feedArticle.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.github.jonforshort.rssreader.ui.main.feedArticle.FeedArticle
import java.net.URL

@Entity
internal data class FeedArticleEntity(

    @PrimaryKey @ColumnInfo(name = "id") val id: String,

    @ColumnInfo(name = "contentHtml") val contentHtml: String,

    @ColumnInfo(name = "linkUrl") val linkUrl: String,

    @ColumnInfo(name = "publishDate") val publishDate: String = "",

    @ColumnInfo(name = "publishTimeInMs") val publishTimeInMs: Long = 0,

    @ColumnInfo(name = "providerName") val providerName: String,

    @ColumnInfo(name = "providerIconUrl") val providerIconUrl: String,

    @ColumnInfo(name = "isBookmarked") val isBookmarked: Boolean
)

internal fun fromArticle(article: FeedArticle, isBookmarked: Boolean) =
    FeedArticleEntity(
        id = article.id,
        contentHtml = article.contentHtml,
        linkUrl = article.linkUrl.toString(),
        publishDate = article.publishDate,
        publishTimeInMs = article.publishTimeInMs,
        providerName = article.providerName,
        providerIconUrl = article.providerIconUrl.toString(),
        isBookmarked = isBookmarked
    )

internal fun FeedArticleEntity.toArticle() =
    FeedArticle(
        id = id,
        contentHtml = contentHtml,
        linkUrl = URL(linkUrl),
        publishDate = publishDate,
        publishTimeInMs = publishTimeInMs,
        providerName = providerName,
        providerIconUrl = URL(providerIconUrl),
    )
