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
import com.example.tictactoe.domain.GameOutcome

class GameEntityListAdapter : ListAdapter<GameEntity, GameEntityListAdapter.GameViewHolder>(GAME_ENTITY_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        return GameViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val player1TextView: TextView = itemView.findViewById(R.id.player_1_name)
        private val player2TextView: TextView = itemView.findViewById(R.id.player_2_name)

        fun bind(gameEntity: GameEntity?) {
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
        }

        companion object {
            fun create(parent: ViewGroup): GameViewHolder {
                val view: View = LayoutInflater.from(parent.context)
                    .inflate(R.layout.history_item, parent, false)
                return GameViewHolder(view)
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
