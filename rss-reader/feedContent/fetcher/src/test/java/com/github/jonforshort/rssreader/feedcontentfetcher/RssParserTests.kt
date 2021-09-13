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
package com.github.jonforshort.rssreader.feedcontentfetcher

import org.junit.Assert.*
import org.junit.Test
import java.io.File

class RssParserTests {

    @Test
    fun testParse_validTestPageOne_expectParserToSucceed() {
        val testPageResource = RssParserTests::class.java.classLoader.getResource("test_page_1.xml")
        val testPageContent = File(testPageResource!!.toURI()).readLines().joinToString("")
        val parsedContent = RssParser().parse(testPageContent)
        assertEquals(parsedContent.version, "2.0")
        assertEquals(parsedContent.channel.items.size, 50)

        parsedContent.channel.items.forEach { feedItem ->
            assertTrue(feedItem.title.isNotEmpty())
            assertTrue(feedItem.description.isEmpty())
            assertTrue(feedItem.link.isNotEmpty())
            assertTrue(feedItem.publishDate.isNotEmpty())
        }
    }

    @Test
    fun testParse_validTestPageTwo_expectParserToSucceed() {
        val testPageResource = RssParserTests::class.java.classLoader.getResource("test_page_2.xml")
        val testPageContent = File(testPageResource!!.toURI()).readLines().joinToString("")
        val parsedContent = RssParser().parse(testPageContent)
        assertEquals(parsedContent.version, "2.0")
        assertEquals(parsedContent.channel.items.size, 25)

        parsedContent.channel.items.forEach { feedItem ->
            assertTrue(feedItem.title.isNotEmpty())
            assertTrue(feedItem.description.isNotEmpty())
            assertTrue(feedItem.link.isNotEmpty())
            assertTrue(feedItem.publishDate.isNotEmpty())
        }
    }

    @Test
    fun testParse_validTestPageThree_expectParserToSucceed() {
        val testPageResource = RssParserTests::class.java.classLoader.getResource("test_page_3.xml")
        val testPageContent = File(testPageResource!!.toURI()).readLines().joinToString("")
        val parsedContent = RssParser().parse(testPageContent)
        assertEquals(parsedContent.version, "2.0")
        assertEquals(parsedContent.channel.items.size, 10)

        parsedContent.channel.items.forEach { feedItem ->
            assertTrue(feedItem.title.isNotEmpty())
            assertTrue(feedItem.publishDate.isNotEmpty())
            assertTrue(feedItem.description.isNotEmpty())
            assertTrue(feedItem.link.isNotEmpty())
            assertNotNull(feedItem.enclosure)
            assertTrue(feedItem.enclosure?.url?.isNotBlank() ?: false)
        }
    }
}
