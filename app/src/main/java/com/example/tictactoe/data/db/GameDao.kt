package com.example.tictactoe.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {
    @Query("SELECT * FROM game_table")
    fun getAllGames(): Flow<List<GameEntity>>

    @Query("SELECT * FROM game_table WHERE (p1_name == :p1 and p2_name == :p2) or (p1_name == :p2 and p2_name == :p2)")
    fun getGamesWithPlayers(p1: String, p2: String): Flow<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(gameEntity: GameEntity)

    @Query("DELETE FROM game_table")
    suspend fun deleteAll()

}