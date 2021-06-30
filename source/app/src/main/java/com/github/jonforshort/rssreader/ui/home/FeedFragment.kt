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
package com.github.jonforshort.rssreader.ui.home

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.github.jonforshort.rssreader.R
import com.github.jonforshort.rssreader.databinding.ViewFeedArticleBinding
import com.github.jonforshort.rssreader.feedcontentfetcher.FeedItem
import com.github.jonforshort.rssreader.ui.home.feed.home.FeedArticle
import com.github.jonforshort.rssreader.ui.home.feed.home.FeedViewModel
import java.net.URL

internal class FeedFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {

    private lateinit var feedViewModel: FeedViewModel
    private lateinit var feedRecyclerView: RecyclerView
    private lateinit var feedArticleAdapter: FeedArticleAdapter
    private lateinit var swipeRefreshLayout: SwipeRefreshLayout

    enum class FeedType {
        HOME,
        POPULAR,
        BOOKMARK
    }

    companion object {

        private const val ARGUMENT_FEED_TYPE = "FeedType"

        fun encodeFragmentArguments(feedType: FeedType) = Bundle().apply {
            putString(ARGUMENT_FEED_TYPE, feedType.toString())
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        feedViewModel = ViewModelProvider(this).get(FeedViewModel::class.java)
        feedArticleAdapter = FeedArticleAdapter(requireContext())
        feedRecyclerView = view.findViewById(R.id.contentRecyclerView)
        feedRecyclerView.adapter = feedArticleAdapter
        feedRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        feedViewModel.getFeedContentLiveData().observe(viewLifecycleOwner) {
            feedArticleAdapter.submitList(it.channel.items)
            swipeRefreshLayout.isRefreshing = false
        }

        swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout) as SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(this)

        feedViewModel.getFeedUrls().value = listOf(
            URL("https://www.nasa.gov/rss/dyn/breaking_news.rss")
        )
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

private class FeedArticleAdapter(private val context: Context) :
    ListAdapter<FeedItem, FeedArticleAdapter.ViewHolder>(FeedItemDiffer()) {

    class ViewHolder(private val binding: ViewFeedArticleBinding, private val context: Context) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FeedItem) {
            binding.feedArticle = FeedArticle(
                item.title,
                item.link,
                item.description,
                item.publishDate,
                item.enclosure
            )
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                CustomTabsIntent
                    .Builder()
                    .build()
                    .launchUrl(context, Uri.parse(item.link))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val feedItemBinding = ViewFeedArticleBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(feedItemBinding, context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feedItem = getItem(position)
        holder.bind(feedItem)
    }

    class FeedItemDiffer : DiffUtil.ItemCallback<FeedItem>() {

        override fun areItemsTheSame(oldItem: FeedItem, newItem: FeedItem) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: FeedItem, newItem: FeedItem) =
            oldItem == newItem
    }
}