package com.example.tictactoe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.tictactoe.R
import com.example.tictactoe.TicTacToeApp
import com.example.tictactoe.databinding.FragmentMainScreenBinding
import com.example.tictactoe.domain.GameOutcome
import kotlinx.coroutines.launch

class MainScreen : Fragment() {

    private val viewModel: GameViewModel by activityViewModels()

    private lateinit var binding: FragmentMainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.changedCell.observe(this, Observer<DrawableCell> { cell ->
            getButtons(binding)[cell.id].setImageResource(cell.drawableId)
        })

        viewModel.playerNames.observe(this, Observer<Pair<String, String>> { (name1, name2) ->
            binding.playerName1.text = name1
            binding.playerName2.text = name2
        })

        viewModel.playerScores.observe(this, Observer<Pair<Int,Int>> { (score1, score2) ->
            binding.playerScore1.text = score1.toString()
            binding.playerScore2.text = score2.toString()
        })

        viewModel.gameOutcome.observe(this, Observer<GameOutcome> { gameOutcome ->
            findNavController().navigate(R.id.action_mainScreen_to_gameOutcomeDialog)
        })

        viewModel.currentTurn.observe(this, Observer<PlayerTurn> { currentTurn ->
            when(currentTurn){
                PlayerTurn.PLAYER_1 -> binding.currentTurnImage.setImageResource(R.drawable.ic_arrow_left)
                PlayerTurn.PLAYER_2 -> binding.currentTurnImage.setImageResource(R.drawable.ic_arrow_right)
            }

        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main_screen, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        getButtons(binding).withIndex().forEach { (index, button) ->
            button.setOnClickListener {
                TicTacToeApp.applicationScope.launch {
                    viewModel.processDataInput(index)
                }
            }
        }
        binding.restartButton.setOnClickListener {
            viewModel.startGame()
        }
        binding.settingsButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreen_to_nameSelectionScreen)
        }
        binding.historyButton.setOnClickListener {
            findNavController().navigate(R.id.action_mainScreen_to_historyScreen)
        }
        viewModel.startGame()
        return binding.root
    }

    private fun getButtons(binding: FragmentMainScreenBinding): List<ImageButton> {
        return listOf(
            binding.buttonTopLeft,
            binding.buttonTopCenter,
            binding.buttonTopRight,
            binding.buttonMiddleLeft,
            binding.buttonMiddleCenter,
            binding.buttonMiddleRight,
            binding.buttonBottomLeft,
            binding.buttonBottomCenter,
            binding.buttonBottomRight
        )
    }
}

//    Restore with
//    viewModel.restoreGameState(savedInstanceState)
//    override fun onSaveInstanceState(outState: Bundle) {
//        //viewModel.saveGameState(outState)
//        //println("Fragment main: Saving instance state")
//        //viewModel.printBoard()
//        super.onSaveInstanceState(outState)
//    }
