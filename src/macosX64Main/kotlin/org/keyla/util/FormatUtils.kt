package org.keyla.util

actual fun format1f(value: Double): String {
    val rounded = (value * 10).toInt() / 10.0
    return rounded.toString()
} 