package org.dnltsk.pizza

import kotlin.math.abs

class PizzaSlicer(val config: Config,
                  val cells: List<List<Topping>>) {

    private val allDirs = Dir.values().asList()

    private val notProcessed: MutableMap<Int, MutableSet<Int>> = hashMapOf()

    fun slice(): List<Slice> {

        for (row in 0..(config.rows - 1)) {
            notProcessed[row] = mutableSetOf()
            for (col in 0..(config.cols - 1)) {
                notProcessed[row]!!.add(col)
            }
        }

        val slices = mutableListOf<Slice>()
        for (i in 0..(config.cols * config.rows)) {
            println("processing slice $i")
            val center = randomCenter()
            println("new center: $center")
            if (center == null) {
                println("no new center found!")
                return slices
            }
            var slice = Slice(center.copy(), center.copy())
            while (true) {
                val increasedSlice = increase(slice)
                if (increasedSlice == null) {
                    println("no grow possible!")
                    if (isSliceCompleted(slice)) {
                        println("slice before finished!")
                        slices.add(slice)
                        commitProcessedCells(slice)
                    } else {
                        println("center cannot result in slice!")
                    }
                    break
                }
                slice = increasedSlice
            }
        }
        return slices
    }

    private fun commitProcessedCells (slice: Slice) {
        println("commit slice $slice")
        for (row in (slice.ul.row)..(slice.lr.row)) {
            for (col in (slice.ul.col)..(slice.lr.col)) {
                notProcessed[row]!!.remove(col)
            }
            if(notProcessed[row]!!.isEmpty()){
                notProcessed.remove(row)
            }
        }
    }

    private fun increase(slice: Slice): Slice? {
        val dirs = allDirs.toMutableList()
        allDirs.forEach {
            when (it) {
                Dir.N ->
                    when {
                        slice.ul.row == 0 -> dirs.remove(Dir.N)
                        newRowIsProcessed(slice.ul.row - 1, slice) -> dirs.remove(Dir.N)
                        isSliceInvalid(Slice(slice.ul.copy(row = slice.ul.row - 1), slice.lr)) -> dirs.remove(Dir.N)
                    }
                Dir.E ->
                    when {
                        slice.lr.col == config.cols - 1 -> dirs.remove(Dir.E)
                        newColIsProcessed(slice.lr.col + 1, slice) -> dirs.remove(Dir.E)
                        isSliceInvalid(Slice(slice.ul, slice.lr.copy(col = slice.lr.col + 1))) -> dirs.remove(Dir.E)
                    }
                Dir.S ->
                    when {
                        slice.lr.row == config.rows - 1 -> dirs.remove(Dir.S)
                        newRowIsProcessed(slice.lr.row + 1, slice) -> dirs.remove(Dir.S)
                        isSliceInvalid(Slice(slice.ul, slice.lr.copy(row = slice.lr.row + 1))) -> dirs.remove(Dir.S)
                    }
                Dir.W ->
                    when {
                        slice.ul.col == 0 -> dirs.remove(Dir.W)
                        newColIsProcessed(slice.ul.col - 1, slice) -> dirs.remove(Dir.W)
                        isSliceInvalid(Slice(slice.ul.copy(col = slice.ul.col - 1), slice.lr)) -> dirs.remove(Dir.W)
                    }
            }
        }
        if (dirs.isEmpty()) {
            return null
        }
        val newSlice =
            when (dirs[random(0, dirs.size - 1)]) {
                Dir.N -> Slice(slice.ul.copy(row = slice.ul.row - 1), slice.lr)
                Dir.E -> Slice(slice.ul, slice.lr.copy(col = slice.lr.col + 1))
                Dir.S -> Slice(slice.ul, slice.lr.copy(row = slice.lr.row + 1))
                Dir.W -> Slice(slice.ul.copy(col = slice.ul.col - 1), slice.lr)
            }

        //println("")
        //println("from: $slice")
        //println("to:   $newSlice")

        return newSlice
    }

    private fun newRowIsProcessed(newRow: Int, slice: Slice): Boolean {
        for (col in (slice.ul.col)..(slice.lr.col)) {
            if (notProcessed[newRow] == null || !notProcessed[newRow]!!.contains(col)) {
                return true
            }
        }
        return false
    }

    private fun newColIsProcessed(newCol: Int, slice: Slice): Boolean {
        for (row in (slice.ul.row)..(slice.lr.row)) {
            if (notProcessed[row] == null || !notProcessed[row]!!.contains(newCol)) {
                return true
            }
        }
        return false
    }

    private fun isSliceCompleted(slice: Slice): Boolean {
        val count = mutableMapOf<Topping, Int>()
        Topping.values().forEach {
            count[it] = 0
        }
        for (row in (slice.ul.row)..(slice.lr.row)) {
            for (col in (slice.ul.col)..(slice.lr.col)) {
                count[cells[row][col]] = count[cells[row][col]]!! + 1
            }
        }
        val min = Topping.values().map {
            count.getOrDefault(it, 0)
        }.min()

        return min ?: 0 >= config.min
    }

    //TODO: Only check new cells
    private fun isSliceInvalid(slice: Slice): Boolean {
        val numCells = (1 + abs(slice.lr.row - slice.ul.row)) * (1 + abs(slice.lr.col - slice.ul.col))
        if (numCells > config.maxSliceCells) {
            return true
        }
        return false
    }

    private fun randomCenter(): Cell? {
        if(notProcessed.keys.isEmpty()){
            return null
        }
        val row = notProcessed.keys.elementAt(random(0,notProcessed.keys.size-1))
        val col = notProcessed[row]!!.elementAt(random(0,notProcessed[row]!!.size-1))
        return Cell(row, col)
    }

    private fun random(from: Int, toIncl: Int): Int {
        return (from..toIncl).random()
    }

}
