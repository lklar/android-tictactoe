package com.example.tictactoe.di

import android.content.Context
import androidx.room.Room
import com.example.tictactoe.data.db.GameDao
import com.example.tictactoe.data.db.GameDatabase
import com.example.tictactoe.data.repository.GameRepository
import com.example.tictactoe.domain.GameManager
import com.example.tictactoe.domain.PlayerManager

interface AppModule {
    val gameDao: GameDao
    val gameDatabase: GameDatabase
    val gameRepository: GameRepository
    val gameManager: GameManager
    val playerManager: PlayerManager
}

class AppModuleImpl(
    private val appContext: Context
): AppModule {

    override val gameDatabase: GameDatabase by lazy {
        Room.databaseBuilder(
            appContext,
            GameDatabase::class.java,
            "game_database"
        ).build()
    }

    override val gameDao: GameDao by lazy {
        gameDatabase.gameDao()
    }

    override val gameRepository: GameRepository by lazy {
        GameRepository(gameDao)
    }

    override val gameManager: GameManager by lazy {
         GameManager()
    }

    override val playerManager: PlayerManager by lazy {
        PlayerManager()
    }


}