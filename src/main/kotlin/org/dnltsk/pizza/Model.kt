package org.dnltsk.pizza

data class Config(
    val rows: Int,
    val cols: Int,
    val min: Int,
    val maxSliceCells: Int
)

enum class Topping { M, T }

data class Cell(
    val row: Int,
    val col: Int
)

data class Slice(
    val ul: Cell,
    val lr: Cell
)

enum class Dir { N, E, S, W }