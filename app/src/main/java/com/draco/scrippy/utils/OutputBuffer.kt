package com.draco.scrippy.utils

class OutputBuffer(private val size: Int) {
    private val buffer = ArrayDeque<String>()

    fun add(string: String) {
        if (buffer.size >= size)
            buffer.removeFirst()
        buffer.add(string)
    }

    fun clear() {
        buffer.clear()
    }

    fun get(): String = buffer.joinToString(System.lineSeparator())
}