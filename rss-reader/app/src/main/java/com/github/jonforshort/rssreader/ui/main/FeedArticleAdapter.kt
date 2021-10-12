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

internal class FeedArticleAdapter(
    private val context: Context,
    private val viewObserver: FeedArticleViewObserver
) : ListAdapter<FeedArticle, FeedArticleAdapter.ViewHolder>(FeedArticleDiffer()) {

    class ViewHolder(
        private val binding: ViewFeedArticleBinding,
        private val context: Context,
        private val viewObserver: FeedArticleViewObserver
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FeedArticle) {
            binding.feedArticleEventListener = viewObserver
            binding.feedArticle = item
            binding.executePendingBindings()
            binding.root.setOnClickListener {
                CustomTabsIntent
                    .Builder()
                    .build()
                    .launchUrl(context, Uri.parse(item.linkUrl.toString()))
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val feedArticleBinding = ViewFeedArticleBinding.inflate(layoutInflater, parent, false)
        return ViewHolder(feedArticleBinding, context, viewObserver)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val feedArticle = getItem(position)
        holder.bind(feedArticle)
    }

    class FeedArticleDiffer : DiffUtil.ItemCallback<FeedArticle>() {

        override fun areItemsTheSame(oldItem: FeedArticle, newItem: FeedArticle) =
            oldItem == newItem

        override fun areContentsTheSame(oldItem: FeedArticle, newItem: FeedArticle) =
            oldItem == newItem
    }
}
