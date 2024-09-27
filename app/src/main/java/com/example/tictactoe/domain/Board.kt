package com.example.tictactoe.domain

import io.reactivex.rxjava3.subjects.PublishSubject

enum class CellState {
    EMPTY, PLAYER_1, PLAYER_2
}

data class CellChange(
    val row: Int,
    val col: Int,
    val state: CellState
)

class Board {
    private val cells = List(3) { MutableList(3) { CellState.EMPTY } }
    val cellChangePublisher = PublishSubject.create<CellChange>()

    fun clear() {
        (0..2).forEach { row ->
            (0..2).forEach { col ->
                set(row, col, CellState.EMPTY)
            }
        }
    }

    fun set(row: Int, col: Int, cellState: CellState)
    {
        if(cells[row][col] == CellState.EMPTY || cellState == CellState.EMPTY)
        {
            cells[row][col] = cellState
            cellChangePublisher.onNext(CellChange(row, col, cellState))
        }
    }
}



//class Board {
//    var cells = MutableList(3) { MutableList<Player?>(3) { null } }
//
//    /**
//     * Resets all cells on the board to null.
//     */
//    fun resetCells() {
//        cells = MutableList(3) { MutableList<Player?>(3) { null } }
//    }
//
//    /**
//     * Sets a cell on the board to the given player's symbol.
//     *
//     * @param cellId The ID of the cell to set (1-9) in a Numpad-like order.
//     * @param player The player whose symbol will be set in the cell.
//     * @return True if the cell was successfully set, false if the cell was already occupied or the cellId was invalid.
//     */
//    fun set(row: Int, col: Int, player: Player?): Boolean {
//        if(cells[row][col] == null)
//        {
//            cells[row][col] = player
//            return true
//        } else {
//            return false
//        }
//    }
//
//    /**
//     * Checks if there are any empty cells on the board.
//     *
//     * @return True if there are empty cells, false otherwise.
//     */
//    fun hasEmptyCells(): Boolean {
//        return cells.any {row -> row.any { it == null}}
//    }
//
//    /**
//     * Calculates the number of winning lines for the given player.
//     *
//     * @param player The player for whom to calculate the winning lines.
//     * @return The number of winning lines for the player.
//     */
//    fun calcLineCount(player: Player?): Int {
//        if(player == null) return 0
//        var winCount = 0
//        for (row in cells)
//        {
//            if(row.all { it == player })
//                winCount++
//        }
//        for (col in 0..2)
//        {
//            if(cells.map { row -> row[col] }.all { it == player })
//                winCount++
//        }
//        // Check diagonals
//        if((0..2).all {cells[it][it] == player})
//            winCount++
//        if((0..2).all {cells[it][2-it] == player})
//            winCount++
//        return winCount
//    }
//
//    override fun toString(): String {
//        return cells.map { row ->
//            row.map { cell ->
//                cell?.name?.last() ?: "0"
//            }.joinToString(",")
//        }.joinToString("|")
//    }
//}