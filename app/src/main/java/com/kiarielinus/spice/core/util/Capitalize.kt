package com.kiarielinus.spice.core.util

import java.util.*

fun toCamelCase(str: String): String {
    return str.trim().split(buildString {
        this.append("\\s+")
    }.toRegex())
        .asSequence()
        .map { s -> s.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.ENGLISH) else it.toString() }
        }.joinToString(" ")
}