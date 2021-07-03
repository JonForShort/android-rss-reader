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

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.github.jonforshort.rssreader.databinding.ViewFeedArticleBinding
import com.github.jonforshort.rssreader.feedcontentfetcher.FeedItem

internal class FeedArticleAdapter(
    private val context: Context,
    private val viewObserver: FeedArticleViewObserver
) : ListAdapter<FeedItem, FeedArticleAdapter.ViewHolder>(FeedItemDiffer()) {

    class ViewHolder(
        private val binding: ViewFeedArticleBinding,
        private val context: Context,
        private val viewObserver: FeedArticleViewObserver
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FeedItem) {
            binding.feedArticleEventListener = viewObserver
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
        return ViewHolder(feedItemBinding, context, viewObserver)
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