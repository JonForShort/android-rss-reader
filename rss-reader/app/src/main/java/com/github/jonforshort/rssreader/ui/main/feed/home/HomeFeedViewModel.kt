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

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.jonforshort.rssreader.feedcontentfetcher.FeedContent
import com.github.jonforshort.rssreader.feedcontentrepo.FeedContentRepository
import com.github.jonforshort.rssreader.feedsource.repo.Feed
import com.github.jonforshort.rssreader.feedsource.repo.FeedRepository
import com.github.jonforshort.rssreader.preferences.UserPreferences
import com.github.jonforshort.rssreader.ui.main.feedArticle.FeedArticle
import com.github.jonforshort.rssreader.ui.main.feedArticle.database.FeedArticleRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.net.URL
import javax.inject.Inject

internal typealias FeedAndFeedContent = Pair<Feed, FeedContent>

@HiltViewModel
internal class HomeFeedViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val feedArticleRepository: FeedArticleRepository,
    private val feedContentRepository: FeedContentRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    private val feedContentLiveData = MutableLiveData<FeedAndFeedContent>()

    fun feedContent() = feedContentLiveData

    fun refreshFeedContent() {
        viewModelScope.launch {
            val selectedTags = userPreferences.getSelectedFeedTags()
            feedRepository.getByTags(selectedTags).collect { feed ->
                val url = URL(feed.rssUrl)
                feedContentRepository.fetch(url).collect { feedContent ->
                    feedContentLiveData.postValue(Pair(feed, feedContent))
                }
            }
        }
    }

    fun bookmarkArticle(feedArticle: FeedArticle) {
        viewModelScope.launch {
            feedArticleRepository.bookmarkArticle(feedArticle)
        }
    }

    fun unbookmarkArticle(feedArticle: FeedArticle) {
        viewModelScope.launch {
            feedArticleRepository.unbookmarkArticle(feedArticle)
        }
    }
}
