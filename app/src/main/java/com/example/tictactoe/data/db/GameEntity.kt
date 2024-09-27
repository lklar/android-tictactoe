package com.example.tictactoe.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.tictactoe.domain.GameOutcome

@Entity(tableName = "game_table")
class GameEntity (
    @ColumnInfo(name = "p1_name")    val p1Name: String,
    @ColumnInfo(name = "p2_name")    val p2Name: String,
    @ColumnInfo(name = "p1_icon")    val p1Icon: Int,
    @ColumnInfo(name = "p2_icon")    val p2Icon: Int,
    @ColumnInfo(name = "outcome")    val outcome: GameOutcome,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)