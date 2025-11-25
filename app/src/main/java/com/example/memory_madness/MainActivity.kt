package com.example.memory_madness

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts

import androidx.appcompat.app.AppCompatActivity
import com.example.memory_madness.Fragments.game_play.EasyFragment
import com.example.memory_madness.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var player : Player? = Player("Default",0.0,0)
//    private var player2 : Player? = Player("Default2",0.0,0)

    private lateinit var binding: ActivityMainBinding

    private val startLancher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == RESULT_OK) {
            val playerUpdated = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                 result.data?.getSerializableExtra("player_updated", Player ::class.java)
            } else {
                result.data?.getSerializableExtra("player_updated") as Player
            }
            player = playerUpdated
            binding.tvMainAm.text = player?.name.toString()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //// todo Lägg till Highscore bäst kontra sämst tid och drag

       val intent = Intent(this, StartActivity::class.java)
        intent.putExtra("player", player)
        startLancher.launch(intent)
        onPause()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fv_game_plan_am, EasyFragment(), "easy_fragment")
            .addToBackStack(null)
            .commit()

    }
}