package com.example.tictactoe.domain

import com.example.tictactoe.R
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import kotlin.properties.Delegates

class Player {
    var name:       String by Delegates.observable("") { _, _, _ -> publisher.onNext(this) }
    var drawableId: Int    by Delegates.observable(0)  { _, _, _ -> publisher.onNext(this) }
    var score:      Int    by Delegates.observable(0)  { _, _, _ -> publisher.onNext(this) }
    val publisher = BehaviorSubject.create<Player>()

    init {
        publisher.onNext(this)
    }
}

class PlayerManager {
    val players = Pair(Player(), Player())
    private val _playersObservable = Observable.combineLatest(
        players.first.publisher,
        players.second.publisher
    ) {
            p1, p2 -> Pair(p1, p2)
    }

    fun fetchNames():       Observable<Pair<String, String>> = _playersObservable.map { (p1, p2) -> Pair(p1.name,       p2.name)       }.distinctUntilChanged()
    fun fetchDrawableIds(): Observable<Pair<Int, Int>>       = _playersObservable.map { (p1, p2) -> Pair(p1.drawableId, p2.drawableId) }.distinctUntilChanged()
    fun fetchScores():      Observable<Pair<Int, Int>>       = _playersObservable.map { (p1, p2) -> Pair(p1.score,      p2.score)      }.distinctUntilChanged()

    fun updateScoreOnGameOutcome(gameOutcome: GameOutcome)
    {
        when(gameOutcome)
        {
            GameOutcome.WIN_PLAYER_1 -> players.first.score++
            GameOutcome.WIN_PLAYER_2 -> players.second.score++
            GameOutcome.DRAW -> {}
        }
    }
}