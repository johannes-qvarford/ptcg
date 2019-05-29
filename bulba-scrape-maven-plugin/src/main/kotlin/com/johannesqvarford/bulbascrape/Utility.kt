package com.johannesqvarford.bulbascrape

fun String.toUtf8ByteArray() = this.toByteArray(Charsets.UTF_8)

fun ByteArray.toUtf8String() = this.toString(Charsets.UTF_8)
