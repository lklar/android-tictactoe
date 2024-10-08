package com.example.tictactoe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tictactoe.R
import com.example.tictactoe.data.db.GameEntity
import com.example.tictactoe.databinding.FragmentHistoryBinding
import com.example.tictactoe.domain.GameOutcome


class History : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: GameViewModel by activityViewModels()
    private lateinit var adapter: GameEntityListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        adapter = GameEntityListAdapter { gameEntity ->
            // Handle item click here
            binding.historyPlayer1.text = gameEntity?.p1Name
            binding.historyPlayer2.text = gameEntity?.p2Name
            val imageViews = listOf(
                binding.historyFieldTopLeft,
                binding.historyFieldTopCenter,
                binding.historyFieldTopRight,
                binding.historyFieldMiddleLeft,
                binding.historyFieldMiddleCenter,
                binding.historyFieldMiddleRight,
                binding.historyFieldBottomLeft,
                binding.historyFieldBottomCenter,
                binding.historyFieldBottomRight
            )
            gameEntity?.board?.toList()?.zip(imageViews)?.forEach { (cellStateChar, imageView) ->
                when(cellStateChar) {
                    '0' -> imageView.setImageResource(R.drawable.ic_tictactoe_empty)
                    '1' -> imageView.setImageResource(R.drawable.ic_tictactoe_player1)
                    '2' -> imageView.setImageResource(R.drawable.ic_tictactoe_player2)
                }
            }
            val winnerView: TextView?
            val loserView: TextView?
            when(gameEntity?.outcome) {
                GameOutcome.WIN_PLAYER_1 -> {
                    winnerView = binding.historyPlayer1
                    loserView  = binding.historyPlayer2
                }
                GameOutcome.WIN_PLAYER_2 -> {
                    winnerView = binding.historyPlayer2
                    loserView  = binding.historyPlayer1
                }
                else                     -> {
                    winnerView = null
                    loserView  = null
                }
            }
            winnerView?.setBackgroundResource(android.R.color.holo_orange_light)
            loserView?.setBackgroundResource(android.R.color.transparent)
        }
        viewModel.previousGames.observe(viewLifecycleOwner, Observer<List<GameEntity>> { previousGames ->
            adapter.submitList(previousGames)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        binding.lifecycleOwner = viewLifecycleOwner

        binding.returnButton.setOnClickListener {
            findNavController().navigate(R.id.action_historyScreen_to_mainScreen)
        }

        val recyclerView = binding.recyclerview
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        return binding.root
    }
}