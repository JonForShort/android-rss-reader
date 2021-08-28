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
package com.github.jonforshort.rssreader.feedsource.repo

import com.github.jonforshort.rssreader.feedsource.datasource.LocalDataSource
import com.github.jonforshort.rssreader.feedsource.datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.github.jonforshort.rssreader.feedsource.datasource.Feed as FeedDTO

interface FeedRepository {

    fun getAll(): Flow<Feed>

    fun getAllTags(): Flow<Collection<String>>

    fun getByTags(tags: Collection<String>): Flow<Feed>

    fun getByProvider(provider: String): Flow<Feed>
}

fun createFeedRepo(): FeedRepository = FeedRepositoryImpl()

private class FeedRepositoryImpl : FeedRepository {

    private val dataSources = listOf(
        LocalDataSource(),
        RemoteDataSource()
    )

    override fun getAll(): Flow<Feed> = flow {
        dataSources.forEach { dataSource ->
            dataSource.get().let { feedChannel ->
                feedChannel.feed?.map { feedDto ->
                    emit(feedDto.toFeed())
                }
            }
        }
    }

    override fun getAllTags(): Flow<List<String>> = flow {
        dataSources.forEach { dataSource ->
            dataSource.get().let { feedChannel ->
                feedChannel.feed?.forEach { feedDto ->
                    emit(feedDto.tags)
                }
            }
        }
    }

    override fun getByTags(tags: Collection<String>): Flow<Feed> = flow {
        dataSources.forEach { dataSource ->
            dataSource.get().let { feedChannel ->
                feedChannel.feed
                    .map { it.toFeed() }
                    .filter { it.tags.any { tag -> tags.contains(tag) } }
                    .forEach { emit(it) }
            }
        }
    }

    override fun getByProvider(provider: String): Flow<Feed> = flow {
        dataSources.forEach { dataSource ->
            dataSource.get().let { feedChannel ->
                feedChannel.feed.map {
                    emit(it.toFeed())
                }
            }
        }
    }
}

private fun FeedDTO.toFeed() = Feed(
    providerName = this.provider,
    providerHomePageUrl = this.homePageUrl,
    providerIconUrl = this.iconUrl,
    title = this.title,
    description = this.description,
    rssUrl = this.rssUrl,
    tags = this.tags
)
