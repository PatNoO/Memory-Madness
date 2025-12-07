package com.example.memory_madness.activitys

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.memory_madness.enum_class.CardTheme
import com.example.memory_madness.fragments.game_play.EasyFragment
import com.example.memory_madness.fragments.game_play.HardFragment
import com.example.memory_madness.fragments.game_play.MediumFragment
import com.example.memory_madness.data_class.Player
import com.example.memory_madness.view_model.PlayerViewModel
import com.example.memory_madness.R
import com.example.memory_madness.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var player: Player = Player("Default", "easy", "", 0, 0, CardTheme.HALLOWEEN_THEME)
    private lateinit var playerViewModel: PlayerViewModel
    private lateinit var binding: ActivityMainBinding

    private val startLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == RESULT_OK) {

                val playerUpdated = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    result.data?.getSerializableExtra("player_updated", Player::class.java)

                } else {
                    result.data?.getSerializableExtra("player_updated") as Player
                }

                playerUpdated?.let { player ->
                    playerViewModel.setName(player)
                    playerViewModel.setDifficulty(player)
                    playerViewModel.enablePause(player)
                }

                // starts Game
                startGamePlay()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerViewModel = ViewModelProvider(this)[PlayerViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Starts Start Activity
        initStartActivity()
    }

    /**
     * Starts Game
     * Checks difficulty so correct Game Starts
     */
    private fun startGamePlay() {
        when (playerViewModel.player.value?.difficulty) {
            "easy" -> {
                startEasyGame()
            }
            "medium" -> {
                startMediumGame()
            }
            "hard" -> {
                startHardGame()
            }
            else -> {
                startEasyGame()
            }
        }
    }

    /**
     * Starts Game level HARD
     */
    fun startHardGame() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fcv_game_plan_am, HardFragment())
            commit()
        }
    }

    /**
     * Starts Game level MEDIUM
     */
    fun startMediumGame() {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fcv_game_plan_am, MediumFragment())
            commit()
        }
    }

    /**
     * Starts Game level EASY
     */
    private fun startEasyGame() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcv_game_plan_am, EasyFragment())
            .commit()
    }

    /**
     * Initiates : Start activity to collect with intent Player Name and Difficulty
     */

    private fun initStartActivity() {
        val intent = Intent(this, StartActivity::class.java)
        intent.putExtra("player", player)
        startLauncher.launch(intent)
        onPause()
    }

}