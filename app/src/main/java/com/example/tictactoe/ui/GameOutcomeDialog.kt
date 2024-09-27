package com.example.tictactoe.ui

import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.tictactoe.R
import com.example.tictactoe.databinding.FragmentGameOutcomeDialogBinding
import com.example.tictactoe.domain.GameOutcome


class GameOutcomeDialog : DialogFragment() {
    private lateinit var binding: FragmentGameOutcomeDialogBinding
    private val viewModel: GameViewModel by activityViewModels()
    //private val args: GameOutcomeDialogArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_game_outcome_dialog, container, false)
        val view = binding.root
        when(viewModel.gameOutcome.value) {
            GameOutcome.WIN_PLAYER_1 -> binding.gameOutcomeDisplay.text = "Congrats ${viewModel.playerNames.value?.first}"
            GameOutcome.WIN_PLAYER_2 -> binding.gameOutcomeDisplay.text = "Congrats ${viewModel.playerNames.value?.second}"
            else                     -> binding.gameOutcomeDisplay.text = "Its a draw"
        }
        return view
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        viewModel.startGame()
    }
}