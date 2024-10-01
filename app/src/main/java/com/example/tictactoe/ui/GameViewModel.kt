package com.example.tictactoe.ui

import SingleLiveEvent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tictactoe.R
import com.example.tictactoe.TicTacToeApp
import com.example.tictactoe.data.db.GameEntity
import com.example.tictactoe.data.db.GameMapper
import com.example.tictactoe.data.repository.GameRepository
import com.example.tictactoe.domain.CellState
import com.example.tictactoe.domain.GameManager
import com.example.tictactoe.domain.GameOutcome
import com.example.tictactoe.domain.PlayerManager
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.launch

class GameViewModel: ViewModel() {

    private val gameRepository: GameRepository =  TicTacToeApp.appModule.gameRepository

    private val gameManager: GameManager = TicTacToeApp.appModule.gameManager
    private val playerManager: PlayerManager = TicTacToeApp.appModule.playerManager
    private val compositeDisposable = CompositeDisposable()

    private val _changedCell = MutableLiveData<DrawableCell>()
    val changedCell: LiveData<DrawableCell> = _changedCell

    private val _playerNames = MutableLiveData<Pair<String, String>>()
    val playerNames: LiveData<Pair<String, String>> = _playerNames

    private val _playerScores = MutableLiveData<Pair<Int, Int>>()
    val playerScores: LiveData<Pair<Int, Int>> = _playerScores

    val gameOutcome = SingleLiveEvent<GameOutcome>()

    private val _currentTurn = MutableLiveData<PlayerTurn>()
    val currentTurn: LiveData<PlayerTurn> = _currentTurn

    private val _previousGames = MutableLiveData<List<GameEntity>>()
    val previousGames: LiveData<List<GameEntity>> = _previousGames

    init {
        viewModelScope.launch {
            gameRepository.allGames.collect { games ->
                _previousGames.value = games
            }
        }

        playerManager.fetchNames()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { names ->
                onPlayerNameChange(names)
        }.let { compositeDisposable.add(it) }

        playerManager.fetchScores()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { scores ->
                _playerScores.value = scores
        }.let { compositeDisposable.add(it) }

        Observable.combineLatest(
                playerManager.fetchDrawableIds(),
                gameManager.fetchCellChanges()
            ) {
                drawableIds, changedCell ->
                println("Got cells: ${drawableIds}, ${changedCell.state}")
                val cellId = changedCell.row * 3 + changedCell.col
                val drawableId = when(changedCell.state) {
                    CellState.EMPTY    -> R.drawable.ic_tictactoe_empty
                    CellState.PLAYER_1 -> drawableIds.first
                    CellState.PLAYER_2 -> drawableIds.second
                }
                DrawableCell(cellId, drawableId)
            }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { drawableCell ->
                _changedCell.value = drawableCell
        }
        .let { compositeDisposable.add(it) }

        gameManager.fetchNextCellState()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { nextCellState ->
                when(nextCellState) {
                    CellState.PLAYER_1 -> _currentTurn.value = PlayerTurn.PLAYER_1
                    CellState.PLAYER_2 -> _currentTurn.value = PlayerTurn.PLAYER_2
                    else -> {}
                }
        }.let { compositeDisposable.add(it) }

        gameManager.fetchGameOutcome()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { outcome ->
                gameOutcome.value = outcome
                playerManager.updateScoreOnGameOutcome(outcome)
                viewModelScope.launch {
                    gameRepository.insert(
                        GameMapper.fromDomain(
                            playerManager.players,
                            outcome
                        )
                    )
                }
        }.let { compositeDisposable.add(it) }

        playerManager.players.first.drawableId = R.drawable.ic_tictactoe_player1
        playerManager.players.second.drawableId = R.drawable.ic_tictactoe_player2
    }

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    private fun onPlayerNameChange(names: Pair<String, String>) {
        _playerNames.value = names
        val winnersNames = previousGames.value?.filter { gameEntity ->
            (gameEntity.p1Name == names.first  && gameEntity.p2Name == names.second) ||
            (gameEntity.p1Name == names.second && gameEntity.p2Name == names.first)
        }?.mapNotNull { gameEntity ->
            when(gameEntity.outcome) {
                GameOutcome.WIN_PLAYER_1 -> gameEntity.p1Name
                GameOutcome.WIN_PLAYER_2 -> gameEntity.p2Name
                else                     -> null
            }
        }
        playerManager.players.first.score  = winnersNames?.count {it == names.first}  ?: 0
        playerManager.players.second.score = winnersNames?.count {it == names.second} ?: 0
    }

    fun processDataInput(id: Int) {
        gameManager.processInput(id / 3, id % 3)
    }

    fun startGame() {
        gameManager.startGame()
    }

    fun setPlayerNames(name1: String?, name2: String?) {
        if (name1 != null)
            playerManager.players.first.name = name1
        if (name2 != null)
            playerManager.players.second.name = name2
    }
}