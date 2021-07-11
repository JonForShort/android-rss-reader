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
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.jonforshort.rssreader.R
import com.github.jonforshort.rssreader.ui.main.FeedArticle
import com.github.jonforshort.rssreader.ui.main.FeedArticleAdapter
import com.github.jonforshort.rssreader.ui.main.FeedArticleViewObserver
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber.d

@AndroidEntryPoint
internal class HomeFeedFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var feedRecyclerView: RecyclerView
    private lateinit var feedArticleAdapter: FeedArticleAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    private val feedArticleViewObserver = HomeFeedArticleViewObserver()
    private val feedViewModel: HomeFeedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedArticleAdapter = FeedArticleAdapter(requireContext(), feedArticleViewObserver)
        feedRecyclerView = view.findViewById(R.id.contentRecyclerView)
        feedRecyclerView.adapter = feedArticleAdapter
        feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        feedViewModel.feedContent().observe(viewLifecycleOwner) { feedAndFeedContent ->
            val feedContent = feedAndFeedContent.second
            val feedArticles = feedContent.channel.items.map { feedItem ->
                FeedArticle(
                    feedItem.title,
                    feedItem.link,
                    feedItem.description,
                    feedItem.publishDate,
                    feedItem.enclosure,
                    feedAndFeedContent.first.providerName,
                    feedAndFeedContent.first.providerIconUrl
                )
            }
            feedArticleAdapter.submitList(feedArticles)
            swipeRefreshLayout.isRefreshing = false
        }

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(this)
    }

    override fun onResume() {
        super.onResume()
        refreshFeedContent()
    }

    override fun onRefresh() {
        refreshFeedContent()
    }

    private fun refreshFeedContent() {
        swipeRefreshLayout.isRefreshing = true
        feedViewModel.refreshFeedContent()
    }
}

private class HomeFeedArticleViewObserver : FeedArticleViewObserver {

    override fun onBookmarkClicked(view: View, feedArticle: FeedArticle) {
        d("onBookmarkClicked")
    }

    override fun onFavoriteClicked(view: View, feedArticle: FeedArticle) {
        d("onFavoriteClicked")
    }

    override fun onShareClicked(view: View, feedArticle: FeedArticle) {
        d("onShareClicked")
    }
}
