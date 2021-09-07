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
package com.github.jonforshort.rssreader.ui.feeds

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.jonforshort.rssreader.feedsource.repo.FeedRepository
import com.github.jonforshort.rssreader.preferences.UserPreferences
import com.github.jonforshort.rssreader.utils.flatMapToList
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class FeedSelectionViewModel @Inject constructor(
    private val feedRepository: FeedRepository,
    private val userPreferences: UserPreferences
) : ViewModel() {

    val feedItems = MutableLiveData<List<Pair<String, Boolean>>>()

    suspend fun loadFeedItems() {
        val feedTags = feedRepository.getAllTags().flatMapToList()
        val selectedFeedTags = userPreferences.getSelectedFeedTags()
        feedItems.value = feedTags.map { feedTag ->
            feedTag to selectedFeedTags.contains(feedTag)
        }
    }

    fun onFeedSelectionStateChanged(feedTag: String, isSelected: Boolean) {
        if (isSelected) {
            userPreferences.selectFeedTag(feedTag)
        } else {
            userPreferences.unselectFeedTag(feedTag)
        }
    }
}
