package com.example.tictactoe.domain

import kotlin.random.Random

// data class Player(var name: String, var drawableId: Int, var score: Int)

//class Players(player1: Player, player2: Player) {
//    private val players = listOf(player1, player2)
//    private var currentPlayerIndex = 0
//
//    fun getPlayer(id: Int): Player? {
//        return if(id < players.size) players[id] else null
//    }
//
//    fun currentPlayer(): Player {
//        return players[currentPlayerIndex]
//    }
//
//    fun switchPlayer() {
//        currentPlayerIndex = (currentPlayerIndex + 1) % players.size
//    }
//
//    fun randomizeCurrentPlayer() {
//        currentPlayerIndex = Random.nextInt() % players.size
//    }
//}