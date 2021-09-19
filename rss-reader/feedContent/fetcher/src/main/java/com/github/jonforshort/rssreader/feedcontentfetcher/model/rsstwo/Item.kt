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
package com.github.jonforshort.rssreader.feedcontentfetcher.model.rsstwo

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty

internal class Item {

    var title = ""

    var link = ""

    var description = ""

    var author = ""

    var category = ""

    var comments = ""

    var enclosure: Enclosure = Enclosure()

    @JacksonXmlProperty(namespace = "media", localName = "content")
    var mediaContent: MediaContent = MediaContent()

    @JacksonXmlProperty(namespace = "media", localName = "credit")
    var mediaCredit: MediaCredit = MediaCredit()

    var guid = ""

    var pubDate = ""

    var source = ""
}

internal data class Enclosure(

    var url: String = "",

    var mimeType: String = "",

    var lengthInBytes: String? = null
)

internal data class MediaContent(

    var url: String = "",

    var height: Int = -1,

    var width: Int = -1
)

internal data class MediaCredit(

    var role: String = "",
)
