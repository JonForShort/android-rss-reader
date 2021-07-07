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
package com.github.jonforshort.rssreader.feed.repo

import com.github.jonforshort.rssreader.feed.datasource.LocalDataSource
import com.github.jonforshort.rssreader.feed.datasource.RemoteDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.github.jonforshort.rssreader.feed.datasource.Feed as FeedDTO

interface FeedRepo {

    fun getAll(): Flow<Feed>

    fun getByTags(tags: Collection<String>): Flow<Feed>

    fun getByProvider(provider: String): Flow<Feed>
}

fun createFeedRepo(): FeedRepo = FeedRepoImpl()

private class FeedRepoImpl : FeedRepo {

    private val dataSources = listOf(
        LocalDataSource(),
        RemoteDataSource()
    )

    override fun getAll(): Flow<Feed> = flow {
        dataSources.forEach { dataSource ->
            dataSource.get().let { feedsDTO ->
                feedsDTO.map {
                    emit(it.toFeed())
                }
            }
        }
    }

    override fun getByTags(tags: Collection<String>): Flow<Feed> = flow {
        dataSources.forEach { dataSource ->
            dataSource.get().let { feedsDTO ->
                feedsDTO.map {
                    emit(it.toFeed())
                }
            }
        }
    }

    override fun getByProvider(provider: String): Flow<Feed> = flow {
        dataSources.forEach { dataSource ->
            dataSource.get().let { feedsDTO ->
                feedsDTO.map {
                    emit(it.toFeed())
                }
            }
        }
    }
}

private fun FeedDTO.toFeed() = Feed(
    title = this.title,
    description = this.description,
    homePageUrl = this.homePageUrl,
    rssUrl = this.rssUrl,
    iconUrl = this.iconUrl
)