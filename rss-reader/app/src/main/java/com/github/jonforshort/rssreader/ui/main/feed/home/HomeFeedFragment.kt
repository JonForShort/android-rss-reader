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

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.jonforshort.rssreader.R
import com.github.jonforshort.rssreader.ui.main.feedArticle.FeedArticle
import com.github.jonforshort.rssreader.ui.main.feedArticle.FeedArticleAdapter
import com.github.jonforshort.rssreader.ui.main.feedArticle.FeedArticleFactory
import com.github.jonforshort.rssreader.ui.main.feedArticle.FeedArticleViewObserver
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber.d

@AndroidEntryPoint
internal class HomeFeedFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var feedRecyclerView: RecyclerView
    private lateinit var feedArticleAdapter: FeedArticleAdapter
    private lateinit var feedArticleViewObserver: HomeFeedArticleViewObserver
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout
    private lateinit var bookmarkObserver: HomeFeedBookmarkObserver

    private val feedViewModel: HomeFeedViewModel by viewModels()
    private val feedArticles = mutableListOf<FeedArticle>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bookmarkObserver = HomeFeedBookmarkObserver(feedViewModel)
        feedArticleViewObserver = HomeFeedArticleViewObserver(bookmarkObserver)
        feedArticleAdapter = FeedArticleAdapter(requireContext(), feedArticleViewObserver)
        feedRecyclerView = view.findViewById(R.id.contentRecyclerView)
        feedRecyclerView.adapter = feedArticleAdapter
        feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        feedViewModel.feedContent().observe(viewLifecycleOwner) { feedAndFeedContent ->
            val feedContent = feedAndFeedContent.second
            val currentFeedArticles = feedContent.channel.items.map { feedItem ->
                FeedArticleFactory.from(feedAndFeedContent.first, feedItem)
            }
            feedArticles.addAll(currentFeedArticles)
            feedArticles.sortByDescending { it.publishTimeInMs }
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

private class HomeFeedBookmarkObserver(
    private val homeFeedViewModel: HomeFeedViewModel
) : HomeFeedArticleViewObserver.BookmarkObserver {

    override fun onBookmarkArticle(feedArticle: FeedArticle) =
        homeFeedViewModel.bookmarkArticle(feedArticle)

    override fun onUnbookmarkArticle(feedArticle: FeedArticle) =
        homeFeedViewModel.unbookmarkArticle(feedArticle)
}

private class HomeFeedArticleViewObserver(
    private val bookmarkObserver: BookmarkObserver
) : FeedArticleViewObserver {

    interface BookmarkObserver {
        fun onBookmarkArticle(feedArticle: FeedArticle)

        fun onUnbookmarkArticle(feedArticle: FeedArticle)
    }

    override fun onBookmarkClicked(view: View, feedArticle: FeedArticle) {
        d("onBookmarkClicked")
        val imageView = view as? ImageView
        when (imageView?.tag) {
            R.drawable.ic_bookmark -> {
                imageView.tag = R.drawable.ic_bookmark_border
                imageView.setImageResource(R.drawable.ic_bookmark_border)

                bookmarkObserver.onUnbookmarkArticle(feedArticle)
            }
            else -> {
                imageView?.tag = R.drawable.ic_bookmark
                imageView?.setImageResource(R.drawable.ic_bookmark)

                bookmarkObserver.onBookmarkArticle(feedArticle)
            }
        }
    }

    override fun onFavoriteClicked(view: View, feedArticle: FeedArticle) {
        d("onFavoriteClicked")
        val imageView = view as? ImageView
        when (imageView?.tag) {
            R.drawable.ic_favorite -> {
                imageView.tag = R.drawable.ic_favorite_border
                imageView.setImageResource(R.drawable.ic_favorite_border)
            }
            else -> {
                imageView?.tag = R.drawable.ic_favorite
                imageView?.setImageResource(R.drawable.ic_favorite)
            }
        }
    }

    override fun onShareClicked(view: View, feedArticle: FeedArticle) {
        d("onShareClicked")
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Sharing Feed URL")
            putExtra(Intent.EXTRA_TEXT, feedArticle.linkUrl.toString())
        }
        val chooserIntent = Intent.createChooser(intent, "Share URL")
        view.context.startActivity(chooserIntent)
    }
}
