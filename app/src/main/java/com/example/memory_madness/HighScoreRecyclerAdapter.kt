package com.example.memory_madness

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.memory_madness.DataClass.Player
import com.example.memory_madness.databinding.ListHighScoreItemBinding

class HighScoreRecyclerAdapter(private var players: List<Player>) :
    RecyclerView.Adapter<HighScoreRecyclerAdapter.HighScoreViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HighScoreViewHolder {
        val binding =
            ListHighScoreItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HighScoreViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: HighScoreViewHolder,
        position: Int
    ) {
        val player = players[position]

        val minutes = player.time?.div(60)
        val seconds = player.time?.rem(60)

        holder.binding.tvPlayerName.text = "${player.name}"
        holder.binding.tvDifficulty.text = holder.itemView.context.getString(R.string.difficulty_highscore, player.difficulty)
        holder.binding.tvPause.text = holder.itemView.context.getString(R.string.pause_help_highscore, player.pauseChoice)
        holder.binding.tvTimeLeft.text = holder.itemView.context.getString(R.string.time_left_highscore, minutes, seconds)
        holder.binding.tvMoves.text = holder.itemView.context.getString(R.string.moves_highscore, player.moves)
    }

    override fun getItemCount(): Int {
        return players.size
    }

    fun updateList(updatedList: List<Player>) {
        players = updatedList
        notifyDataSetChanged()
    }


    class HighScoreViewHolder(val binding: ListHighScoreItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}
