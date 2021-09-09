package com.gb.lib

import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

fun main() {
    val array = arrayOf("1", "3", "2", "4")
//    val isEven = fun(value: String): Boolean {
//        return value.toInt() % 2 == 0
//    }

    val isEven: (String) -> Boolean = { text -> text.toInt() % 2 == 0 }

    val evenArray = arrayOf(array.filter { isEven(it) })
    //val evenArray = arrayOf(array.filter(::isEven))

    println(evenArray.joinToString())
}
//
//fun isEven(c: String): Boolean = c.toInt() % 2 == 0
//



