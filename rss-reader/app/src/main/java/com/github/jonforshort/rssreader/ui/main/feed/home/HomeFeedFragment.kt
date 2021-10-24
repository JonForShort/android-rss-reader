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
package com.github.jonforshort.rssreader.ui.main.feed.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.github.jonforshort.rssreader.ui.main.feed.FeedFragment
import com.github.jonforshort.rssreader.ui.main.feedArticle.FeedArticle
import com.github.jonforshort.rssreader.ui.main.feedArticle.FeedArticleFactory
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class HomeFeedFragment : FeedFragment() {

    private val feedViewModel: HomeFeedViewModel by viewModels()
    private val feedArticlesCache = mutableListOf<FeedArticle>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedViewModel.feedContent().observe(viewLifecycleOwner) { feedAndFeedContent ->
            val feedContent = feedAndFeedContent.second
            val currentFeedArticles = feedContent.channel.items.map { feedItem ->
                FeedArticleFactory.from(feedAndFeedContent.first, feedItem)
            }
            feedArticlesCache.addAll(currentFeedArticles)
            feedArticlesCache.sortByDescending { it.publishTimeInMs }
            feedArticles().postValue(feedArticlesCache)
        }
    }

    override fun onRefreshFeedContent() {
        feedViewModel.refreshFeedContent()
    }
}
