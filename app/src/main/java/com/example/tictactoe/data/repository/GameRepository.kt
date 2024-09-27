package com.example.tictactoe.data.repository

import androidx.annotation.WorkerThread
import com.example.tictactoe.data.db.GameDao
import com.example.tictactoe.data.db.GameEntity
import com.example.tictactoe.domain.Game
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.runBlocking

class GameRepository(private val gameDao: GameDao) {

    val allGames: Flow<List<GameEntity>> = gameDao.getAllGames()

    @WorkerThread
    suspend fun insert(gameEntity: GameEntity) {
        gameDao.insert(gameEntity)
    }

    @WorkerThread
    suspend fun getGamesWithPlayers(p1: String, p2: String): List<GameEntity>
    {
        val resultList = mutableListOf<GameEntity>()
        runBlocking {
            gameDao.getGamesWithPlayers(p1, p2).collect { list ->
                resultList.addAll(list)
            }
        }
        return resultList
    }
}