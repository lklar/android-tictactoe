package com.example.tictactoe.data.db

import com.example.tictactoe.domain.Game
import com.example.tictactoe.domain.GameOutcome
import com.example.tictactoe.domain.Player
import com.google.gson.Gson

object GameMapper {
    private val gson = Gson()

    fun fromDomain(players: Pair<Player, Player>, gameOutcome: GameOutcome): GameEntity {
        return GameEntity(
            p1Name  = players.first.name,
            p2Name  = players.second.name,
            p1Icon  = players.first.drawableId,
            p2Icon  = players.second.drawableId,
            outcome = gameOutcome
        )
            // board = gson.toJson(game.board),
    }

//    fun toDomain(gameEntity: GameEntity): Game {
//        val player1 = Player(gameEntity.p1Name, gameEntity.p1Icon, 0)
//        val player2 = Player(gameEntity.p2Name, gameEntity.p2Icon, 0)
//        //val board = gson.fromJson(gameEntity.board, Array<Array<Player?>>::class.java).map { it.toMutableList() }.toMutableList()
//        val state = gameEntity.state
//        return Game(player1, player2).apply {
//            this.state = state
//        }
//    }
}