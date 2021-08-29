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
package com.github.jonforshort.rssreader.feedcontentfetcher.utils

import java.text.ParseException
import java.text.SimpleDateFormat

@SuppressWarnings("SimpleDateFormat")
private val dateParsers = mutableListOf(
    SimpleDateFormat("EEE, dd MMM yyyy HH:mm Z"),
    SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z"),
    SimpleDateFormat("yyyy-dd-MM'T'HH:mm:ss'Z'"),
)

internal fun convertDateStringToTimeInMs(date: String): Long {
    dateParsers.forEach { parser ->
        try {
            return parser.parse(date).time
        } catch (e: ParseException) {
            // Try next parser if format cannot be parsed.
        }
    }
    throw IllegalArgumentException("unable to parse date string [$date]")
}
