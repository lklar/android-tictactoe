package com.example.tictactoe.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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


class History : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: GameViewModel by activityViewModels()
    private var adapter = GameEntityListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.previousGames.observe(this, Observer<List<GameEntity>> { previousGames ->
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