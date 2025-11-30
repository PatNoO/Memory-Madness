package com.example.memory_madness.Activitys

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.memory_madness.Fragments.game_play.EasyFragment
import com.example.memory_madness.Fragments.game_play.HardFragment
import com.example.memory_madness.Fragments.game_play.MediumFragment
import com.example.memory_madness.Player
import com.example.memory_madness.PlayerViewModel
import com.example.memory_madness.R
import com.example.memory_madness.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var player : Player = Player("Default", "easy", 0, 0)
    private lateinit var playerViewModel : PlayerViewModel
    private lateinit var binding: ActivityMainBinding

    private val startLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

        if (result.resultCode == RESULT_OK) {

            val playerUpdated = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                 result.data?.getSerializableExtra("player_updated", Player::class.java)

            } else {
                result.data?.getSerializableExtra("player_updated") as Player
            }

            playerUpdated?.let { player ->
                playerViewModel.setName(player)
                playerViewModel.setDifficulty(player)
            }
            binding.tvMainAm.text = playerViewModel.player.value?.difficulty.toString()
            startGamePlay()
        }
    }
    //// todo Lägg till Highscore bäst kontra sämsta tid och drag

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        playerViewModel = ViewModelProvider(this)[PlayerViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initStartActivity()


    }

    private fun startGamePlay() {
        if (playerViewModel.player.value?.difficulty == "easy") {
            startEasyGame()
        } else if (playerViewModel.player.value?.difficulty == "medium") {
            startMediumGame()
        } else if (playerViewModel.player.value?.difficulty == "hard") {
            startHardGame()
        } else {
            startEasyGame()
        }
    }

    fun startHardGame () {
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fcv_game_plan_am, HardFragment())
            commit()
        }
    }

    fun startMediumGame () {
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


//    override fun updatePlayer(moves: Int, time: Int) {
////        player?.moves = moves
////        binding.tvMainAm.text = moves.toString()
//        val minutes = time / 60
//        val seconds = time % 60
//        binding.tvMainAm.text = "Time $minutes : $seconds"
//
//    }


}