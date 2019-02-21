package org.dnltsk.pizza

import java.io.File
import kotlin.math.abs

class SlicesWriter {

    fun write(filename: String, slices: List<Slice>) {
        val sb = StringBuffer()

        sb.append(slices.count()).append("\n")
        var score = 0
        slices.forEach {
            sb.append(("${it.ul.row} ${it.ul.col} ${it.lr.row} ${it.lr.col}")).append("\n")
            score += (1 + abs(it.lr.row - it.ul.row)) * (1 + abs(it.lr.col - it.ul.col))
        }

        println(sb.toString())
        println("=> $score")
        val outfile = File("results/$filename/${score}_${System.currentTimeMillis()}.out")
        outfile.writeText(sb.toString())

    }

}
