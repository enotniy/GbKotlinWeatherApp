package com.gb.lib

import kotlin.random.Random

fun main() {
    val text: String = "1, 2, 5, 7, 10, 3, 5"

    println(
        text.split(", ")
            .filter { it.isNotBlank() }
            .map { str -> str.toInt() }
            .sum()
    )
}

interface Super {
    fun fun1()
}