package com.varabyte.kotter.foundation.input

val Key.name: String
    get() {
        val s = this.toString()
        return if (s.length == 1) {
            if (s[0] == ' ') "SPACE" else s
        } else {
            s.uppercase()
        }
    }