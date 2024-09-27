package com.example.tictactoe

import android.app.Application
import com.example.tictactoe.di.AppModule
import com.example.tictactoe.di.AppModuleImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class TicTacToeApp: Application() {
    companion object {
        lateinit var appModule: AppModule
        lateinit var applicationScope: CoroutineScope
    }

    override fun onCreate() {
        super.onCreate()
        applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)
        appModule = AppModuleImpl(this)
    }
}