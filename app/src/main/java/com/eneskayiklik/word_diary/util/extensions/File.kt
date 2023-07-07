package com.eneskayiklik.word_diary.util.extensions

import java.io.File

fun File.createIfNotExists(): File {
    if (exists().not()) createNewFile()
    return this
}