package org.dnltsk.pizza

import java.io.File

class PizzaReader {

    fun read(file: File): Pair<Config, List<List<Topping>>>{
        var config : Config? = null
        val cells = file.readLines().mapIndexed { i, line ->
            if(i == 0){
                config = parseConfig(line)
                null
            }else{
                parseRow(line)
            }
        }.filterNotNull()
        return Pair(config!!, cells)
    }

    private fun parseRow(line: String): List<Topping> {
        return line.map { Topping.valueOf(it.toString()) }.toMutableList()
    }

    private fun parseConfig(line: String) : Config{
        val tokens = line.trim().split(" ")
        return Config(tokens[0].toInt(), tokens[1].toInt(), tokens[2].toInt(), tokens[3].toInt())
    }

}
