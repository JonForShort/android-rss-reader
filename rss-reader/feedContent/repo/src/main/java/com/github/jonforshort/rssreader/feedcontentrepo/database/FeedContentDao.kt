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
package com.github.jonforshort.rssreader.feedcontentrepo.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FeedContentDao {

    @Query("SELECT * FROM FeedContentEntity ORDER BY modification_date DESC")
    suspend fun getAll(): List<FeedContentEntity>

    @Query("SELECT * FROM FeedContentEntity WHERE url = :url")
    suspend fun getByUrl(url: String): FeedContentEntity

    @Query("SELECT EXISTS(SELECT * FROM FeedContentEntity WHERE url = :url)")
    suspend fun isFeedContentExists(url: String): Boolean

    @Insert
    suspend fun insert(feedContentEntity: FeedContentEntity)

    @Delete
    suspend fun delete(feedContentEntity: FeedContentEntity)
}