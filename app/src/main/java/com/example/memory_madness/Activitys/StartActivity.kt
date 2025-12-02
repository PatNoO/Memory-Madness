package com.example.memory_madness.Activitys

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.memory_madness.DataClass.Player
import com.example.memory_madness.ViewModell.PlayerViewModel
import com.example.memory_madness.R
import com.example.memory_madness.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding
    private lateinit var player: Player
    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerViewModel = ViewModelProvider(this)[PlayerViewModel::class.java]
        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        player = (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("player", Player::class.java)!!
        } else {
            intent.getSerializableExtra("player") as Player
        })

        playerViewModel.player.observe(this) { (name, difficulty, pauseChoice, time, moves) ->
            binding.tvCurrentDifficultyAs.text = difficulty
        }



        spinner()

        binding.checkBoxEnablePauseSa.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                player.pauseChoice = "on"
                playerViewModel.enablePause(player)
            } else {
                player.pauseChoice = "off"
                playerViewModel.enablePause(player)

            }
        }
        startGame()

    }

    /**
     * Player chooses difficulty
     * sets player difficulty
     * updates PlayerViewModel difficulty
     */
    private fun chooseDifficulty(position: Int) {
        when (position) {
            0 -> ""
            1 -> {
                player.difficulty = "easy"
                playerViewModel.setDifficulty(player)
            }

            2 -> {
                player.difficulty = "medium"
                playerViewModel.setDifficulty(player)
            }

            3 -> {
                player.difficulty = "hard"
                playerViewModel.setDifficulty(player)
            }
        }
    }

    /**
     * initiates or creates the spinner with different difficulty choices
     */
    private fun spinner() {
        val difficulty = listOf("Choose Difficulty", "easy", "medium", "hard")

        val adapter = ArrayAdapter(this, R.layout.spinner_text, difficulty)
        adapter.setDropDownViewResource(R.layout.spinner_dropdown)
        binding.spinnerDifficultyAs.adapter = adapter

        binding.spinnerDifficultyAs.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    chooseDifficulty(position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }

            }
    }

    /**
     * Sets player name and starts the game via start button
     */
    private fun startGame() {


        binding.btnStartAs.setOnClickListener {

            if (binding.etInputNameAs.text.isNullOrEmpty()) {
                Toast.makeText(this, "Field Cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            player.name = binding.etInputNameAs.text.toString()

            val resultIntent = Intent().apply {
                putExtra("player_updated", player)
            }
            setResult(RESULT_OK, resultIntent)
            finish()

        }
    }
}