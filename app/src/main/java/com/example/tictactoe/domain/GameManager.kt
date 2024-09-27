package com.example.tictactoe.domain

import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.PublishSubject
import com.example.tictactoe.R
import com.example.tictactoe.data.db.GameEntity
import com.example.tictactoe.data.repository.GameRepository
import io.reactivex.rxjava3.core.Single


class GameManager(
    private val gameRepository: GameRepository
) {
    private val gameObservablePublisher = PublishSubject.create<Observable<CellChange>>()
    private val gameOutcomePublisher = PublishSubject.create<Single<GameOutcome>>()
    private var game = Game()

    fun fetchCellChanges(): Observable<CellChange> {
        return game.fetchCellChanges()
    }

    fun processInput(row: Int, col: Int) {
        game.processTurn(row, col)
    }

    fun startGame() {
        game.reset()
        gameOutcomePublisher.onNext( game.fetchGameOutcome() )
    }

    fun fetchGameOutcome(): Observable<GameOutcome> {
        return gameOutcomePublisher.switchMap { it.toObservable() }
    }
}