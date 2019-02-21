package org.dnltsk.pizza

import org.junit.Test
import java.io.File

class PizzaTest {

    @Test
    fun a_example() {
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
}
