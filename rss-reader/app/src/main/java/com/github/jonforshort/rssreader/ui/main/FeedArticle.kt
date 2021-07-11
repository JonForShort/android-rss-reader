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

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.github.jonforshort.rssreader.feedcontentfetcher.FeedItemEnclosure

internal data class FeedArticle(

    val title: String = "",

    val link: String = "",

    val description: String = "",

    val publishDate: String = "",

    val enclosure: FeedItemEnclosure? = null,

    val providerName: String,

    val providerIconUrl: String,
)

@BindingAdapter("loadEnclosure")
internal fun loadEnclosure(view: ImageView, enclosure: FeedItemEnclosure) {
    Glide.with(view.context)
        .load(enclosure.url)
        .into(view)
}

@BindingAdapter("loadFeedIcon")
internal fun loadFeedIcon(view: ImageView, iconUrl: String) {
    Glide.with(view.context)
        .load(iconUrl)
        .into(view)
}
