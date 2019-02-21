package org.dnltsk.pizza

import java.io.File

fun main(args: Array<String>) {
    //val filename = "a_example.in"
    //val filename = "b_small.in"
    val filename = "c_medium.in"
    //val filename = "d_big.in"
    File("results/$filename/").mkdirs()
    val file = File(filename)
    for (i in 0..100) {
        val read = PizzaReader().read(file)
        val slices = PizzaSlicer(read.first, read.second).slice()
        SlicesWriter().write(filename, slices)
    }
}