package com.example.tictactoe.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.tictactoe.R
import com.example.tictactoe.data.db.GameEntity
import com.example.tictactoe.databinding.HistoryItemBinding
import com.example.tictactoe.domain.GameOutcome

class GameEntityListAdapter(private val clickListener: (GameEntity?) -> Unit) : ListAdapter<GameEntity, GameEntityListAdapter.GameViewHolder>(GAME_ENTITY_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return GameViewHolder.create(binding)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, clickListener)
    }

    class GameViewHolder(private val binding: HistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        private val player1TextView: TextView = binding.player1Name
        private val player2TextView: TextView = binding.player2Name

        fun bind(gameEntity: GameEntity?, clickListener: (GameEntity?) -> Unit) {
            player1TextView.text  = gameEntity?.p1Name
            player2TextView.text = gameEntity?.p2Name
            val winnerView: TextView?
            val loserView: TextView?
            val drawViews: List<TextView>?
            when(gameEntity?.outcome) {
                GameOutcome.WIN_PLAYER_1 -> {
                    winnerView = player1TextView
                    loserView  = player2TextView
                    drawViews  = null
                }
                GameOutcome.WIN_PLAYER_2 -> {
                    winnerView = player2TextView
                    loserView  = player1TextView
                    drawViews  = null
                }
                else -> {
                    winnerView = null
                    loserView  = null
                    drawViews  = listOf(player1TextView, player2TextView)
                }
            }
            winnerView?.setTextAppearance(R.style.victor)
            winnerView?.setBackgroundResource(android.R.color.holo_orange_light)
            loserView?.setTextAppearance(R.style.loser)
            drawViews?.forEach { textView ->
                textView.setTextAppearance(R.style.participant)

            }

            binding.root.setOnClickListener { clickListener(gameEntity) }
        }

        companion object {
            fun create(binding: HistoryItemBinding): GameViewHolder {
                return GameViewHolder(binding)
            }
        }
    }

    companion object {
        private val GAME_ENTITY_COMPARATOR = object : DiffUtil.ItemCallback<GameEntity>() {
            override fun areItemsTheSame(oldItem: GameEntity, newItem: GameEntity): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: GameEntity, newItem: GameEntity): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
