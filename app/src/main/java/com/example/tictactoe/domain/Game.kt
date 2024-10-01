package com.example.tictactoe.domain

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.PublishSubject


class Game() {
    private val board = Board()
    private var nextCellState: CellState = listOf(CellState.PLAYER_1, CellState.PLAYER_2).random()
    private val compositeDisposable = CompositeDisposable()
    private val playerCellChanges = board.cellChangePublisher.filter { cellChange -> cellChange.state != CellState.EMPTY }
    private val nextCellStatePublisher = PublishSubject.create<CellState>()

    init {
        playerCellChanges.subscribe {
            if(nextCellState == CellState.PLAYER_1)
                nextCellState = CellState.PLAYER_2
            else
                nextCellState = CellState.PLAYER_1
            nextCellStatePublisher.onNext(nextCellState)
        }.apply { compositeDisposable.add(this) }
    }

    fun reset()
    {
        board.clear()
        nextCellState = listOf(CellState.PLAYER_1, CellState.PLAYER_2).random()
        nextCellStatePublisher.onNext(nextCellState)
    }

    fun processTurn(row: Int, col: Int)
    {
        board.set(
            row = row,
            col = col,
            cellState = nextCellState
        )
    }

    fun fetchNextCellState(): Observable<CellState> {
        return nextCellStatePublisher
    }

    fun fetchCellChanges(): Observable<CellChange>
    {
        return board.cellChangePublisher
    }

    fun fetchGameOutcome(): Single<GameOutcome>
    {
        val lineChecks = listOf(
            // Row checks
            (0..2).map { checkedRow -> { cellChange: CellChange -> cellChange.row == checkedRow }},
            // Column checks
            (0..2).map { checkedCol -> { cellChange: CellChange -> cellChange.col == checkedCol }},
            // Diagonal checks
            listOf(
                { cellChange: CellChange -> cellChange.row == cellChange.col },
                { cellChange: CellChange -> cellChange.row == 2 - cellChange.col }
            )
        ).flatten()

        val lineObservables = lineChecks.map { lineCheck ->
            playerCellChanges
                .filter { lineCheck(it) }
                .take(3)
        }

        // A player observable emits either a single playerWinOutcome signal when one of the line observables
        // has emitted three player items, or emits nothing at all.
        val createPlayerObservables = { playerWinCondition: CellState, playerWinOutcome: GameOutcome ->
            lineObservables.map { lineObservable ->
                lineObservable
                    .all { it.state == playerWinCondition }
                    .flatMapObservable { playerWon ->
                        if(playerWon)
                            Observable.just(playerWinOutcome)
                        else
                            Observable.empty()
                    }
            }
        }

        val p1Observable = createPlayerObservables(CellState.PLAYER_1, GameOutcome.WIN_PLAYER_1)
        val p2Observable = createPlayerObservables(CellState.PLAYER_2, GameOutcome.WIN_PLAYER_2)

        val gameResult = listOf(p1Observable, p2Observable)
            .flatten()
            .let { observables -> Observable.merge(observables) }
            .first(GameOutcome.DRAW)

        return gameResult
    }

//    fun checkWin(): GameState?
//    {
//        var lineCount = 0
//        for (row in board)
//        {
//            if(row.all { it == currentPlayer })
//                lineCount++
//        }
//        for (col in 0..2)
//        {
//            if(board.map { row -> row[col] }.all { it == currentPlayer })
//                lineCount++
//        }
//        // Check diagonals
//        if((0..2).all { board[it][it] == currentPlayer })
//            lineCount++
//        if((0..2).all { board[it][2-it] == currentPlayer })
//            lineCount++
//        if(lineCount != 0) {
//            return if(currentPlayer == Players.PLAYER1) GameState.WIN_PLAYER_1 else GameState.WIN_PLAYER_2
//        } else {
//            // Check if there are still any empty cells left
//            if(board.any {row -> row.any { it == null}}) {
//                currentPlayer = if (currentPlayer == Players.PLAYER1) Players.PLAYER2 else Players.PLAYER1
//            } else {
//                return GameState.DRAW
//            }
//        }
//        return null
//    }
}